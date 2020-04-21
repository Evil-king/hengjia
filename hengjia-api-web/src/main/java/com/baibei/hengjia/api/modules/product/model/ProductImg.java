package com.baibei.hengjia.api.modules.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_img")
public class ProductImg {
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
     * 图片URL地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取图片URL地址
     *
     * @return img_url - 图片URL地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置图片URL地址
     *
     * @param imgUrl 图片URL地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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