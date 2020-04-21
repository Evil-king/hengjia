package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.biz.TradeTimeReleaseBiz;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/10 8:11 PM
 * @description: 客户商品持仓明细达到交易日期之后需要增加商品的可卖数量 定时任务处理类
 */
@Slf4j
@RestController
@RequestMapping("/api/tradeTime")
public class ApiTradeTimeTaskController {
    @Autowired
    private TradeTimeReleaseBiz tradeTimeReleaseBiz;

    /**
     * tradeTimeDeal
     *
     * @return
     */
    @GetMapping("/deal")
    public ApiResult tradeTimeDeal() {
        log.info("tradeTimeDeal running...");
        tradeTimeReleaseBiz.release();
        return ApiResult.success();
    }
}
