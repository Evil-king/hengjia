package com.baibei.hengjia.api.modules.advisory.web;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryVideoDetailsDto;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/video")
public class AdvisoryVideoController {

    @Autowired
    private IAdvisoryVideoService advisoryVideoService;

    @RequestMapping("/details")
    public ApiResult details(@RequestBody AdvisoryVideoDetailsDto advisoryVideoDetailsDto){
       return ApiResult.success( advisoryVideoService.details(advisoryVideoDetailsDto.getTypeId()));
    }
}
