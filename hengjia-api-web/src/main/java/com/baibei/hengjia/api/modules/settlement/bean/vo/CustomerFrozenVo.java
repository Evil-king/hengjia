package com.baibei.hengjia.api.modules.settlement.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/1 11:05 AM
 * @description:
 */
@Data
public class CustomerFrozenVo {
    private String customerNo;
    private BigDecimal frozenTotal;
    private BigDecimal unfrozenTotal;
}
