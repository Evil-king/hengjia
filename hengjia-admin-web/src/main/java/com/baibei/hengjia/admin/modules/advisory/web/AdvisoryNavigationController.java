package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.*;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationDetailVo;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisory/navigation")
public class AdvisoryNavigationController {
    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;
    @Autowired
    private IAdvisoryNavigationDetailsService advisoryNavigationDetailsService;

    @RequestMapping("/addAndEdit")
    @PreAuthorize("hasAnyRole('ADMIN','ADD','EDIT')")
    public ApiResult addObj(@RequestBody AdvisoryNavigationDto advisoryNavigationDto) {
        return advisoryNavigationService.addObj(advisoryNavigationDto);
    }

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','NAV_MANAGEMENT')")
    public ApiResult pageList(@RequestBody AdvisoryNavigationListDto advisoryNavigationListDto) {
            MyPageInfo<AdvisoryNavigationVo> advisoryNavigationVoMyPageInfo = advisoryNavigationService
                    .objList(advisoryNavigationListDto);
           return ApiResult.success(advisoryNavigationVoMyPageInfo);
    }

    @RequestMapping("/batchDelete")
    @PreAuthorize("hasAnyRole('ADMIN','DELETE','BATCH')")
    public ApiResult batchDelete(@RequestBody AdvisoryNavigationDetailsDeleteDto advisoryNavigationDetailsDeleteDto) {
        ApiResult apiResult = null;
        for (int i = 0; i < advisoryNavigationDetailsDeleteDto.getIdList().size(); i++) {
            apiResult = advisoryNavigationService.batchDelete(advisoryNavigationDetailsDeleteDto.getIdList().get(i));
        }
        return apiResult;
    }

    @RequestMapping("/pageListDetails")
    @PreAuthorize("hasAnyRole('ADMIN','NAV_CONTENT_MANAGEMENT')")
    public ApiResult pageListDetails(@RequestBody AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto) {
        if(StringUtils.isEmpty(advisoryNavigationDetailsDto.getNavigationId())){
            MyPageInfo<AdvisoryNavigationDetailVo> advisoryNavigationVoMyPageInfo = advisoryNavigationDetailsService
                    .objList(advisoryNavigationDetailsDto);
           return ApiResult.success(advisoryNavigationVoMyPageInfo);
        } else {
            MyPageInfo<AdvisoryNavigationDetailVo> advisoryNavigationVoMyPageInfo = advisoryNavigationDetailsService
                    .objListByNavigationId(advisoryNavigationDetailsDto);
           return ApiResult.success(advisoryNavigationVoMyPageInfo);
        }
    }

    @RequestMapping("/editSort")
    @PreAuthorize("hasAnyRole('ADMIN','SORT')")
    public ApiResult editSort(@RequestBody EditSortDto editSortDto){
        if(advisoryNavigationDetailsService.editSort(editSortDto) > 0){
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    @RequestMapping("/deleteDetails")
    @PreAuthorize("hasAnyRole('ADMIN','BATCH','DELETE')")
    public ApiResult deleteDetails(@RequestBody AdvisoryNavigationDetailsDeleteDto advisoryNavigationDetailsDeleteDto){
        for(int i = 0; i < advisoryNavigationDetailsDeleteDto.getIdList().size(); i++){
            if(advisoryNavigationDetailsService.deleteDetails(advisoryNavigationDetailsDeleteDto.getIdList().get(i)) > 0){
                return ApiResult.success();
            }
        }
        return ApiResult.error();
    }

    @GetMapping("/navigationList")
    public ApiResult navigationList(){
        return ApiResult.success(advisoryNavigationService.navigationList());
    }

}
