package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.product.service.IProductImgService;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DealOrderDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.DealOrderVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.IndexProductVo;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.POST;

/**
 * @author: hyc
 * @date: 2019/6/6 13:42
 * @description:
 */
@RestController
@RequestMapping("/auth/api/dealOrder")
public class AuthApiDealOrderControllor {
    @Autowired
    private IDealOrderService dealOrderService;

    @PostMapping("/findByCustomerNo")
    public ApiResult<MyPageInfo> findByCustomerNo(@RequestBody @Validated DealOrderDto dealOrderDto){
        ApiResult<MyPageInfo> apiResult;
        MyPageInfo<DealOrderVo> dealOrderVoMyPageInfo = dealOrderService.findByCustomerNo(dealOrderDto);
        apiResult = ApiResult.success(dealOrderVoMyPageInfo);
        return apiResult;
    }
}
