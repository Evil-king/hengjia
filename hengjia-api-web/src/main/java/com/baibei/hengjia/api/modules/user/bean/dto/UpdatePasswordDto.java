package com.baibei.hengjia.api.modules.user.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/5/29 15:23
 * @description:
 */
@Data
public class UpdatePasswordDto {
    @NotBlank(message = "交易商编号不能为空")
    private String customerNo;
    @NotBlank(message = "当前密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
    //重复密码
    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;
}
