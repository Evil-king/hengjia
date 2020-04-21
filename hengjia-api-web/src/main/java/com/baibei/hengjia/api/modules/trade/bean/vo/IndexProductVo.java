package com.baibei.hengjia.api.modules.trade.bean.vo;

import com.baibei.hengjia.api.modules.product.model.ProductImg;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname IndexProductVo
 * @Description 交易首页商品列表
 * @Date 2019/6/4 19:20
 * @Created by Longer
 */
@Data
public class IndexProductVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 货号，商品的唯一编码
     */
    @Column(name = "spu_no")
    private String spuNo;
    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 商品交易名称
     */
    @Column(name = "product_trade_name")
    private String productTradeName;

    /**
     * 发行价
     */
    @Column(name = "issue_price")
    private BigDecimal issuePrice;

    @Column(name = "img_url")
    private String imgUrl;

}
