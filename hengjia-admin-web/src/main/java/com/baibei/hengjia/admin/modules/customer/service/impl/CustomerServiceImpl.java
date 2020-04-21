package com.baibei.hengjia.admin.modules.customer.service.impl;

import com.baibei.hengjia.admin.modules.customer.bean.dto.ChangeStatusDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.SigningDataVo;
import com.baibei.hengjia.admin.modules.customer.dao.CustomerMapper;
import com.baibei.hengjia.admin.modules.customer.model.Customer;
import com.baibei.hengjia.admin.modules.customer.model.CustomerIntegral;
import com.baibei.hengjia.admin.modules.customer.service.ICouponAccountService;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerIntegralService;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerService;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.CustomerIntegralDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.admin.modules.settlement.model.SigningRecord;
import com.baibei.hengjia.admin.modules.settlement.service.ISigningRecordService;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDealOrderService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldTotalService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordIntegralService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordMoneyService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/15 10:07:43
* @description: Customer服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl extends AbstractService<Customer> implements ICustomerService {

    @Autowired
    private CustomerMapper tblCustomerMapper;

    @Autowired
    private IHoldTotalService holdTotalService;

    @Autowired
    private ICustomerIntegralService customerIntegralService;

    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private IRecordIntegralService recordIntegralService;

    @Autowired
    private IDealOrderService dealOrderService;

    @Autowired
    private IRecordMoneyService recordMoneyService;

    @Autowired
    private ICouponAccountService  couponAccountService;
    @Override
    public MyPageInfo<CustomerVo> pageList(CustomerDto customerDto) {
        PageHelper.startPage(customerDto.getCurrentPage(), customerDto.getPageSize());
        List<CustomerVo> customerVos=CustomerVoList(customerDto);
        MyPageInfo<CustomerVo> page = new MyPageInfo<>(customerVos);
        return page;
    }
    public List<CustomerVo> CustomerVoList(CustomerDto customerDto) {
        List<CustomerVo> customerVos=tblCustomerMapper.pageList(customerDto);
        for (int i = 0; i < customerVos.size(); i++) {
            CustomerVo customerVo=customerVos.get(i);
            HoldTotalDto holdTotalDto=new HoldTotalDto();
            holdTotalDto.setCustomerNo(customerVo.getCustomerNo());
            //通过用户编号拿到用户持有集合
            List<HoldTotalVo> holdTotalVos=holdTotalService.getTotal(holdTotalDto);
            //持有本票数量
            BigDecimal totalMain=BigDecimal.ZERO;
            //持有配票数量
            BigDecimal totalMatch=BigDecimal.ZERO;
            //持有市值
            BigDecimal totalHold=BigDecimal.ZERO;
            for (int j = 0; j <holdTotalVos.size() ; j++) {
                if("match".equals(holdTotalVos.get(j).getHoldType())){
                    totalMatch=totalMatch.add(holdTotalVos.get(j).getProductNumber()).setScale(2,BigDecimal.ROUND_DOWN);
                }else if("main".equals(holdTotalVos.get(j).getHoldType())){
                    totalMain=totalMain.add(holdTotalVos.get(j).getProductNumber()==null?BigDecimal.ZERO:holdTotalVos.get(j).getProductNumber()).setScale(2,BigDecimal.ROUND_DOWN);
                }
                totalHold=totalHold.add(holdTotalVos.get(j).getHoldPositionsPrice()).setScale(2,BigDecimal.ROUND_DOWN);
            }
            customerVo.setHoldMain(totalMain);
            customerVo.setHoldMatch(totalMatch);
            //找到积分余额
            CustomerIntegral customerIntegral=customerIntegralService.selectByCustomerNo(customerVos.get(i).getCustomerNo());
            customerVo.setIntegralBalance(customerIntegral==null? BigDecimal.ZERO:customerIntegral.getBalance());
            //用户的券余额
            BigDecimal couponAcount=couponAccountService.findByCustomerNo(customerVos.get(i).getCustomerNo());
            customerVo.setCouponAccount(couponAcount);
            BigDecimal totalAssets =BigDecimal.ZERO;
            //总资产=可用资金+冻结资金+积分余额+持有市值+券金额
            totalAssets=totalAssets.add(customerVo.getBalance()).add(customerVo.getFrozenMoney()).add(totalHold).add(customerVo.getIntegralBalance()).add(couponAcount).setScale(2,BigDecimal.ROUND_DOWN);
            customerVo.setTotalAssets(totalAssets);
            //证件号需要隐藏处理
            if(!StringUtil.isEmpty(customerVo.getIdCard())&&(customerVo.getIdCard().length()>14)){
                customerVo.setIdCard(customerVo.getIdCard().substring(0, 3) + "****" + customerVo.getIdCard().substring(14, customerVo.getIdCard().length()));
            }
            //状态操作
            String status="";
            String[] statusArgs=customerVo.getStatus().split(",");
            for (int j = 0; j <statusArgs.length ; j++) {
                if(j==statusArgs.length-1){
                    status=status+CustomerStatusEnum.getMsg(Integer.valueOf(statusArgs[j]));
                }else{
                    status=status+CustomerStatusEnum.getMsg(Integer.valueOf(statusArgs[j]))+" ";
                }
            }
            customerVo.setStatus(status);
            //真实姓名加密
            customerVo.setRealname(MobileUtils.changeName(customerVo.getRealname()));
            //手机号需要隐藏处理
            customerVo.setMobile(MobileUtils.changeMobile(customerVo.getMobile()));
        }
        return customerVos;
    }
    @Override
    public  List<SigningDataVo> getSigningData(String customerNo) {
        List<SigningDataVo> signingDataVos=new ArrayList<>();
        SigningDataVo signingDataVo=new SigningDataVo();
        SigningRecord signingRecord=signingRecordService.selectByCustomerNo(customerNo);
        Condition condition=new Condition(Customer.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<Customer> customers=tblCustomerMapper.selectByCondition(condition);
        if(customers.size()>0){
            signingDataVo.setCustomerNo(customerNo);
            signingDataVo.setUsername(customers.get(0).getUsername());
            signingDataVo.setRealname(signingRecord==null?"未签约":signingRecord.getCustName());
            signingDataVo.setBankCardNo(signingRecord==null?"未签约":signingRecord.getRelatedAcctId());
            signingDataVo.setIdcard(signingRecord==null?"未签约":signingRecord.getIdCode());
            signingDataVos.add(signingDataVo);
        }
        return signingDataVos;
    }

    @Override
    public ApiResult changeStatus(ChangeStatusDto changeStatusDto) {
        Condition condition=new Condition(Customer.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeStatusDto.getCustomerNo());
        List<Customer> customers=tblCustomerMapper.selectByCondition(condition);
        if(customers.size()>0){
            Customer customer=customers.get(0);
            if(StringUtil.isEmpty(changeStatusDto.getStatus())){
                //如果什么都不传则设置为正常
                changeStatusDto.setStatus("正常");
            }
            customer.setModifyTime(new Date());
            String status="";
            String[] statusArgs=changeStatusDto.getStatus().split(",");
            for (int j = 0; j <statusArgs.length ; j++) {
                if(j==statusArgs.length-1){
                    status=status+CustomerStatusEnum.getCode(statusArgs[j]);
                }else{
                    status=status+CustomerStatusEnum.getCode(statusArgs[j])+",";
                }
            }
            customer.setCustomerStatus(status);
            update(customer);
            return ApiResult.success(changeStatusDto.getStatus().replace(","," "));
        }else {
            return ApiResult.badParam("用户不存在");
        }
    }

    @Override
    public MyPageInfo<CustomerIntegralVo> integralPageList(CustomerIntegralDto customerIntegralDto) {
        PageHelper.startPage(customerIntegralDto.getCurrentPage(), customerIntegralDto.getPageSize());
        List<CustomerIntegralVo> customerIntegralVos=integralVoList(customerIntegralDto);
        MyPageInfo<CustomerIntegralVo> page = new MyPageInfo<>(customerIntegralVos);
        return page;
    }

    @Override
    public List<CustomerIntegralVo> integralVoList(CustomerIntegralDto customerIntegralDto) {
        List<CustomerIntegralVo> customerIntegralVos=tblCustomerMapper.integralPageList(customerIntegralDto);
        for (int i = 0; i <customerIntegralVos.size() ; i++) {
            CustomerIntegralVo customerIntegralVo=customerIntegralVos.get(i);
            //查询所有出金手续费
            BigDecimal sumWithdrawFee=recordMoneyService.findSumByDateAndCustomerNoAndTradeType(customerIntegralVo.getCustomerNo(),"108",customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            //查询所有出金手续费回退的金额
            BigDecimal backWithdrawFee=recordMoneyService.findSumByDateAndCustomerNoAndTradeType(customerIntegralVo.getCustomerNo(),"109",customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            sumWithdrawFee=sumWithdrawFee.add(backWithdrawFee);
            //查询所有交易手续费
            BigDecimal tradeFee=dealOrderService.findTradeFeeByCustomerNo(customerIntegralVo.getCustomerNo(),customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            //消费积分
            BigDecimal consumeIntegral=recordIntegralService.findByTradetypeAndCustomerNo("101",customerIntegralVo.getCustomerNo(),customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            //获取积分
            BigDecimal getIntegral=recordIntegralService.findByTradetypeAndCustomerNo("102",customerIntegralVo.getCustomerNo(),customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            //回退积分
            BigDecimal backIntegral=recordIntegralService.findByTradetypeAndCustomerNo("103",customerIntegralVo.getCustomerNo(),customerIntegralDto.getStartTime(),customerIntegralDto.getEndTime());
            if(StringUtils.isEmpty(consumeIntegral)){
                consumeIntegral=BigDecimal.ZERO;
            }
            if(StringUtils.isEmpty(backIntegral)){
                backIntegral=BigDecimal.ZERO;
            }
            consumeIntegral=consumeIntegral.add(backIntegral);
            customerIntegralVo.setWithdrawFee(sumWithdrawFee.setScale(2,BigDecimal.ROUND_DOWN));
            customerIntegralVo.setTradeFee(tradeFee.setScale(2,BigDecimal.ROUND_DOWN));
            customerIntegralVo.setConsumeIntegral(consumeIntegral==null?BigDecimal.ZERO:consumeIntegral);
            customerIntegralVo.setGetIntegral(getIntegral==null?BigDecimal.ZERO:getIntegral);
            //姓名加密处理
            customerIntegralVo.setRealname(MobileUtils.changeName(customerIntegralVo.getRealname()));
        }
        return customerIntegralVos;
    }
}

