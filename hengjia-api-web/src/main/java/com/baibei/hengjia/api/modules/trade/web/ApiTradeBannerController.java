package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.dto.BannerDto;
import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.api.modules.trade.service.ITradeBannerService;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ApiTradeBannerController
 * @Description TODO
 * @Date 2019/6/3 15:37
 * @Created by Longer
 */
@RestController
@RequestMapping("/api/banner")
public class ApiTradeBannerController {
    @Autowired
    private ITradeBannerService bannerService;
    @Autowired
    private ICustomerService customerService;

    @PostMapping("/memberBanner")
    public ApiResult<TradeBanner> getMemberBanner(){
        ApiResult<TradeBanner> apiResult;
        try {
           /* CustomerVo customer = customerService.findUserByCustomerNo(bannerDto.getCustomerNo());
            TradeBanner memberBanner = bannerService.getMemberBanner(customer.getMemberNo());*/
            TradeBanner sysBanner = bannerService.getSysBanner();
            apiResult = ApiResult.success(sysBanner);
        } catch (Exception e) {
            apiResult = ApiResult.error();
            e.printStackTrace();
        }
        return apiResult;
    }
}
