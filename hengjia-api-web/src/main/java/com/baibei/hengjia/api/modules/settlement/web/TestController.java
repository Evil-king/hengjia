package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.match.service.IBuymatchUserService;
import com.baibei.hengjia.api.modules.settlement.bean.dto.DealDiffDto;
import com.baibei.hengjia.api.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.api.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;

    @Autowired
    private IBankOrderService bankOrderService;
    @Autowired
    private IBuymatchUserService buymatchUserService;
    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Autowired
    private IMatchConfigService matchConfigService;



    @PostMapping("/bankOder")
    public ApiResult deliveryMatch() {
        bankOrderService.bankOrder(null);
        return ApiResult.success();
    }

    /**
     * 出入金流水对账
     * @return
     */
    @GetMapping("/wdDiff")
    public ApiResult deliveryMatch(String batchNo) {
        withDrawDepositDiffService.withDrawDepositDiff(batchNo);
        return ApiResult.success();
    }

    /**
     * 出入金流水调账
     * @return
     */
    @PostMapping("/dealDiff")
    public ApiResult deliveryMatch(@RequestBody DealDiffDto dealDiffDto) {
        withDrawDepositDiffService.dealDiff(dealDiffDto);
        return ApiResult.success();
    }

    @GetMapping("/redis")
    public ApiResult redis() {
        redisUtil.leftPush("mylist", "20190702");
        redisUtil.pub(RedisConstant.SET_CLEAN_PRE_TOPIC, RedisConstant.SET_CLEAN_PRE_TOPIC);
        return ApiResult.success();
    }

    @GetMapping("/get")
    public ApiResult get() {
        Object obj = redisUtil.rightPop("mylist");
        if (obj == null) {
            return ApiResult.error();
        }
        return ApiResult.success((String) obj);
    }

    @PostMapping("/buymatch")
    public ApiResult buymatch() {
        buymatchUserService.buyMatchUsers();
        return ApiResult.success();
    }

    @PostMapping("/buymatch2")
    public ApiResult buymatch2(String batchNo) {
        /*buymatchLogService.buyMatch(batchNo);*/
        return ApiResult.success();
    }

    @PostMapping("/sw")
    public ApiResult sw() {
        matchConfigService.test();
        return ApiResult.success();
    }
}
