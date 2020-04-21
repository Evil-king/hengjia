package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/06/06
 */
public interface IValidateService {

    /**
     * 校验参数
     * @param orderWithdrawDto
     * @return
     */
     ApiResult validate(OrderWithdrawDto orderWithdrawDto);

    /**
     * 创建订单
     * @param orderWithdrawDto
     * @return
     */
    OrderWithdraw createOrder(OrderWithdrawDto orderWithdrawDto);

    boolean compairAmount(String customerNo, BigDecimal money);
}
