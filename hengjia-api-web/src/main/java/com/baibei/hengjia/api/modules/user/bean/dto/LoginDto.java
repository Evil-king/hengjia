package com.baibei.hengjia.api.modules.user.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: hyc
 * @date: 2019/5/27 14:26
 * @description:
 */

@Data
public class LoginDto implements Serializable {
    //交易账号（允许为手机号或者用户名）
    @NotBlank(message = "交易账号不能为空")
    private String username;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;

}

