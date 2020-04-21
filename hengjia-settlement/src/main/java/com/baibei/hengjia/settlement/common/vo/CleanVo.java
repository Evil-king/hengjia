package com.baibei.hengjia.settlement.common.vo;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/24 4:39 PM
 * @description: 清算文件
 */
@Data
public class CleanVo {
    private String queryId;
    private String tranDateTime;
    private String counterId;
    private String supAcctId;
    private String funcCode;
    private String custAcctId;
    private String custName;
    private String thirdCustId;
    private String thirdLogNo;
    private String ccyCode;
    private String freezeAmount;
    private String unfreezeAmount;
    private String addTranAmount;
    private String cutTranAmount;
    private String profitAmount;
    private String lossAmount;
    private String tranHandFee;
    private String tranCount;
    private String newBalance;
    private String newFreezeAmount;
    private String note;
    private String reserve;
}
