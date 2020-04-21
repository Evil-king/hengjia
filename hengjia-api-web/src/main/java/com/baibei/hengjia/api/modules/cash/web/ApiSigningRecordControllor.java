package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: hyc
 * @date: 2019/6/27 14:40
 * @description:
 */
@RestController
@RequestMapping("/api/signingRecord")
public class ApiSigningRecordControllor {
    @Autowired
    private ISigningRecordService signingRecordService;

    @Autowired
    private List<ICashFunctionService> cashFunctionService;

    @Autowired
    private BankBackMessageAnalysis bankInterfaceBackMessage;

    /**
     * 银行--->交易网(请求--1019接口)此处仅供测试使用
     *
     * @param message
     */
    @GetMapping("/getAcountBalance")
    public void getAcountBalance(String message) {
        signingRecordService.getAcountBalance(message);
    }

    @PostMapping("/queryTotalBalance")
    public ApiResult queryTotalBalance() {
        return signingRecordService.queryTotalBalance();
    }

    @PostMapping("/signing")
    public ApiResult signingMessage(@RequestBody Map<String,String> map){
        Map<String, String> result = bankInterfaceBackMessage.parsingTranMessageString(map.get("message"));
        String tranFunc = result.get("TranFunc");
        String bodyMessages = null;
        if (tranFunc != null) {
            ICashFunctionService iCashFunctionService = cashFunctionService.stream()
                    .filter(function -> function.getType().getIndex() == Integer.valueOf(tranFunc))
                    .findFirst().orElse(null);
            bodyMessages = iCashFunctionService.response(result);
        }
        return ApiResult.success(bodyMessages);
    }
}
