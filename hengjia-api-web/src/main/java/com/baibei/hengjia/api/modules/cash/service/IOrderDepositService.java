package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.model.OrderDeposit;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/06/03 20:37:57
 * @description: OrderDeposit服务接口
 */
public interface IOrderDepositService extends Service<OrderDeposit> {


    /**
     * 入金申请服务接口
     * 当用户提交入金请求时,生成服入金订单记录交给后台审核
     *
     * @param orderDepositDto
     * @return
     */
    ApiResult<Boolean> depositApplicationService(OrderDepositDto orderDepositDto);

    /**
     * 根据外部流水号查询入金流水(1005)
     * @param externalNo
     * @return
     */
    OrderDeposit getOrderByExternalNo(String externalNo);

    /**
     * 获取某天的入金流水
     * @param period 时间，格式：2019-06-26
     * @return
     */
    List<OrderDeposit> getPeriodOrderList(String period);

    /**
     * 获取某天的入金流水，该入金流水不在银行的入金流水内
     * @param period 时间，格式：2019-06-26
     * @return
     */
    List<OrderDeposit> getPeriodOrderListNotInBankOrders(String period);

    OrderDeposit getOrderByOrderNo(String orderNo);
}
