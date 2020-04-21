package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/7/4 9:25
 * @description:
 */
@Data
public class InsertPasswordDto extends  CustomerNoDto{
    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;

}
