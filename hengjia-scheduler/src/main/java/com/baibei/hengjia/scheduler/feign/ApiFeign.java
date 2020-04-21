package com.baibei.hengjia.scheduler.feign;

import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.scheduler.dto.ControlSwitchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/10 8:17 PM
 * @description: API接口调用
 */
@FeignClient(name = "apiFeign", url = "${api.url}")
public interface ApiFeign {


    /**
     * 客户商品持仓到可交易日期之后增加可卖数量
     *
     * @return
     */
    @GetMapping("/api/tradeTime/deal")
    ApiResult tradeTimeDeal();

    /**
     * 平安出金---1325接口
     * 定时查询渠道出金信息(交易网-->银行)
     *
     * @return
     */
    @GetMapping("/api/cash/queryData")
    ApiResult queryData();


    /**
     * 定时执行提货配票
     */
    @PostMapping("/api/deliveryMatch/deliveryMatchs")
    ApiResult deliveryMatch();

    /**
     * 设置开休市时间
     *
     * @return
     */
    @GetMapping("/api/tradeDay/setTradeDay")
    ApiResult setTradeDay();

    /**
     * 每日零点定时将可提现金额以及可用金额对等
     */
    @GetMapping("/api/account/updateWithdrawTiming")
    ApiResult updateWithdrawTiming();

    /**
     * 微信AccessToken和js_ticket刷新
     *
     * @return
     */
    @GetMapping("/api/weixin/updateWxInfo")
    ApiResult updateWxInfo();

    /**
     * 监管签到和签退
     *
     * @return
     */
    @GetMapping("/api/cash/signIn")
    ApiResult signIn();

    /**
     * 签退
     *
     * @return
     */
    @GetMapping("/api/cash/signBack")
    ApiResult signBack();

    /**
     * 积分返还
     *
     * @return
     */
    @GetMapping("/api/amountReturn/integralReturn")
    ApiResult amountReturnGenerate();

    /**
     * 定时更新出金订单状态2改为3
     *
     * @return
     */
    @GetMapping("/api/cash/findStatusTask")
    ApiResult findStatusTask();

    /**
     * 清结算
     *
     * @return
     */
    @GetMapping("/api/settlement/clean")
    ApiResult clean();


    /**
     * 平安出金---1010接口
     * 定时查询台账信息
     *
     * @return
     */
    @GetMapping("/api/cash/queryMemberBlance")
    ApiResult queryMemberBlance();


    /**
     * 14天自动收货
     *
     * @return
     */
    @GetMapping("/api/shop/longDayConfirmSend")
    ApiResult longDayConfirmSend();

    /**
     * 买入配货定时任务
     *
     * @return
     */
    @PostMapping("/api/buyMatch/buyMatchs")
    ApiResult buyMatch();

    /**
     * 系统自动提货定时任务
     *
     * @return
     */
    @PostMapping("/api/buyMatch/sysDelivery")
    ApiResult sysDelivery();

    /**
     * 提货券
     *
     * @return
     */
    @PostMapping("/api/buyMatch/deliveryTransfer")
    ApiResult deliveryTransfer();

    /**
     * 提货券补扣
     * @return
     */
    @PostMapping("/api/buyMatch/offsetDeliveryTicket")
    ApiResult offsetDeliveryTicket();

    /**
     * 将配货失败单失效
     * @return
     */
    @PostMapping("/api/buyMatch/destoryFailLog")
    ApiResult destoryFailLog();


    /**
     * 操作开关
     *
     * @return
     */
    @PostMapping("/api/matchSwitch/controlSwitch")
    ApiResult controlSwitch(ControlSwitchDto controlSwitchDto);


    /**
     * 自动收货
     *
     * @return
     */
    @GetMapping("/api/delivery/autoReceipt")
    ApiResult autoReceipt();


    /**
     * 每日休市后计算客户挂单的权重
     */
    @GetMapping("/api/entrustWeight/calculation")
    ApiResult calculation();

    /**
     * 预约购销截止日期后关闭
     *
     * @return
     */
    @GetMapping("/api/autoTrade/closingTime")
    ApiResult closingTime();

    /**
     * 自动预约购销
     *
     * @return
     */
    @GetMapping("/api/autoTrade/trade")
    ApiResult trade();

    /**
     * 休市后撤单
     *
     * @return
     */
    @GetMapping("/auth/api/entrust/revokeAll")
    ApiResult revokeAll();

    @GetMapping("/api/account/changeCouponAndIntegralAmount")
    ApiResult changeCouponAndIntegralAmount();
}
