package com.baibei.hengjia.admin.modules.match.bean.vo;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
@Data
public class MatchLogVo {
    private Long id;

    private String matchNo;

    /**
     * 用户编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
     */
    private String matchType;

    /**
     * 配票数量
     */
    private BigDecimal matchNum;

    /**
     * 成功配票的数量
     */
    private BigDecimal matchSuccessNum;

    /**
     * 配票金额（用户应扣除的金额）=成本+手续费
     */
    private BigDecimal matchMoney;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    private String matchStatus;

    /**
     * 时间段（年月日格式，和match_no字段做联合唯一索引）
     */
    private String period;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "modify_time")
    private String modifyTime;

    /**
     * 是否有效（1：有效；0：无效）
     */
    private Byte flag;

}