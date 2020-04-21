package com.baibei.hengjia.api.modules.user.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/5/28 20:21
 * @description:
 */
@Data
public class ForgetPasswordDto {
    private String customerNo;
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    //验证码
    @NotBlank(message = "验证码不能为空")
    private String mobileVerificationCode;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
    //重复密码
    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;
}
