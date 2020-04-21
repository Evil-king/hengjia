package com.baibei.hengjia.api.modules.settlement.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/24 4:39 PM
 * @description: 清算文件
 */
@Data
public class CleanVo {
    private Integer queryId;
    private String tranDateTime;
    private String counterId;
    private String supacctId;
    private String funcCode;
    private String custAcctId;
    private String custName;
    private String thirdCustId;
    private String thirdLogNo;
    private String ccyCode;
    // 以下数据默认为0
    private BigDecimal freezeAmount = BigDecimal.ZERO;
    private BigDecimal unfreezeAmount = BigDecimal.ZERO;
    private BigDecimal addTranAmount = BigDecimal.ZERO;
    private BigDecimal cutTranAmount = BigDecimal.ZERO;
    private BigDecimal profitAmount = BigDecimal.ZERO;
    private BigDecimal lossAmount = BigDecimal.ZERO;
    private BigDecimal tranHandFee = BigDecimal.ZERO;
    private BigDecimal tranCount = BigDecimal.ZERO;
    private BigDecimal newBalance = BigDecimal.ZERO;
    private BigDecimal newFreezeAmount = BigDecimal.ZERO;
    private String note;
    private String reserve;


    //附加便于统计的数据字段

    // 总手续费=买入手续费+卖出手续费+提现手续费+配票手续费
    private BigDecimal totalFee = BigDecimal.ZERO;

    private Byte funcFlag;

}
