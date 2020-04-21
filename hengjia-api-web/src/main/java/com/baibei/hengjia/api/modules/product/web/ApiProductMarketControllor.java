package com.baibei.hengjia.api.modules.product.web;

import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/6/5 18:45
 * @description:
 */
@RestController
@RequestMapping("/api/productMarket")
public class ApiProductMarketControllor {
    @Autowired
    private IProductMarketService productMarketService;
    @RequestMapping("/getAllProductTradeNo")
    public ApiResult<List<String>> getAllProductTradeNo(){
        List<String> statusList=new ArrayList();
        statusList.add("trading");//正在交易
        statusList.add("onmarket");//已上市
        return ApiResult.success(productMarketService.getAllProductTradeNo(statusList));
    }
}
