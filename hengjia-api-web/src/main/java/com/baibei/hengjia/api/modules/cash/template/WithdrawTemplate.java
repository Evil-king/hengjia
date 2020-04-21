package com.baibei.hengjia.api.modules.cash.template;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.cash.service.IValidateService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hwq
 * @date 2019/06/06
 */
@Component
public abstract class WithdrawTemplate {

    @Autowired
    private IValidateService validateService;


    public ApiResult doProcess(OrderWithdrawDto orderWithdrawDto) {
        ApiResult order = doBeforeProcess(orderWithdrawDto);
        return order;
    }

    /**
     * 前置处理
     * <p>
     * 处理验证、创建订单
     * </p>
     *
     * @param orderWithdrawDto
     */
    protected ApiResult doBeforeProcess(OrderWithdrawDto orderWithdrawDto) {
        ApiResult result = validateService.validate(orderWithdrawDto);
        if (result.hasSuccess()) {
            OrderWithdraw order = validateService.createOrder(orderWithdrawDto);
            result.setData(order.getOrderNo());
            return result;
        }
        return result;
    }
}
