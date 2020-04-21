package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/24 10:16
 * @description:
 */
@Data
public class AutoTradeRecordVo {
    private String customerNo;
    private String productTradeNo;
    private String productTradeName;
    private String imageUrl;
    private BigDecimal price;
    private Integer count;
    private String type;
    private Date createTime;
    private String spuNo;
}