package com.baibei.hengjia.api.modules.settlement.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/26 2:57 PM
 * @description:
 */
@Data
public class AccountInfo {
    private String customerNo;
    private BigDecimal balance;
    private BigDecimal freezingAmount;
}
