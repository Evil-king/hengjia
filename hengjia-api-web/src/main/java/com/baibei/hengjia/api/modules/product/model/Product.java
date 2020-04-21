package com.baibei.hengjia.api.modules.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product")
public class Product {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品描述
     */
    @Column(name = "product_desc")
    private String productDesc;

    /**
     * 货号，商品的唯一编码
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 关联分类ID
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 冗余分类标题
     */
    @Column(name = "category_title")
    private String categoryTitle;

    /**
     * 商品类型，integral=积分商品，trade=交易商品
     */
    @Column(name = "product_type")
    private String productType;

    /**
     * 原价
     */
    @Column(name = "origin_price")
    private BigDecimal originPrice;

    /**
     * 零售价
     */
    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    /**
     * 总库存
     */
    private Integer stock;

    /**
     * 商品主图片URL地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 上架状态，up=上架，down=下架
     */
    private String updown;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品名称
     *
     * @return product_name - 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称
     *
     * @param productName 商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取商品描述
     *
     * @return product_desc - 商品描述
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * 设置商品描述
     *
     * @param productDesc 商品描述
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * 获取货号，商品的唯一编码
     *
     * @return spu_no - 货号，商品的唯一编码
     */
    public String getSpuNo() {
        return spuNo;
    }

    /**
     * 设置货号，商品的唯一编码
     *
     * @param spuNo 货号，商品的唯一编码
     */
    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    /**
     * 获取关联分类ID
     *
     * @return category_id - 关联分类ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置关联分类ID
     *
     * @param categoryId 关联分类ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取冗余分类标题
     *
     * @return category_title - 冗余分类标题
     */
    public String getCategoryTitle() {
        return categoryTitle;
    }

    /**
     * 设置冗余分类标题
     *
     * @param categoryTitle 冗余分类标题
     */
    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    /**
     * 获取商品类型，integral=积分商品，trade=交易商品
     *
     * @return product_type - 商品类型，integral=积分商品，trade=交易商品
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 设置商品类型，integral=积分商品，trade=交易商品
     *
     * @param productType 商品类型，integral=积分商品，trade=交易商品
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * 获取原价
     *
     * @return origin_price - 原价
     */
    public BigDecimal getOriginPrice() {
        return originPrice;
    }

    /**
     * 设置原价
     *
     * @param originPrice 原价
     */
    public void setOriginPrice(BigDecimal originPrice) {
        this.originPrice = originPrice;
    }

    /**
     * 获取零售价
     *
     * @return sell_price - 零售价
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    /**
     * 设置零售价
     *
     * @param sellPrice 零售价
     */
    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    /**
     * 获取总库存
     *
     * @return stock - 总库存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置总库存
     *
     * @param stock 总库存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取商品主图片URL地址
     *
     * @return img_url - 商品主图片URL地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置商品主图片URL地址
     *
     * @param imgUrl 商品主图片URL地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取上架状态，up=上架，down=下架
     *
     * @return updown - 上架状态，up=上架，down=下架
     */
    public String getUpdown() {
        return updown;
    }

    /**
     * 设置上架状态，up=上架，down=下架
     *
     * @param updown 上架状态，up=上架，down=下架
     */
    public void setUpdown(String updown) {
        this.updown = updown;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}