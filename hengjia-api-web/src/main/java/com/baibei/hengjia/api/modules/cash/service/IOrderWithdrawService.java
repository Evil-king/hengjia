package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author: uqing
 * @date: 2019/06/06 15:35:29
 * @description: OrderWithdraw服务接口
 */
public interface IOrderWithdrawService extends Service<OrderWithdraw> {

    ApiResult withdrawApplicationApplication(OrderWithdrawDto orderWithdrawDto);

    /**
     * 入金申请
     * 交易网-->银行(1318接口文档中的代号)
     *
     * @return
     */
    void queryWithdrawTask(OrderWithdraw orderWithdraw);

    /**
     * 查询出金订单
     * 交易网-->银行(1352接口文档中的代号)
     */
    void queryWithdrawOrderTask(OrderWithdraw orderWithdraw);

    /**
     * 银行发起出金请求(接口文档1312)
     *
     * @param message
     * @return
     */
    String withdrawForBank(String message);

    /**
     * 银行发起的异步出金(1317接口)
     */
    void queryDataAsynchronousTask(OrderWithdraw orderWithdraw);

    /**
     * 根据外部流水号查询流水(1005)
     *
     * @param externalNo 外部流水号
     * @return
     */
    OrderWithdraw getOrderByExternalNo(String externalNo);

    /**
     * 获取某天的出金流水
     *
     * @param period 时间，格式：20190626
     * @return
     */
    List<OrderWithdraw> getPeriodOrderList(String period);

    /**
     * 获取某天的出金流水，该出金流水不在银行的出金流水内
     *
     * @param period 时间，格式：20190626
     * @return
     */
    List<OrderWithdraw> getPeriodOrderListNotInBankOrders(String period);

    /**
     * 1010接口 查银行端会员资金台帐余额
     *
     * @param SelectFlag
     * @return
     */
    ApiResult queryMemberBlance(String SelectFlag,String pageNum);

    String operatorMoney(String type, OrderWithdraw order,String sign);

    int updateOrder(OrderWithdraw order, String ourOrderNo, String type, String tranBackMessage);

    /**
     * 对账更新订单
     * @param order
     * @return
     */
    int updateOrderByDiff(OrderWithdraw order);

    /**
     * 定时任务，把出金表中状态为2的改为3
     */
    void findStatusTask(OrderWithdraw orderWithdraw);

    /**
     * 根据出金订单号获取出金信息
     * @param orderNo
     * @return
     */
    OrderWithdraw getByOrderNo(String orderNo);

    /**
     * 查询时间段内所有的出金手续费
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumFee(Map<String, Object> param);


    /**
     * 查询交易网发起状态为3的订单
     * @return
     */
    List<OrderWithdraw> selectDoingList();

    /**
     * 签退的时候将订单状态为1的都置为审核不通过
     */
    void operatorReview();

    /**
     * 判断每个用户当天出金额
     * @param customerNo
     * @return
     */
    BigDecimal sumAmountOfCustomer(String customerNo);

    void clear();

    List<OrderWithdraw> selectByStatus();

}
