package com.baibei.hengjia.api.modules.user.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/6/5 10:54
 * @description:
 */
@Data
public class AuthenticationDto extends CustomerNoDto {
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String realname;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idcard;

}
