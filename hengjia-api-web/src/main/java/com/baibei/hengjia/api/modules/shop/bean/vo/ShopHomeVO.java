package com.baibei.hengjia.api.modules.shop.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/06/04
 */
@Data
public class ShopHomeVO {

    private String spuNo;

    private String imgUrl;

    private String productName;

    private BigDecimal sellPrice;

}
