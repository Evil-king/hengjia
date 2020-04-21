package com.baibei.hengjia.api.modules.trade.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "tbl_tra_banner")
public class TradeBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 挂牌商编号
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 外部链接
     */
    @Column(name = "link_url")
    private String linkUrl;

    private List<TradeBannerImage> images = new ArrayList<>();

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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 获取挂牌商编号
     *
     * @return member_no - 挂牌商编号
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置挂牌商编号
     *
     * @param memberNo 挂牌商编号
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取外部链接
     *
     * @return link_url - 外部链接
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * 设置外部链接
     *
     * @param linkUrl 外部链接
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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

    public List<TradeBannerImage> getImages() {
        return images;
    }

    public void setImages(List<TradeBannerImage> images) {
        this.images = images;
    }
}