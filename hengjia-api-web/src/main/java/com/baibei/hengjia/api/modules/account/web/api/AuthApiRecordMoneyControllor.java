package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.service.IRecordMoneyService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author: hyc
 * @date: 2019/6/5 15:59
 * @description:
 */
@Controller
@RequestMapping("/auth/api/recordMoney")
public class AuthApiRecordMoneyControllor {
    @Autowired
    private IRecordMoneyService recordMoneyService;

    @PostMapping("/getAll")
    @ResponseBody
    public ApiResult<MyPageInfo<RecordVo>> getAll(@RequestBody @Validated RecordDto recordDto){
        return ApiResult.success(recordMoneyService.getAll(recordDto));
    }
}
