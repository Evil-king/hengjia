package com.baibei.hengjia.api.modules.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_category")
public class Category {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级ID，0表示顶层分类
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类标题
     */
    private String title;

    /**
     * 排序
     */
    private Integer sort;

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
     * 状态(1:正常，0:禁用)
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
     * 获取父级ID，0表示顶层分类
     *
     * @return parent_id - 父级ID，0表示顶层分类
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级ID，0表示顶层分类
     *
     * @param parentId 父级ID，0表示顶层分类
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取分类标题
     *
     * @return title - 分类标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置分类标题
     *
     * @param title 分类标题
     */
    public void setTitle(String title) {
        this.title = title;
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
}