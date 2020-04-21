package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.settlement.biz.AmountReturnBiz;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/2 4:45 PM
 * @description: 手续费/积分返还办理数据生成
 */
@RestController
@RequestMapping("/api/amountReturn")
@Slf4j
public class ApiAmountReturnController {
    @Autowired
    private AmountReturnBiz amountReturnBiz;


    /**
     * 数据生成
     *
     * @return
     */
    @GetMapping("/generate")
    public ApiResult<String> generate() {
        log.info("正在执行业务办理数据生成");
        amountReturnBiz.generate();
        return ApiResult.success();
    }

    /**
     * 定时任务——积分返还办理
     *
     * @return
     */
    @GetMapping("/integralReturn")
    public ApiResult integralReturn() {
        log.info("正在执行积分返还办理");
        long start = System.currentTimeMillis();
        ApiResult result = amountReturnBiz.integralReturnNew();
        log.info("积分返还办理执行完毕，result={}，耗时{}", result.toString(), (System.currentTimeMillis() - start));
        return result;
    }
}
