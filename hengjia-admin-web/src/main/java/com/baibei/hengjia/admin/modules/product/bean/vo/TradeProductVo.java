package com.baibei.hengjia.admin.modules.product.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/2 6:52 PM
 * @description:
 */
@Data
public class TradeProductVo {
    private Long id;

    /**
     * 供货商编码
     */
    private String customerNo;

    /**
     * 所属用户编码
     */
    private String belongNo;

    /**
     * 商品货号
     */
    private String spuNo;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 商品原始数量
     */
    private Integer count;

    /**
     * 创建时间
     */
    private Date createTime;
}
