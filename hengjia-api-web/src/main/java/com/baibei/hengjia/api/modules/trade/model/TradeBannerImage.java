package com.baibei.hengjia.api.modules.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_banner_image")
public class TradeBannerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banner_id")
    private Long bannerId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 外部跳转链接
     */
    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否启用（1：启用；0：禁用）
     */
    private Byte flag;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return banner_id
     */
    public Long getBannerId() {
        return bannerId;
    }

    /**
     * @param bannerId
     */
    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    /**
     * 获取图片地址
     *
     * @return url - 图片地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置图片地址
     *
     * @param url 图片地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取是否启用（1：启用；0：禁用）
     *
     * @return flag - 是否启用（1：启用；0：禁用）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否启用（1：启用；0：禁用）
     *
     * @param flag 是否启用（1：启用；0：禁用）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}