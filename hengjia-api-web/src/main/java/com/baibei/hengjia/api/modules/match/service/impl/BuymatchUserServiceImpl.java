package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.bean.vo.BuymatchUsersVo;
import com.baibei.hengjia.api.modules.match.dao.BuymatchUserMapper;
import com.baibei.hengjia.api.modules.match.model.BuymatchUser;
import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.api.modules.match.service.IBuymatchUserService;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.model.Delivery;
import com.baibei.hengjia.api.modules.trade.model.MatchConfig;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.NoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: BuymatchUser服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BuymatchUserServiceImpl extends AbstractService<BuymatchUser> implements IBuymatchUserService {

    @Autowired
    private BuymatchUserMapper buymatchUserMapper;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IMatchConfigService matchConfigService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IMatchAuthorityService matchAuthorityService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private IDeliveryService deliveryService;

    @Override
    public void buyMatchUsers() {
        long start = System.currentTimeMillis();
        log.info("=====开始查询符合买入配货的用户集合，并入库=====");
        //判断当前时间是否休市状态
        boolean tradeDay = tradeDayService.isTradeDay(new Date());
        if(!tradeDay){
            throw new ServiceException("非交易日不能买入配货");
        }
        //判断开关是否打开
        MatchConfig aSwitch = matchConfigService.getSwitch(Constants.SwitchType.BUYMATCH);
        if(aSwitch.getMatchSwitch().equals(Constants.MatchSwitch.OFF))
            throw new ServiceException("买入配货开关没打开");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //批次号
        String batchNo = simpleDateFormat.format(new Date());
        /**
         * 获取符合规则的用户编码集合:以零售价买入商品，获得以批发价买入商品的权利（批发权）。收市后直接扣钱配仓单，收手续费。持仓；类型为：折扣商品。
         * 条件：1.当天有买入商品
         */
        List<BuymatchUsersVo> buymatchUsersVoList = buymatchUserMapper.selectCustomerNos();
        List<BuymatchUser> buymatchUserList = new ArrayList<>();
        Date currentDate = new Date();
        for (BuymatchUsersVo buymatchUsersVo : buymatchUsersVoList) {
            BuymatchUser oneMatchUser = this.getOneMatchUser(batchNo, buymatchUsersVo.getCustomerNo(), buymatchUsersVo.getProductTradeNo());
            if (StringUtils.isEmpty(oneMatchUser)) {
                //新老用户标识。new=新用户，old=老用户
                String newOrOldFlag="old";
               /* //查询用户第一次提货的提货订单信息
                Delivery theFirstDelivery = deliveryService.getTheFirstDelivery(buymatchUsersVo.getCustomerNo());
                *//*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");*//*
                if(StringUtils.isEmpty(theFirstDelivery)){
                    newOrOldFlag="new";
                }else{
                    String deliveryDate = simpleDateFormat.format(theFirstDelivery.getCreateTime());//用户第一次提货时间
                    String currentDateStr = simpleDateFormat.format(new Date());//当前时间
                    if (deliveryDate.equals(currentDateStr)) {//证明是新用户
                        newOrOldFlag="new";
                    }
                }*/
                String deliveryFlag="success";//提货是否成功标识
                //=======去掉用户首提逻辑=======
                /*//查询用户的收货地址
                CustomerAddress customerAddress = customerAddressService.findDefaultByNo(buymatchUsersVo.getCustomerNo());
                if (StringUtils.isEmpty(theFirstDelivery)) {//新用户首提
                    if(!StringUtils.isEmpty(customerAddress)){
                        //生成提货订单
                        DeliveryApplyDto deliveryApplyDto = new DeliveryApplyDto();
                        deliveryApplyDto.setProductTradeNo(buymatchUsersVo.getProductTradeNo());
                        deliveryApplyDto.setHoldType(Constants.HoldType.MAIN);
                        deliveryApplyDto.setDeliveryCount(1);//新商品提货1手
                        deliveryApplyDto.setAddressId(customerAddress.getId());
                        deliveryApplyDto.setCustomerNo(buymatchUsersVo.getCustomerNo());
                        deliveryApplyDto.setValidateTradeDayFlag("1");
                        deliveryApplyDto.setSource(Constants.DeliverySource.PLAN_FIRSTDELIVERY);
                        ApiResult apiResult = deliveryService.deliveryApply(deliveryApplyDto);
                        if(ResultEnum.ERROR.getCode()==apiResult.getCode()){
                            deliveryFlag="fail";
                            log.info("用户买入配货失败，该用户编号为：{}，提货商品为：{}，失败原因为：{}",buymatchUsersVo.getCustomerNo(),buymatchUsersVo.getProductTradeNo(),apiResult.getMsg());
                        }
                    }else{
                        log.info("用户买入配货失败，该用户编号为：{}，提货商品为：{}，失败原因为：{}",buymatchUsersVo.getCustomerNo(),buymatchUsersVo.getProductTradeNo(),"找不到用户的收货地址");
                        deliveryFlag="fail";
                    }
                }*/
                if("success".equals(deliveryFlag)){
                    BuymatchUser buymatchUser = new BuymatchUser();
                    buymatchUser.setBatchNo(batchNo);
                    buymatchUser.setDealNo(NoUtil.getBuyMatchNo());
                    buymatchUser.setCustomerNo(buymatchUsersVo.getCustomerNo());
                    buymatchUser.setProductTradeNo(buymatchUsersVo.getProductTradeNo());
                    buymatchUser.setMatchNum(1L);
                    buymatchUser.setStatus(Constants.BuyMatchUserStatus.UNRUN);
                    buymatchUser.setOperateType(Constants.BuyMatchOperateType.PLAN);
                    buymatchUser.setCreateTime(currentDate);
                    buymatchUser.setModifyTime(currentDate);
                    buymatchUser.setFlag(new Byte(Constants.Flag.VALID));
                    buymatchUserList.add(buymatchUser);
                }
            }
        }
        if(buymatchUserList.size()>0){
            //入库
            buymatchUserMapper.insertList(buymatchUserList);
        }
        log.info("=====买入配货的用户集合入库完毕=====");
        //关闭买入配货开关
        /*int i = matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.BUYMATCH);
        if(i==0)
            throw new ServiceException("买入配货用户入库失败，开关乐观锁问题");*/
        //发送买入配货消息
        log.info("发送买入配货通知,主题为：{}，参数为：{}", RedisConstant.MATCH_BUYMATCH_TOPIC, batchNo);
        /**
         * 注意：这里要先向队列里面插入消息，再发布买入配货通知，这里的顺序不能搞反了。
         * 这样，能够解决多节点重复消费问题。
         */
        redisUtil.leftPush(RedisConstant.MATCH_BUYMATCH_LIST,batchNo);
        redisUtil.pub(RedisConstant.MATCH_BUYMATCH_TOPIC, batchNo);
        log.info("buyMatchUsers time comsuming " + (System.currentTimeMillis() - start) + " ms");

    }
    public void deleteBybatchNo(String batchNo){
        if (StringUtils.isEmpty(batchNo)) {
            throw new ServiceException("批次号不能为空");
        }
        Condition condition = new Condition(BuymatchUser.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("batchNo",batchNo);
        buymatchUserMapper.deleteByCondition(condition);
    }

    @Override
    public List<BuymatchUser> getByBatchNo(String batchNo,String status) {
        if (StringUtils.isEmpty(batchNo)) {
            throw new ServiceException("获取失败，未指定批次号");
        }
        Condition condition = new Condition(BuymatchUser.class);
        condition.setOrderByClause("create_time asc,id");
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("batchNo",batchNo);
        if (!StringUtils.isEmpty(status)) {
            criteria.andEqualTo("status",status);
        }
        List<BuymatchUser> buymatchUserList = buymatchUserMapper.selectByCondition(condition);
        return buymatchUserList;
    }

    @Override
    public List getExistCustomerNos(String batchNo) {
        List<BuymatchUser> buymatchUserList = this.getByBatchNo(batchNo,null);
        List<String> customerNoList= new ArrayList();
        for (BuymatchUser buymatchUser : buymatchUserList) {
            customerNoList.add(buymatchUser.getCustomerNo());
        }
        return customerNoList;
    }

    @Override
    public BuymatchUser getOneMatchUser(String batchNo, String customerNo, String productTradeNo) {
        Condition condition = new Condition(BuymatchUser.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isEmpty(batchNo)) {
            throw new ServiceException("查询配货用户失败，未指定批次号");
        }
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("查询配货用户失败，未指定用户编号");
        }
        if (StringUtils.isEmpty(productTradeNo)) {
            throw new ServiceException("查询配货用户失败，未指定交易商品编号");
        }
        criteria.andEqualTo("batchNo",batchNo);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        List<BuymatchUser> buymatchUsers = buymatchUserMapper.selectByCondition(condition);
        if (buymatchUsers.size()>1) {
            throw new ServiceException("BuymatchUser should select one but more");
        }
        return buymatchUsers.size()==0?null:buymatchUsers.get(0);
    }
}
