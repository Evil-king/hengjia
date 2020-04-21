package com.baibei.hengjia.admin.modules.customer.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/9/5 16:47
 * @description:
 */
@Data
public class CouponAccountVo {
    private String productName;
    private BigDecimal balance;
}
