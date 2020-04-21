package com.baibei.hengjia.api.modules.cash.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberBalanceVo {

    private String custAcctid;//子账户

    private String custFlag;//虚拟账号

    private String custType;//子账户属性

    private String custStatus;//子账户状态

    private String customerNo;//交易网会员代码

    private String mainAcctid;//上级监管账号

    private String custName;//会员名称

    private BigDecimal TotalAmount;//账户总余额

    private BigDecimal TotalBalance;//账户可用余额

    private BigDecimal TotalFreezeAmount;//账户总冻结金额

    private String TranDate;//维护日期
}
