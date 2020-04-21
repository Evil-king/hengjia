package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryTransferDto;
import com.baibei.hengjia.api.modules.trade.model.Delivery;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author: Longer
 * @date: 2019/06/05 10:46:05
 * @description: 提货订单接口
 */
public interface IDeliveryService extends Service<Delivery> {

    ApiResult deliveryApply(DeliveryApplyDto deliveryApplyDto);

    /**
     * task:1256.用户提货，提的是挂牌商的货，生成的是用户的提货单
     * @param deliveryTransferDto
     * @return
     */
    ApiResult deliveryTransfer(DeliveryTransferDto deliveryTransferDto);




    MyPageInfo<MyDeliveryVo> myDeliveryList(DeliveryQueryDto queryDto);

    /**
     * 获取用户某个商品的提货订单
     *
     * @param customerNo     用户编码
     * @param productTradeNo 商品交易编码
     * @return
     */
    Delivery getDeliveryByProAndCust(String customerNo, String productTradeNo);

    /**
     * 获取用户第一次提货的提货信息
     *
     * @param customerNo
     * @return
     */
    Delivery getTheFirstDelivery(String customerNo);

    /**
     * @param beginTime
     * @param endTime
     * @return
     */
    BigDecimal sumAmount(Date beginTime, Date endTime);

    /**
     * 确认收货
     *
     * @param deliveryApplyDto
     */
    void receipt(DeliveryApplyDto deliveryApplyDto);

    /**
     * 发货以后,根据配置判断多少天自动收货
     * 1、stop 查询所有发货的用户
     */
    void autoReceipt();


}
