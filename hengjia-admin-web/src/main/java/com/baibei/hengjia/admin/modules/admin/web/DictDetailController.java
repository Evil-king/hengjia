package com.baibei.hengjia.admin.modules.admin.web;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailsQueryDto;
import com.baibei.hengjia.admin.modules.admin.model.Dict_Detail;
import com.baibei.hengjia.admin.modules.admin.service.IDict_DetailService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jie
 * @date 2019-04-10
 */
@RestController
@RequestMapping("api")
public class DictDetailController {
    @Autowired
    private IDict_DetailService dictDetailService;


    private static final String ENTITY_NAME = "dictDetail";

    //@Log("查询字典详情")
    @GetMapping(value = "/dictDetail")
    public ApiResult<MyPageInfo<Dict_Detail>> getDictDetails(DictDetailsQueryDto resources) {
        return ApiResult.success(dictDetailService.listByDictName(resources));
    }


    @PostMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ApiResult create(@Validated @RequestBody DictDetailDTO resources) {
        if (resources.getId() != null) {
            return ApiResult.error("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        dictDetailService.saveDictDetail(resources);
        return ApiResult.success();
    }

    @PutMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ApiResult update(@Validated @RequestBody DictDetailDTO resources) {
        dictDetailService.updateDictDetail(resources);
        return ApiResult.success();
    }

    @DeleteMapping(value = "/dictDetail/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        dictDetailService.deleteDictDetail(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}