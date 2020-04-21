package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.settlement.bean.dto.DealDiffDto;
import com.baibei.hengjia.api.modules.settlement.bean.dto.DiffDto;
import com.baibei.hengjia.api.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.api.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ApiWithDrawDepositController
 * @Description 出入金流水对账，调账接口
 * @Date 2019/7/4 19:24
 * @Created by Longer
 */
@RestController
@RequestMapping("/api/diff")
@Slf4j
public class ApiWithDrawDepositController {

    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;

    /**
     * 出入金流水对账
     * @return
     */
    @PostMapping("/diffList")
    public ApiResult diffList(@RequestBody @Validated DiffDto diffDto) {
        withDrawDepositDiffService.withDrawDepositDiff(diffDto.getBatchNo());
        return ApiResult.success();
    }

    /**
     * 出入金流水调账
     * @return
     */
    @PostMapping("/dealDiff")
    public ApiResult dealDiff(@RequestBody @Validated DealDiffDto dealDiffDto) {
        withDrawDepositDiffService.dealDiff(dealDiffDto);
        return ApiResult.success();
    }
}
