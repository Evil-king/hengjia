package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.IndexProductListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.IndexProductVo;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AuthApiTradeDeliveryController
 * @Description TODO
 * @Date 2019/6/3 15:37
 * @Created by Longer
 */
@RestController
@RequestMapping("/api/tradeIndex")
public class ApiTradeIndexController {
    @Autowired
    private IProductMarketService productMarketService;

    @PostMapping("/productList")
    public ApiResult<MyPageInfo> getProductList(@RequestBody @Validated IndexProductListDto productListDto){
       /* List<String> statusList=new ArrayList();
        statusList.add("trading");//正在交易
        statusList.add("onmarket");//已上市
        productListDto.setTradeStatusList(statusList);*/
        ApiResult<MyPageInfo> apiResult;
        MyPageInfo<IndexProductVo> indexProductVoMyPageInfo = productMarketService.productList(productListDto);
        apiResult = ApiResult.success(indexProductVoMyPageInfo);
        return apiResult;
    }
}
