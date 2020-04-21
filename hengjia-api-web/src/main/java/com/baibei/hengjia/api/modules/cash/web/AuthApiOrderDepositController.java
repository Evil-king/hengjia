package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.service.IOrderDepositService;
import com.baibei.hengjia.api.modules.cash.bean.vo.OrderDepositVo;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 入金服务
 */
@RestController
@RequestMapping("/auth/api/orderDeposit")
public class AuthApiOrderDepositController {

    private final ICashService cashService;

    public AuthApiOrderDepositController(ICashService cashService) {
        this.cashService = cashService;
    }


    @PostMapping("/deposit")
    public ApiResult<OrderDepositVo> depositApplication(@Valid @RequestBody OrderDepositDto orderDepositDto) throws Exception {
        return cashService.depositDto(orderDepositDto);
    }
}
