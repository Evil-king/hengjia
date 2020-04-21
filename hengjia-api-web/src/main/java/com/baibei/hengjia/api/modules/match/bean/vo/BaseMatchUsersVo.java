package com.baibei.hengjia.api.modules.match.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Classname BaseMatchUsersVo
 * @Description 当天所有应该给予配货的用户信息（当天+以前配货失败数据）
 * @Date 2019/8/6 17:56
 * @Created by Longer
 */
@Data
public class BaseMatchUsersVo {
    /**
     * 成交单号
     */
    private String dealNo;

    private String customerNo;

    private String productTradeNo;

    /**
     * 配货数量
     */
    private Long matchNum;

    /**
     * 失败次数
     */
    private int failCount;

    private Date createTime;

    /**
     * 执行类型。send=送货；plan=配货
     */
    private String operateType;
}
