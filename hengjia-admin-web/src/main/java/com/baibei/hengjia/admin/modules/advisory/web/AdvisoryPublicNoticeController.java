package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticeDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticePageDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.admin.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.admin.modules.advisory.service.IPublicNoticeService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * 咨询公告管理列表
 */
@RestController
@RequestMapping("/admin/public/notice")
public class AdvisoryPublicNoticeController {

    @Autowired
    private IPublicNoticeService publicNoticeService;

    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<AdvisoryPublicNoticeVo>> pageList(@Validated @RequestBody AdvisoryPublicNoticePageDto advisoryPublicNoticePageDto) {
        return ApiResult.success(publicNoticeService.pageList(advisoryPublicNoticePageDto));
    }

    @PostMapping(path = "/getById")
    public ApiResult<AdvisoryPublicNoticeVo> getById(@RequestBody AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        PublicNotice publicNotice = this.publicNoticeService.findById(publicNoticeDto.getId());
        AdvisoryPublicNoticeVo result = BeanUtil.copyProperties(publicNotice, AdvisoryPublicNoticeVo.class);
        return ApiResult.success(result);
    }

    @PostMapping(path = "/save")
    public ApiResult save(@Valid @RequestBody AdvisoryPublicNoticeDto publicNoticeDto) {
        PublicNotice publicNotice = BeanUtil.copyProperties(publicNoticeDto, PublicNotice.class);
        if (publicNoticeDto.getPublicType() == null) {
            publicNotice.setPublicType(Constants.PublicNoticeType.MAIL);
        }
        publicNotice.setFlag(new Byte(Constants.Flag.VALID));
        this.publicNoticeService.save(publicNotice);
        return ApiResult.success();
    }

    @PostMapping(path = "/update")
    public ApiResult update(@Valid @RequestBody AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        this.publicNoticeService.updatePublicNotice(publicNoticeDto);
        return ApiResult.success();
    }

    @PostMapping(path = "/deleteById")
    public ApiResult deleteById(@RequestBody AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getId() == null) {
            return ApiResult.error("Id不能为空");
        }
        publicNoticeService.softDeleteById(publicNoticeDto.getId());
        return ApiResult.success();
    }

    @PostMapping(path = "/batchOperate")
    public ApiResult batchOperate(@RequestBody AdvisoryPublicNoticeDto adminPublicNoticeDto) {
        if (StringUtils.isEmpty(adminPublicNoticeDto.getBatchType())) {
            return ApiResult.error("批量类型不能为空");
        }
        ApiResult apiResult;
        switch (adminPublicNoticeDto.getBatchType()) {
            case "delete":
                publicNoticeService.batchDelete(adminPublicNoticeDto);
                apiResult = ApiResult.success();
                break;
            case "update":
                publicNoticeService.batchHidden(adminPublicNoticeDto);
                apiResult = ApiResult.success();
                break;
            default:
                apiResult = ApiResult.error("操作类型不支持");
                break;
        }
        return apiResult;
    }


}
