package com.baibei.hengjia.api.modules.cash.bean.dto;


import com.baibei.hengjia.api.modules.cash.base.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 签到或者签退
 */
@Data
public class SignInBackDto extends BaseRequest {

    /**
     * 请求功能标识 1、签到 2、签退
     */
    @NotNull
    @Size(min = 1, max = 1)
    @Pattern(regexp = "1|2")
    private String funcFlag;

    /**
     * 保留域
     */
    @Size(max = 120)
    private String Reserve;

}
