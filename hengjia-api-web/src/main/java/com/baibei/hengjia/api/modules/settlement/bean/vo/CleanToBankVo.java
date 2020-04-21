package com.baibei.hengjia.api.modules.settlement.bean.vo;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/26 7:29 PM
 * @description:
 */
@Data
public class CleanToBankVo {
    private String funcFlag;
    private String fileName;
    private String fileSize;
    private String supAcctId;
    private String qsZcAmount;
    private String freezeAmount;
    private String unfreezeAmount;
    private String syZcAmount;
    private String password;
    private String frontLogNo;
    private String reserve;
}
