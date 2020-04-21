package com.baibei.hengjia.api.modules.cash.bean.vo;

import com.baibei.hengjia.api.modules.cash.base.BaseResponse;
import lombok.Data;

@Data
public class SignInBackVo extends BaseResponse {

    /**
     * 前置流水号
     */
    private String frontLogNo;

    /**
     * 保留域
     */
    private String reserve;
}
