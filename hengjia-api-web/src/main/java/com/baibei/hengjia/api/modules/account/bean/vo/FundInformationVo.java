package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/29 17:22
 * @description:
 */
@Data
public class FundInformationVo {
    /**
     * 总资产(客户资金+持仓市值+积分+券余额)
     */
    private BigDecimal totalAssets;
    /**
     * 可用资金
     */
    private BigDecimal balance;
    /**
     * 冻结金额
     */
    private BigDecimal freezingAmount;
    /**
     * 持仓市值
     */
    private BigDecimal holdMarketValue;

    /**
     * 可提资金
     */
    private BigDecimal withdrawableCash;
    /**
     * 资金密码是否存在(1:存在 0：不存在)
     */
    private Integer passwordFlag;
}
