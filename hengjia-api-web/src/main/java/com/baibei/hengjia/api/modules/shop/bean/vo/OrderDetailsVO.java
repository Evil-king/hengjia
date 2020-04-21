package com.baibei.hengjia.api.modules.shop.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/06/04
 * <p>
 *     积分商城商品详情
 * </p>
 */
@Data
public class OrderDetailsVO {

    private String spuNo;//商品编号

    private String imageUrl;//商品主图

    private String productName;//商品名称

    private BigDecimal sellPrice;//商品售价

    private int count;//商品数量
}
