package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.SigningRecordDto;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/api/signingRecord")
public class AuthApiSigningRecordController {


    private final ICashService cashService;

    @Value("${cash.supAcctId}")
    private String supAcctId;


    private final Logger logger = LoggerFactory.getLogger(AuthApiSigningRecordController.class);


    public AuthApiSigningRecordController(ICashService cashService) {
        this.cashService = cashService;
    }

    /**
     * 签约服务
     *
     * @param signingRecordDto
     * @return
     */
    @PostMapping("/signing")
    public ApiResult signing(@Valid @RequestBody SigningRecordDto signingRecordDto) throws Exception {
        return cashService.signingService(signingRecordDto);
    }

    /**
     * 获取客户存管账号的信息
     *
     * @return
     */
    @PostMapping("/getCustomerSuAcctId")
    public ApiResult getCustomerSuAcctId(@RequestBody SigningRecordDto signingRecordDto) {
        logger.info("当前用户的客户编号为{}", signingRecordDto.getCustomerNo());
        signingRecordDto.setSupAcctId(supAcctId);
        return ApiResult.success(signingRecordDto);
    }

}
