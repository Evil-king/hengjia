package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/6/14 10:00
 * @description:
 */
@Data
public class SigningDataDto  extends  CustomerNoDto{
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @NotBlank(message = "证件号不能为空")
    private String certificateNo;
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    //联系人姓名，企业用户才有
    private String contactName;
    //身份 个人：personal 企业：enterprise
    @NotBlank(message = "身份不能为空")
    private String identity;

    @NotBlank(message = "银行卡号不能为空")
    private String bankCardNo;
    @NotBlank(message = "密码不能为空")
    private String password;
}
