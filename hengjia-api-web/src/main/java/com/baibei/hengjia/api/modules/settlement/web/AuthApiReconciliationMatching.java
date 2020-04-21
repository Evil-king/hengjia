package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.ReconciliationMatchingDto;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易网触发银行开销户进行清算和对账
 */
@RestController
@RequestMapping("/auth/api/reconciliationMatching")
public class AuthApiReconciliationMatching {

    @Autowired
    private ICashService cashService;

    /**
     * 触发会员开销户文件对账
     *
     * @param reconciliationMatchingDto
     * @return
     */
    @PostMapping("/openingAndCancellationAccounts")
    public ApiResult openingAndCancellationAccounts(@RequestBody ReconciliationMatchingDto reconciliationMatchingDto) {
        reconciliationMatchingDto.setFuncFlag(Constants.PAB1006.MEMBER);
        return cashService.reconciliationMatching(reconciliationMatchingDto);
    }
}
