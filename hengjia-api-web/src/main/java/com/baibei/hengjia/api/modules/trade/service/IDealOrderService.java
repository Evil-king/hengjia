package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.bean.dto.DealOrderDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.DealOrderVo;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: DealOrder服务接口
 */
public interface IDealOrderService extends Service<DealOrder> {

    MyPageInfo<DealOrderVo> findByCustomerNo(DealOrderDto dealOrderDto);

    /**
     * 当天成交的总货款（作为买方）
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumMoneyForBuy(Map<String, Object> param);

    /**
     * 当天成交的总货款（作为卖方）
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumMoneyForSell(Map<String, Object> param);

    /**
     * 统计当天总手续费
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumFee(Map<String, Object> param);

    /**
     * 当天交易总笔数
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> tradeCount(Map<String, Object> param);

    /**
     * 统计卖出用户的盈利总金额
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumProfitAmountForSell(Map<String, Object> param);


    /**
     * 统计买入用户的亏损总金额
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumLossAmountForBuy(Map<String, Object> param);


    /**
     * 统计时间段内买入卖出手续费综合
     *
     * @param param
     * @return
     */
    BigDecimal sumAllFee(Map<String, Object> param);

    /**
     * 统计时间段内所有积分总和
     *
     * @param param
     * @return
     */
    BigDecimal sumAllIntegral(Map<String,Object> param);

    /**
     * 统计时间段内所有券款总和
     *
     * @param param
     * @return
     */
    BigDecimal sumAllCouponAmount(Map<String,Object> param);


    /**
     * 统计时间段内所有红木基金综合
     *
     * @param param
     * @return
     */
    BigDecimal sumAllHongmuFund(Map<String,Object> param);


    /**
     * 指定日期客户买入的商品手数
     *
     * @param buyCustomerNo
     * @param date
     * @return
     */
    Integer sumBuyCount(String buyCustomerNo,String productTradeNo,String date);

    /**
     * 指定日期客户买入的商品手数
     *
     * @param sellCustomerNo
     * @param date
     * @return
     */
    Integer sumSellCount(String sellCustomerNo,String productTradeNo,String date);

    /**
     * 通过用户编号找到该日期下交易的用户编号
     * @param customerNo
     * @param time
     * @return
     */
    List<String> findTradeProductNo(String customerNo, String time);

    List<DealOrder> sumIntegral(String currentDate);

    /**
     * 判断客户今日是否有买入
     *
     * @param customer
     * @return
     */
    boolean isBuyToday(String customer);

    /**
     * 查询指定日期的交易卖出成交单
     *
     * @param customerNo
     * @param date
     * @return
     */
    List<DealOrder> querySellList(String customerNo, Date date);

    List<CustomerCountVo> sumHongmuFund(Map<String, Object> param);

}
