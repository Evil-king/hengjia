package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname DeliveryApplyDto
 * @Description 用户提货dto（提货申请）
 * @Date 2019/6/5 10:47
 * @Created by Longer
 */
@Data
public class DeliveryApplyDto extends CustomerBaseDto {

    /*
        商品id
     */
    private Long productId;
    /*
        收货地址id
     */
    @NotNull(message = "未指定收货地址")
    private Long addressId;
    /*
        商品交易编码
     */
    @NotBlank(message = "未指定提货商品")
    private String productTradeNo;

    /*
        商品持仓类型(main=本票,match=配票)
     */
    @NotBlank(message = "未指定提货商品类型")
    private String holdType;

    /*
        提货数量
     */
    @NotNull(message = "未指定提货数量")
    private Integer deliveryCount;

    private String remark;

    /**
     * 是否需要判断交易日（1：不判断；0:判断；null：判断）
     */
    private String validateTradeDayFlag = "0";

    private Integer flag = 1;


    /**
     * 提货单号
     */
    private String deliveryNo;

    /**
     * 来源。deliveryTicket=提货券。plan=配货，新用户首提
     */
    private String source;
}
