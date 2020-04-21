package com.baibei.hengjia.api.modules.advisory.web;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryArticleDetailsDto;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
public class AdvisoryArticleController {

    @Autowired
    private IAdvisoryArticleService advisoryArticleService;

    @RequestMapping("/details")
    public ApiResult index(@RequestBody AdvisoryArticleDetailsDto advisoryArticleDetailsDto){
        return ApiResult.success(advisoryArticleService.details(advisoryArticleDetailsDto.getTypeId()));
    }

}
