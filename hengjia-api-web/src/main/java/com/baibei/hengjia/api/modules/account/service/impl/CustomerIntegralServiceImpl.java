package com.baibei.hengjia.api.modules.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.bean.dto.IntegralDetailDto;
import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.account.dao.CustomerIntegralMapper;
import com.baibei.hengjia.api.modules.account.dao.RecordIntegralMapper;
import com.baibei.hengjia.api.modules.account.model.CustomerIntegral;
import com.baibei.hengjia.api.modules.account.model.RecordIntegral;
import com.baibei.hengjia.api.modules.account.service.ICustomerIntegralService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:40:48
* @description: CustomerIntegral服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CustomerIntegralServiceImpl extends AbstractService<CustomerIntegral> implements ICustomerIntegralService {

    @Autowired
    private CustomerIntegralMapper customerIntegralMapper;


    @Autowired
    private RecordIntegralMapper recordIntegralMapper;

    @Override
    public void changeIntegral(ChangeIntegralDto changeIntegralDto){
        log.info("修改积分数额传入的值为"+JSONObject.toJSONString(changeIntegralDto));
        //先通过用户编号以及积分类型查找积分账户
        Condition condition=new Condition(CustomerIntegral.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeIntegralDto.getCustomerNo());
        criteria.andEqualTo("integralNo",changeIntegralDto.getIntegralNo());
        List<CustomerIntegral> CustomerIntegrals=customerIntegralMapper.selectByCondition(condition);
        if(CustomerIntegrals.size()<1){
            throw new ServiceException("积分账户不存在");
        }
        CustomerIntegral integral=CustomerIntegrals.get(0);
        //乐观锁
        Condition updateCondition=new Condition(CustomerIntegral.class);
        Example.Criteria updateCriteria=buildValidCriteria(updateCondition);
        updateCriteria.andEqualTo("customerNo",changeIntegralDto.getCustomerNo());
        updateCriteria.andEqualTo("balance",integral.getBalance());
        updateCriteria.andEqualTo("integralNo",changeIntegralDto.getIntegralNo());
        if(changeIntegralDto.getReType()==1){
            //支出
            integral.setBalance(integral.getBalance().subtract(changeIntegralDto.getChangeAmount()));
            if(integral.getBalance().compareTo(BigDecimal.ZERO)<0){
                throw new ServiceException("积分余额不足");
            }
        }else if(changeIntegralDto.getReType()==2){
            //收入
            integral.setBalance(integral.getBalance().add(changeIntegralDto.getChangeAmount()));
        }else {
            throw new ServiceException("收入支出类型错误");
        }
        integral.setModifyTime(new Date());
        //插入一条积分流水
        insertRecordIntegral(changeIntegralDto,integral);
        if (!updateByConditionSelective(integral,updateCondition)) {
            throw new ServiceException("积分操作失败");
        }
    }


    @Override
    public ApiResult changeIntegralSpecil(ChangeIntegralDto changeIntegralDto){
        log.info("修改积分数额传入的值为"+JSONObject.toJSONString(changeIntegralDto));
        //先通过用户编号以及积分类型查找积分账户
        Condition condition=new Condition(CustomerIntegral.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeIntegralDto.getCustomerNo());
        criteria.andEqualTo("integralNo",changeIntegralDto.getIntegralNo());
        List<CustomerIntegral> CustomerIntegrals=customerIntegralMapper.selectByCondition(condition);
        if(CustomerIntegrals.size()<1){
            throw new ServiceException("积分账户不存在");
        }
        CustomerIntegral integral=CustomerIntegrals.get(0);
        //乐观锁
        Condition updateCondition=new Condition(CustomerIntegral.class);
        Example.Criteria updateCriteria=buildValidCriteria(updateCondition);
        updateCriteria.andEqualTo("customerNo",changeIntegralDto.getCustomerNo());
        updateCriteria.andEqualTo("balance",integral.getBalance());
        updateCriteria.andEqualTo("integralNo",changeIntegralDto.getIntegralNo());
        if(changeIntegralDto.getReType()==1){
            //支出
            integral.setBalance(integral.getBalance().subtract(changeIntegralDto.getChangeAmount()));
        }else if(changeIntegralDto.getReType()==2){
            //收入
            if(integral.getBalance().compareTo(BigDecimal.ZERO)<0){
                throw new ServiceException("积分余额不足");
            }
            integral.setBalance(integral.getBalance().add(changeIntegralDto.getChangeAmount()));
        }else {
            throw new ServiceException("收入支出类型错误");
        }
        integral.setModifyTime(new Date());
        //插入一条积分流水
        insertRecordIntegral(changeIntegralDto,integral);
        if (!updateByConditionSelective(integral,updateCondition)) {
            throw new ServiceException("积分操作失败");
        }
        return ApiResult.success();
    }


    /**
     * 通过用户编码查找积分信息
     * @param integralDetailDto
     * @return
     * @throws ServiceException
     */
    @Override
    public IntegralDetailVo findIntegralDetailByCustomer(IntegralDetailDto integralDetailDto) throws ServiceException {
        return customerIntegralMapper.findIntegralDetailByCustomer(integralDetailDto);
    }

    /**
     * 插入积分流水
     * @param changeIntegralDto
     * @param integral
     */
    private void insertRecordIntegral(ChangeIntegralDto changeIntegralDto, CustomerIntegral integral) {
        RecordIntegral recordIntegral=new RecordIntegral();
        String recordNo=IdWorker.get32UUID();
        recordIntegral.setRecordNo(recordNo);
        //交易商编码
        recordIntegral.setCustomerNo(integral.getCustomerNo());
        String change=changeIntegralDto.getReType()==1? "-":"";
        //变动数额
        recordIntegral.setChangeAmount(change+changeIntegralDto.getChangeAmount().toString());
        //可用积分数额
        recordIntegral.setBalance(integral.getBalance());
        //交易类型
        recordIntegral.setTradeType(changeIntegralDto.getTradeType());
        //积分类型
        recordIntegral.setIntegralNo(changeIntegralDto.getIntegralNo());
        //订单号
        recordIntegral.setOrderNo(changeIntegralDto.getOrderNo());
        //类型 1：支出，2：收入
        recordIntegral.setRetype(changeIntegralDto.getReType());
        recordIntegral.setCreateTime(new Date());
        recordIntegral.setModifyTime(new Date());
        if(IntegralTradeTypeEnum.DELIVERY.getCode()==changeIntegralDto.getTradeType()){
            recordIntegral.setRemark(CouponAccountTradeTypeEnum.DELIVERY.getMsg());
        }
        //是否有效
        recordIntegral.setFlag(new Byte("1"));
        recordIntegralMapper.insert(recordIntegral);
    }
}
