package com.baibei.hengjia.api.modules.advisory.web;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/navigation")
public class AdvisoryNavigationController {
    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;
    @Autowired
    private IAdvisoryNavigationDetailsService advisoryNavigationDetailsService;

    @RequestMapping("/navigationList")
    public ApiResult navigationList(){
        return ApiResult.success(advisoryNavigationService.navigationList());
    }

    @RequestMapping("/navigationDetails")
    public ApiResult navigationDetails(@RequestBody AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto){
        return ApiResult.success(advisoryNavigationDetailsService.navigationDetails(advisoryNavigationDetailsDto));
    }
}
