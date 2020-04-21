package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import lombok.Data;


@Data
public class DeliveryVo {

    private Long id;

    private String customerNo;

    /**
     * 商品id
     */
    private Long productId;

    private String productTradeNo;

    private String productTradeName;

    /**
     * 商品发行价
     */
    private Long issuePrice;

    /**
     * 持仓商品类型（main=本票,match=配票）
     */
    private String holdType;

    /**
     * 收货地址id
     */
    private Long addressId;

    /**
     * 提货订单号
     */
    private String deliveryNo;

    /**
     * 运单号
     */
    private String logisticsNo;

    /**
     * 提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     */
    private Integer deliveryStatus;

    /**
     * 申请提货时间
     */
    private String deliveryTime;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 发货时间
     */
    private String pendingTime;

    /**
     * 收货时间
     */
    private String receiveTime;

    /**
     * 物流类型
     */
    private String logisticsType;

    /**
     * 物流公司名称
     */
    private String logisticsCompany;

    /**
     * 仓储费
     */
    private Long storageCharge;

    /**
     * 出仓费
     */
    private Long clearanceCharge;

    /**
     * 运费
     */
    private Long freight;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提货数量
     */
    private Integer deliveryCount;

    private String createTime;

    private String modifyTime;

    /**
     * 是否删除（1：正常；0：已删除）
     */
    private Byte flag;

    /**
     * 用户电话
     */
    private String mobile;

    /**
     * 收货联系电话
     */
    private String receivingMobile;

    /**
     * 收货人姓名
     */
    private String receivingName;

    /**
     * 收货地址
     */
    private String receivingAddress;

    /**
     * 来源
     */
    private String source;

}