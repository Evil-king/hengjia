package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.model.TradeAddress;
import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.api.modules.trade.service.ITradeAddressService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname AuthApiTradeAddressController
 * @Description 提货地址管理
 * @Date 2019/6/3 15:37
 * @Created by Longer
 */
@RestController
@RequestMapping("/auth/api/address")
public class AuthApiTradeAddressController {

    @Autowired
    ITradeAddressService addressService;

    @PostMapping("/addresses")
    public ApiResult<TradeBanner> deliveryApply(TradeAddress address){
        ApiResult<TradeBanner> apiResult;
        try {
            addressService.saveOrUpdate(address);
            apiResult = ApiResult.success();
        } catch (Exception e) {
            apiResult = ApiResult.error();
            e.printStackTrace();
        }
        return apiResult;
    }
}
