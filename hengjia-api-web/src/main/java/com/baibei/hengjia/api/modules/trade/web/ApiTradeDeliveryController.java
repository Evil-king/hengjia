package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
public class ApiTradeDeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    /**
     * 自动收货
     *
     * @return
     */
    @GetMapping(path = "/autoReceipt")
    public ApiResult autoReceipt() {
        this.deliveryService.autoReceipt();
        return ApiResult.success();
    }


}
