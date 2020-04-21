package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleBatchDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisory/article")
public class AdvisoryArticleController {

    @Autowired
    private IAdvisoryArticleService advisoryArticleService;
    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;

    @RequestMapping("/addAndEdit")
    @PreAuthorize("hasAnyRole('ADMIN','ADD','EDIT')")
    public ApiResult addObj(@RequestBody AdvisoryArticleDto advisoryArticleDto) {
        //如果是添加的话 先查询导航是否存在
        String str[] = advisoryArticleDto.getNavigationId().split(",");
        for (int i = 0; i < str.length; i++) {
            if (advisoryNavigationService.queryByNavigationId(str[i]) < 1) {
                return ApiResult.error("导航不存在");
            }
        }
        return advisoryArticleService.addAndEdit(advisoryArticleDto);
    }

    @GetMapping("/lookObj")
    @PreAuthorize("hasAnyRole('ADMIN','LOOK')")
    public ApiResult lookObj(@RequestParam long id) {
        return ApiResult.success(advisoryArticleService.lookObj(id));
    }

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','ARTICLE_MANAGEMENT')")
    public ApiResult ObjList(@RequestBody AdvisoryArticleListDto advisoryArticleListDto) {
        MyPageInfo<AdvisoryArticleVo> pageInfo = advisoryArticleService.ObjList(advisoryArticleListDto);
        return ApiResult.success(pageInfo);
    }

    @RequestMapping("/batchOperator")
    @PreAuthorize("hasAnyRole('ADMIN','DELETE','BATCH')")
    public ApiResult batchOperator(@RequestBody AdvisoryArticleBatchDto advisoryArticleBatchDto) {
        ApiResult apiResult = null;
        for (int i = 0; i < advisoryArticleBatchDto.getIdList().size(); i++) {
            apiResult = advisoryArticleService.batchOperator(advisoryArticleBatchDto.getIdList().get(i),
                    advisoryArticleBatchDto.getType());
        }
        return apiResult;
    }
}
