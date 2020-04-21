package com.baibei.hengjia.api.modules.cash.bean.vo;

import com.baibei.hengjia.api.modules.cash.base.BaseResponse;
import lombok.Data;

@Data
public class SigningRecordVo extends BaseResponse {
    private static final long serialVersionUID = 9201379727043572980L;

    private String bankName;//收款银行

    private String acctName;//收款账户

    private String realtedAcctId;//收款账号
}
