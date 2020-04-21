package com.baibei.hengjia.api.modules.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_shop_product")
public class ShopProduct {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 货号，商品的唯一编码
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * sku编码
     */
    @Column(name = "sku_no")
    private String skuNo;

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
     * 规格属性json数据
     */
    private String properties;

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
     * 获取sku编码
     *
     * @return sku_no - sku编码
     */
    public String getSkuNo() {
        return skuNo;
    }

    /**
     * 设置sku编码
     *
     * @param skuNo sku编码
     */
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
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

    /**
     * 获取规格属性json数据
     *
     * @return properties - 规格属性json数据
     */
    public String getProperties() {
        return properties;
    }

    /**
     * 设置规格属性json数据
     *
     * @param properties 规格属性json数据
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }
}