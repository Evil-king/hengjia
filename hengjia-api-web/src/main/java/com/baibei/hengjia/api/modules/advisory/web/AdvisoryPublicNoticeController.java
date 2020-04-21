package com.baibei.hengjia.api.modules.advisory.web;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryPublicNoticeDto;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.api.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.api.modules.advisory.service.IPublicNoticeService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 咨询公告api管理列表
 */
@RestController
@RequestMapping("/api/public/notice")
public class AdvisoryPublicNoticeController {

    @Autowired
    private IPublicNoticeService publicNoticeService;

    /**
     * 公告管理
     *
     * @param PageParam
     * @return
     */
    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<AdvisoryPublicNoticeVo>> pageList(@Validated @RequestBody PageParam PageParam) {
        return ApiResult.success(publicNoticeService.apiPageList(PageParam));
    }

    /**
     * 公告管理获取Id
     *
     * @param publicNoticeDto
     * @return
     */
    @PostMapping(path = "/getById")
    public ApiResult<AdvisoryPublicNoticeVo> getById(@RequestBody AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        PublicNotice publicNotice = this.publicNoticeService.findById(publicNoticeDto.getId());
        AdvisoryPublicNoticeVo result = BeanUtil.copyProperties(publicNotice, AdvisoryPublicNoticeVo.class);
        return ApiResult.success(result);
    }

    /**
     * 获取最新的公告
     * @return
     */
    @PostMapping(path = "getLastByPublicNotice")
    public ApiResult<List<AdvisoryPublicNoticeVo>> getLastByPublicNotice() {
        return ApiResult.success(publicNoticeService.findByPublicNotice());
    }

}
