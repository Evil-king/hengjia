package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.settlement.service.IAmountReturnService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/3 2:14 PM
 * @description:
 */
@RestController
@RequestMapping("/admin/amountReturn")
@Slf4j
public class AdminAmountReturnController {
    @Autowired
    private IAmountReturnService amountReturnService;

    /**
     * 业务办理
     *
     * @return
     */
    @GetMapping("/process")
    public ApiResult process(@RequestParam("batchNo") String batchNo, @RequestParam("type") String type) {
        if (StringUtils.isEmpty(batchNo)) {
            return ApiResult.badParam("batchNo不能为空");
        }
        if (StringUtils.isEmpty(type) || (!Constants.AmountReturnType.FEE.equals(type)
                && !Constants.AmountReturnType.INTEGRAL.equals(type))) {
            return ApiResult.badParam("类型错误");
        }
        return amountReturnService.process(batchNo, type);
    }

    /**
     * 业务办理
     *
     * @return
     */
    @GetMapping("/processById")
    public ApiResult processById(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return ApiResult.badParam("id不能为空");
        }
        return amountReturnService.processById(id);
    }
}
