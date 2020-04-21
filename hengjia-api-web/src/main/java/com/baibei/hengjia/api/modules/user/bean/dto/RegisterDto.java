package com.baibei.hengjia.api.modules.user.bean.dto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: hyc
 * @date: 2019/5/27 14:36
 * @description:
 */
@Data
public class RegisterDto implements Serializable {
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    //验证码
    @NotBlank(message = "验证码不能为空")
    private String mobileVerificationCode;
    //邀请码
    @NotBlank(message = "邀请码不能为空")
    private String invitationCode;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
    //重复密码
    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private String username;
}
