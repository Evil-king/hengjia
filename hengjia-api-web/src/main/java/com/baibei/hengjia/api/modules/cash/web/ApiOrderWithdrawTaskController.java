package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.QueryBlanceDto;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hwq
 * @date 2019/06/11
 * <p>
 * 定时查询渠道出金信息
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/cash")
public class ApiOrderWithdrawTaskController {

    @Autowired
    private IOrderWithdrawService orderWithdrawService;

    /**
     * queryData
     * 交易网--->银行(定时任务--1325接口)
     *
     * @return
     */
    @GetMapping("/queryData")
    public void queryData() {
        long start = System.currentTimeMillis();
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService.selectDoingList();
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行1325订单查询，此次处理订单数量为{}", size);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (OrderWithdraw orderWithdraw : orderWithdrawList) {
                orderWithdrawService.queryWithdrawOrderTask(orderWithdraw);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("结束执行1325订单查询，耗时{}ms", (System.currentTimeMillis() - start));
    }

    /**
     * 银行--->交易网(请求--1312接口)
     *
     * @param message
     */
    @PostMapping("/withdrawForBank")
    public void withdrawForBank(@RequestBody String message) {
        orderWithdrawService.withdrawForBank(message);
    }

    /**
     * 1010接口
     * 查银行端会员资金台帐余额
     *
     * @param queryBlanceDto
     * @return
     */
    @PostMapping("/queryMemberBlance")
    public ApiResult queryMemberBlance(@Validated @RequestBody QueryBlanceDto queryBlanceDto) {
        ApiResult apiResult = new ApiResult();
        orderWithdrawService.clear();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            apiResult = orderWithdrawService.queryMemberBlance(queryBlanceDto.getSelectFlag(), String.valueOf(i));
            if (-901 == apiResult.getCode()) {
                apiResult.setCode(200);
                apiResult.setMsg("查询成功!");
                break;
            }
        }
        return apiResult;
    }


    /**
     * 定时任务，把出金表中状态为2提交到渠道
     */
    @GetMapping("/findStatusTask")
    public void findStatusTask() {
        long start = System.currentTimeMillis();
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService.selectByStatus();
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行1318发起出金，此次处理订单数量为{}", size);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (int i = 0; i < orderWithdrawList.size(); i++) {
                OrderWithdraw orderWithdraw = orderWithdrawList.get(i);
                orderWithdrawService.findStatusTask(orderWithdraw);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("没有出金状态为2的订单");
        }
        log.info("结束执行1318发起出金，耗时{}ms", (System.currentTimeMillis() - start));
    }

}

