package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/6/5 9:38
 * @description:
 */
@Data
public class RecordVo {
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 变动数额
     */
    private String changeAmount;
    /**
     * 当前余额
     */
    private BigDecimal balance;
}
