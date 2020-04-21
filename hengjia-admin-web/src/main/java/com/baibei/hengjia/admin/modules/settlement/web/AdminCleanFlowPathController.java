package com.baibei.hengjia.admin.modules.settlement.web;

import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanFlowPathVo;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/8 3:48 PM
 * @description:
 */
@RestController
@RequestMapping("/admin/cleanFlowPath")
public class AdminCleanFlowPathController {
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;


    /**
     * 列表
     *
     * @param batchNo
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','CLEAN_ALL','CLEAN_LIST')")
    public ApiResult<List<CleanFlowPathVo>> list(@RequestParam(name = "batchNo", required = false) String batchNo) {
        return ApiResult.success(cleanFlowPathService.list(batchNo));
    }

    /**
     * 清算操作
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLEAN_ALL','CLEAN')")
    public ApiResult clean(@PathVariable String id) {

        return cleanFlowPathService.clean(Long.valueOf(id));
    }
}
