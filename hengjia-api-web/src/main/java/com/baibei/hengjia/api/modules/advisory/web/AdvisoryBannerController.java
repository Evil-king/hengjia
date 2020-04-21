package com.baibei.hengjia.api.modules.advisory.web;

import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryBannerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banner")
public class AdvisoryBannerController {
    @Autowired
    private IAdvisoryBannerService advisoryBannerService;

    @RequestMapping("/index")
    public ApiResult index(){
        return ApiResult.success(advisoryBannerService.index());
    }
}
