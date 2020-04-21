package com.baibei.hengjia.api.modules.settlement.bean.dto;

import com.baibei.hengjia.api.modules.cash.base.BaseRequest;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/26 8:45 PM
 * @description:
 */
@Data
public class BeginCleanDto extends BaseRequest {
    /**
     * 批量标识
     */
    private String funcFlag;

    /**
     * 批量文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String fileSize;


    /**
     * 清收买卖货款扎差金额
     */
    private BigDecimal qsZcAmount;

    /**
     * 冻结总金额
     */
    private BigDecimal freezeAmount;

    /**
     * 解冻总金额
     */
    private BigDecimal unfreezeAmount;

    /**
     * 损益扎差数
     */
    private BigDecimal syZcAmount;

    /**
     * 文件密码
     */
    private String reserve;

    private String thirdLogNo;

}
