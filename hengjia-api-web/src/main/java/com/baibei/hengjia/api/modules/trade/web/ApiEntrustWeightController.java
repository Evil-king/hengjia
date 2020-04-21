package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IEntrustWeightService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/7 10:32 AM
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api/entrustWeight")
public class ApiEntrustWeightController {
    @Autowired
    private IEntrustWeightService entrustWeightService;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private ITradeDayService tradeDayService;

    private static final String PRODUCT_TRADE_NO = "0002";


    /**
     * 每日休市后计算客户挂单的权重
     *
     * @return
     */
    @GetMapping("calculation")
    public ApiResult<String> calculation() {
        log.info("正在执行权重计算...");
        long start = System.currentTimeMillis();
        /*// 休市时间才能计算
        boolean trade = tradeDayService.isTradeTime();
        if (trade) {
            return ApiResult.badParam("开市中,不允许计算权重");
        }*/
        // 先把之前的数据全清除掉
        int count = entrustWeightService.deleteAll();
        log.info("此次清除数据条数为{}", count);
        // 找出有可卖数量的客户,进行权重计算
        List<HoldTotal> holdTotalList = holdTotalService.findCanSellHoldTotal(PRODUCT_TRADE_NO);
        Set<String> set = holdTotalList.stream().map(HoldTotal::getCustomerNo).collect(Collectors.toSet());
        // 获取到下一个交易日
        Date tradeDay = tradeDayService.getAddNTradeDay(new Date(), 1);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            entrustWeightService.calculation(iterator.next(), PRODUCT_TRADE_NO, DateUtil.getEndDay(tradeDay));
        }
        log.info("权重计算完毕,耗时{}", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }
}
