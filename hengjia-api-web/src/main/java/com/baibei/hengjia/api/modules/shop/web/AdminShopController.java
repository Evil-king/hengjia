package com.baibei.hengjia.api.modules.shop.web;

import com.baibei.hengjia.api.modules.shop.service.IShpOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwq
 * @date 2019/06/04
 */
@Slf4j
@RestController
@RequestMapping("/api/shop")
public class AdminShopController {
    @Autowired
    private IShpOrderService shpOrderService;

    @GetMapping("/longDayConfirmSend")
    public ApiResult longDayConfirmSend() {
        if(shpOrderService.longDayConfirmSend() != 1){
            log.info("没有查询结果");
        }
        return ApiResult.success();
    }
}
