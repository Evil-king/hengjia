package com.baibei.hengjia.admin.modules.settlement.web;

import com.baibei.hengjia.admin.feign.ApiFeign;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.AmountReturnDto;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.AmountReturnVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.admin.modules.settlement.service.IAmountReturnService;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/8 3:48 PM
 * @description:
 */
@RestController
@RequestMapping("/admin/amountReturn")
public class AdminAmountReturnController {
    @Autowired
    private IAmountReturnService amountReturnService;
    @Autowired
    private ApiFeign apiFeign;


    /**
     * 分页列表
     *
     * @param amountReturnDto
     * @return
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','AMOUNT_RETURN_ALL','AMOUNT_RETURN_LIST')")
    public ApiResult<MyPageInfo<AmountReturnVo>> pageList(AmountReturnDto amountReturnDto) {
        return ApiResult.success(amountReturnService.pageList(amountReturnDto));
    }

    /**
     * 返还办理
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AMOUNT_RETURN_ALL','AMOUNT_RETURN')")
    public ApiResult amountReturn(@PathVariable String id) {
        return apiFeign.processById(id);
    }
}
