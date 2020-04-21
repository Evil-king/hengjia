package com.baibei.hengjia.api.modules.cash.bean.dto;

import com.baibei.hengjia.api.modules.cash.base.BaseRequest;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ToString(callSuper = true)
public class SigningRecordDto extends BaseRequest {


    @NotNull(message = "功能标识不能为空")
    @Pattern(regexp = "1|2|3", message = "功能标识不正确")
    private String funcFlag;



    @NotNull(message = "会员名称不能为空")
    @Size(max = 120, message = "会员名称长度错误")
    private String custName;

    /**
     * 会员代码
     */
    @NotNull(message = "会员代码不能为空")
    @Size(max = 32, message = "会员代码长度错误")
    private String custAcctId;


    /**
     * 注册类型
     */
    @NotNull(message = "会员证件类型不能为空")
    @Size(max = 2, message = "会员证件类型长度错误")
    private String idType;

    /**
     * 会员证件号码
     */
    @NotNull(message = "会员证件号码不能为空")
    @Size(max = 20, message = "会员证件号码长度错误")
    private String idCode;

    /**
     * 出入金账号
     */
    @NotNull(message = "出入金账号不能为空")
    @Size(max = 32, message = "出入金账号长度错误")
    private String relatedAcctId;


    //1、出金账号 2、入金账号 3、出金账号&入金账号
    @NotNull(message = "账号性质不能为空")
    @Pattern(regexp = "1|2|3", message = "账号性质格式不支持")
    private String acctFlag;


    @NotNull(message = "转账方式不能为空")
    @Pattern(regexp = "1|2|3", message = "转账方式格式错误")
    private String tranType;


    @NotNull(message = "账号名称不能为空")
    @Size(max = 120, message = "账号名称长度错误")
    private String acctName;


    @Size(max = 12, message = "联行号不能长度错误")
    private String bankCode;


    @Size(max = 120, message = "开户行名称错误")
    private String bankName;


    @Size(max = 32, message = "原入金账号长度错误")
    private String oldRelatedAcctId;


    @Size(max = 120, message = "保留域长度错误")
    private String reserve;


}
