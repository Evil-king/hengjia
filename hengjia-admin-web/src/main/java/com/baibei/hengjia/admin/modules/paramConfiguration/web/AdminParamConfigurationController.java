package com.baibei.hengjia.admin.modules.paramConfiguration.web;

import com.baibei.hengjia.admin.modules.paramConfiguration.service.ILookUpService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/paramConfiguration")
public class AdminParamConfigurationController {

    @Autowired
    private ILookUpService lookUpService;

    @GetMapping("/addLogistics")
    @PreAuthorize("hasAnyRole('ADMIN','PARAM_LOGISTICS','PARAMCONFIGURATION')")
    public ApiResult addLogistics(@RequestParam("name") String name){
        if(lookUpService.selectByName(name) > 0){
            return ApiResult.error("该物流已经存在");
        }
        if(lookUpService.addLogistics(name) < 1){
            return ApiResult.error();
        }
        return ApiResult.success();
    }

    @GetMapping("/delLogistics")
    @PreAuthorize("hasAnyRole('ADMIN','PARAM_LOGISTICS','PARAMCONFIGURATION')")
    public ApiResult delLogistics(@RequestParam("id") String id){
        if(lookUpService.deleteToId(Long.valueOf(id)) < 1){
            return ApiResult.error();
        }
        return ApiResult.success();
    }
}
