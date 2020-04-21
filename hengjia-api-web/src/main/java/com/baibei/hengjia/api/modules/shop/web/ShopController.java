package com.baibei.hengjia.api.modules.shop.web;

import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.product.service.IProductService;
import com.baibei.hengjia.api.modules.shop.bean.dto.CancelPointOrderDto;
import com.baibei.hengjia.api.modules.shop.bean.dto.ConfirmReceiptDTO;
import com.baibei.hengjia.api.modules.shop.bean.dto.ExchangeListDTO;
import com.baibei.hengjia.api.modules.shop.bean.dto.ExchangePointDTO;
import com.baibei.hengjia.api.modules.shop.bean.vo.OrderListVO;
import com.baibei.hengjia.api.modules.shop.bean.vo.ShopHomeVO;
import com.baibei.hengjia.api.modules.shop.service.IShpOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author hwq
 * @date 2019/06/04
 */
@RestController
@RequestMapping("/auth/api/shop")
public class ShopController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IShpOrderService shpOrderService;

    @PostMapping("/home")
    public ApiResult homeData(@RequestBody @Validated PageParam pageParam, CustomerBaseDto customerBaseDto) {
        MyPageInfo<ShopHomeVO> pageInfo =
                productService.getHomeData(pageParam, customerBaseDto);
        return ApiResult.success(pageInfo);
    }

    @PostMapping("/userInfo")
    public ApiResult getUserInfo(@RequestBody @Validated CustomerBaseDto customerBaseDto) {
        IntegralDetailVo userInfo = shpOrderService.getUserInfo(customerBaseDto);
        return ApiResult.success(userInfo);
    }

    @PostMapping(value = "/exchange")
    public ApiResult exchangePoint(@RequestBody @Validated ExchangePointDTO exchangePointDTO) {
        shpOrderService.exchangePoint(exchangePointDTO);
        return ApiResult.success();
    }

    @PostMapping("/exchangeList")
    public ApiResult exchangeList(@RequestBody @Validated ExchangeListDTO exchangeListDTO) {
        MyPageInfo<OrderListVO> list = shpOrderService.exchangeList(exchangeListDTO);
        return ApiResult.success(list);
    }

    @PostMapping("/confirmReceipt")
    public ApiResult confirmReceipt(@RequestBody @Validated ConfirmReceiptDTO confirmReceiptDTO) {
        return shpOrderService.confirmReceipt(confirmReceiptDTO);
    }

    @PostMapping("/cancelPointOrder")
    public ApiResult cancelPointOrder(@RequestBody @Validated CancelPointOrderDto cancelPointOrderDto){
        return shpOrderService.cancelPointOrder(cancelPointOrderDto);
    }
}
