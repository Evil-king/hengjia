package com.baibei.hengjia.api.modules.user.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/28 19:24
 * @description:
 */
@Data
public class CustomerVo {
    private String customerNo;
    //手机号
    private String mobile;
    //推荐人交易商编码
    private String recommenderId;
    //会员ID
    private String memberNo;
    //用户类型（1：普通用户 2：代理商）
    private Byte customerType;
    //用户状态，详情请见CustomerStatusEnum
    private String customerStatus;
    //用户名
    private String username;
    //openid
    private String openid;
    //是否业务系统签约(0未签约 1已签约)
    private String signing;
    //积分余额
    private BigDecimal integralBalance;
    //代金券余额
    private BigDecimal couponBalance;
    //提货券余额
    private BigDecimal deliveryTicketBalance;
    /**
     * 资金密码是否存在(1:存在 0：不存在)
     */
    private Integer passwordFlag;

    /**
     * 总资产(客户资金+持仓市值+积分)
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
     * 资金监管账号
     */
    private String supAcctId;

    /**
     * 是否能看到预约购销
     */
    private boolean autoTrade;

    /**
     * 是否自动购销的白名单
     */
    private boolean whiteListFlag;

    /**
     * 是否能看到“当前转让”功能
     */
    private boolean transferFlag;

}
