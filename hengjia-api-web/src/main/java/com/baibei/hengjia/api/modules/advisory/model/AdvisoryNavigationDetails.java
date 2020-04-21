package com.baibei.hengjia.api.modules.advisory.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_advisory_navigation_details")
public class AdvisoryNavigationDetails {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 导航ID
     */
    @Column(name = "navigation_id")
    private Long navigationId;

    /**
     * 对应的类型id
     */
    @Column(name = "type_id")
    private Long typeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String image;

    /**
     * 类型,article-文章,vedio-视频
     */
    private String type;

    /**
     * 链接
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

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
     * 获取导航ID
     *
     * @return navigation_id - 导航ID
     */
    public Long getNavigationId() {
        return navigationId;
    }

    /**
     * 设置导航ID
     *
     * @param navigationId 导航ID
     */
    public void setNavigationId(Long navigationId) {
        this.navigationId = navigationId;
    }

    /**
     * 获取对应的类型id
     *
     * @return type_id - 对应的类型id
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * 设置对应的类型id
     *
     * @param typeId 对应的类型id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取图片
     *
     * @return image - 图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置图片
     *
     * @param image 图片
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取类型,article-文章,vedio-视频
     *
     * @return type - 类型,article-文章,vedio-视频
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型,article-文章,vedio-视频
     *
     * @param type 类型,article-文章,vedio-视频
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取链接
     *
     * @return url - 链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置链接
     *
     * @param url 链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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