package com.baibei.hengjia.admin.modules.customer.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/18 14:23
 * @description:
 */
@Data
public class SigningDataVo {
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 身份证号
     */
    private String idcard;
    /**
     * 银行卡号
     */
    private String bankCardNo;
}
