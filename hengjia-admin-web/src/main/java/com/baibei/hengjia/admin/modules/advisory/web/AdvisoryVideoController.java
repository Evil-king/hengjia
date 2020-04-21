package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoBatchDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoListDto;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisory/video")
public class AdvisoryVideoController {

    @Autowired
    private IAdvisoryVideoService advisoryVideoService;
    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','AUDIO_VIDEO_MANAGEMENT')")
    public ApiResult ObjList(@RequestBody AdvisoryVideoListDto advisoryVideoListDto){
        return ApiResult.success(advisoryVideoService.objList(advisoryVideoListDto));
    }

    @RequestMapping("/addAndEdit")
    @PreAuthorize("hasAnyRole('ADMIN','ADD','EDIT')")
    public  ApiResult addObj(@RequestBody AdvisoryVideoDto advisoryVedioDto){
        //如果是添加的话 先查询导航是否存在
        String strNavigation[] = advisoryVedioDto.getNavigationId().split(",");
        for (int i = 0; i < strNavigation.length; i++) {
            if (advisoryNavigationService.queryByNavigationId(strNavigation[i]) < 1) {
                return ApiResult.error("导航不存在");
            }
        }
        return advisoryVideoService.addAndEdit(advisoryVedioDto);
    }

    @GetMapping("/lookObj")
    @PreAuthorize("hasAnyRole('ADMIN','LOOK')")
    public ApiResult lookObj(@RequestParam long id){
        return ApiResult.success(advisoryVideoService.lookObj(id));
    }

    @RequestMapping("/batchOperator")
    @PreAuthorize("hasAnyRole('ADMIN','BATCH','DELETE')")
    public ApiResult batchOperator(@RequestBody AdvisoryVideoBatchDto advisoryVideoBatchDto) {
        ApiResult apiResult = null;
        for (int i = 0; i < advisoryVideoBatchDto.getIdList().size(); i++) {
            apiResult = advisoryVideoService.batchOperator(advisoryVideoBatchDto.getIdList().get(i),
                    advisoryVideoBatchDto.getType());
        }
        return apiResult;
    }
}
