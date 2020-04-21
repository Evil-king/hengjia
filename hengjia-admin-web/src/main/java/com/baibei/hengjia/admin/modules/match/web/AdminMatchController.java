package com.baibei.hengjia.admin.modules.match.web;

import com.baibei.hengjia.admin.modules.match.bean.dto.MatchLogDto;
import com.baibei.hengjia.admin.modules.match.bean.vo.MatchLogVo;
import com.baibei.hengjia.admin.modules.match.service.IMatchLogService;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Longer
 * @date: 2019/7/17 16:08
 * @description:配票
 */
@RestController
@RequestMapping("/admin/match")
public class AdminMatchController {
    @Autowired
    private IMatchLogService matchLogService;


    /**
     * 分页列表
     * @param matchLogDto
     * @return
     */
    @RequestMapping("/pageList")
    public ApiResult<MyPageInfo<MatchLogVo>> pageList(MatchLogDto matchLogDto) {
        return ApiResult.success(matchLogService.pageList(matchLogDto));
    }
}
