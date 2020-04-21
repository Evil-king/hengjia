package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerBatchDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryBannerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisory/banner")
public class AdvisoryBannerController {
    @Autowired
    private IAdvisoryBannerService advisoryBannerService;

    @RequestMapping("/addAndEdit")
    @PreAuthorize("hasAnyRole('ADMIN','ADD','EDIT')")
    public ApiResult addObject(@RequestBody AdvisoryBannerDto advisoryBannerDto){
        return advisoryBannerService.addObject(advisoryBannerDto);
    }

    @GetMapping("/lookObj")
    @PreAuthorize("hasAnyRole('ADMIN','LOOK')")
    public ApiResult editObj(@RequestParam String id){
       return  ApiResult.success(advisoryBannerService.lookObj(Long.parseLong(id)));
    }

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','BANNER_MAGAGEMENT')")
    public ApiResult ObjList(@RequestBody AdvisoryBannerListDto advisoryBannerListDto){
        MyPageInfo<AdvisoryBannerVo> pageInfo = advisoryBannerService.ObjList(advisoryBannerListDto);
        return ApiResult.success(pageInfo);
    }

    @RequestMapping("/batchOperator")
    @PreAuthorize("hasAnyRole('ADMIN','BATCH','DELETE')")
    public ApiResult batchOperator(@RequestBody AdvisoryBannerBatchDto advisoryBannerBatchDto){
        ApiResult apiResult = null;
        for(int i = 0;i<advisoryBannerBatchDto.getIdList().size();i++){
            apiResult = advisoryBannerService.batchOperator(advisoryBannerBatchDto.getIdList().get(i),
                    advisoryBannerBatchDto.getType());
        }
        return apiResult;
    }
}
