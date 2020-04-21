package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;

@Data
public class AmountReturnVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 用户账号
     */
    private String customerNo;

    /**
     * 待返比例
     */
    private BigDecimal returnRate;

    /**
     * 待返金额
     */
    private BigDecimal returnAmount;

    /**
     * 状态，wait=待返，completed=已返
     */
    private String status;

    /**
     * 返还类型，fee=手续费返还办理，integral=积分返还办理
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;
}
