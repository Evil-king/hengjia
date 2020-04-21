package com.baibei.hengjia.admin.modules.admin.web;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDTO;
import com.baibei.hengjia.admin.modules.admin.model.Dict;
import com.baibei.hengjia.admin.modules.admin.service.IDictService;
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
public class DictController {

    @Autowired
    private IDictService dictService;


    private static final String ENTITY_NAME = "dict";

    @GetMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_SELECT')")
    public ApiResult<MyPageInfo<DictDTO>> getDicts(DictDTO resources) {
        return ApiResult.success(dictService.queryAll(resources));
    }

    @PutMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ApiResult update(@RequestBody Dict resources) {
        dictService.updateDict(resources);
        return ApiResult.success();
    }

    @PostMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ApiResult create(@Validated @RequestBody Dict resources) {
        if (resources.getId() != null) {
            return ApiResult.error("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        dictService.createDict(resources);
        return ApiResult.success();
    }


    @DeleteMapping(value = "/dict/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ApiResult delete(@PathVariable Long id) {
        dictService.deleteDict(id);
        return ApiResult.success();
    }
}