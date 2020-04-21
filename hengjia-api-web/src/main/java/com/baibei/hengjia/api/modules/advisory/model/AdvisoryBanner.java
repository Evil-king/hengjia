package com.baibei.hengjia.api.modules.advisory.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_advisory_banner")
public class AdvisoryBanner {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name = "banner_title")
    private String bannerTitle;

    /**
     * 图片
     */
    @Column(name = "banner_image")
    private String bannerImage;

    /**
     * 内容
     */
    @Column(name = "banner_content")
    private String bannerContent;

    /**
     * 链接
     */
    @Column(name = "banner_url")
    private String bannerUrl;

    /**
     * 排序
     */
    @Column(name = "banner_sort")
    private Integer bannerSort;

    /**
     * 是否显示,show-显示,hidden-隐藏
     */
    @Column(name = "banner_display")
    private String bannerDisplay;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

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
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取标题
     *
     * @return banner_title - 标题
     */
    public String getBannerTitle() {
        return bannerTitle;
    }

    /**
     * 设置标题
     *
     * @param bannerTitle 标题
     */
    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    /**
     * 获取图片
     *
     * @return banner_image - 图片
     */
    public String getBannerImage() {
        return bannerImage;
    }

    /**
     * 设置图片
     *
     * @param bannerImage 图片
     */
    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    /**
     * 获取内容
     *
     * @return banner_content - 内容
     */
    public String getBannerContent() {
        return bannerContent;
    }

    /**
     * 设置内容
     *
     * @param bannerContent 内容
     */
    public void setBannerContent(String bannerContent) {
        this.bannerContent = bannerContent;
    }

    /**
     * 获取链接
     *
     * @return banner_url - 链接
     */
    public String getBannerUrl() {
        return bannerUrl;
    }

    /**
     * 设置链接
     *
     * @param bannerUrl 链接
     */
    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    /**
     * 获取排序
     *
     * @return banner_sort - 排序
     */
    public Integer getBannerSort() {
        return bannerSort;
    }

    /**
     * 设置排序
     *
     * @param bannerSort 排序
     */
    public void setBannerSort(Integer bannerSort) {
        this.bannerSort = bannerSort;
    }

    /**
     * 获取是否显示,show-显示,hidden-隐藏
     *
     * @return banner_display - 是否显示,show-显示,hidden-隐藏
     */
    public String getBannerDisplay() {
        return bannerDisplay;
    }

    /**
     * 设置是否显示,show-显示,hidden-隐藏
     *
     * @param bannerDisplay 是否显示,show-显示,hidden-隐藏
     */
    public void setBannerDisplay(String bannerDisplay) {
        this.bannerDisplay = bannerDisplay;
    }

    /**
     * 获取状态(1:正常，0:禁用)
     *
     * @return flag - 状态(1:正常，0:禁用)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:禁用)
     *
     * @param flag 状态(1:正常，0:禁用)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
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
}