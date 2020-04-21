package com.baibei.hengjia.api.modules.settlement.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/26 4:10 PM
 * @description:
 */
@Data
public class CustomerCountVo {
    private String customerNo;
    private BigDecimal total;
}
