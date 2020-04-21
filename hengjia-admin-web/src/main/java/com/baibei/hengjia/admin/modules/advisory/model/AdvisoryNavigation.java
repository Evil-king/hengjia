package com.baibei.hengjia.admin.modules.advisory.model;

import lombok.Builder;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_advisory_navigation")
//@Builder
public class AdvisoryNavigation {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 导航名称
     */
    @Column(name = "navigation_name")
    private String navigationName;

    /**
     * 是否显示,,show-显示,hidden-隐藏
     */
    @Column(name = "navigation_display")
    private String navigationDisplay;

    /**
     * 排序
     */
    @Column(name = "navigation_sort")
    private Integer navigationSort;

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
     * 获取导航名称
     *
     * @return navigation_name - 导航名称
     */
    public String getNavigationName() {
        return navigationName;
    }

    /**
     * 设置导航名称
     *
     * @param navigationName 导航名称
     */
    public void setNavigationName(String navigationName) {
        this.navigationName = navigationName;
    }

    /**
     * 获取是否显示,,show-显示,hidden-隐藏
     *
     * @return navigation_display - 是否显示,,show-显示,hidden-隐藏
     */
    public String getNavigationDisplay() {
        return navigationDisplay;
    }

    /**
     * 设置是否显示,,show-显示,hidden-隐藏
     *
     * @param navigationDisplay 是否显示,,show-显示,hidden-隐藏
     */
    public void setNavigationDisplay(String navigationDisplay) {
        this.navigationDisplay = navigationDisplay;
    }

    /**
     * 获取排序
     *
     * @return navigation_sort - 排序
     */
    public Integer getNavigationSort() {
        return navigationSort;
    }

    /**
     * 设置排序
     *
     * @param navigationSort 排序
     */
    public void setNavigationSort(Integer navigationSort) {
        this.navigationSort = navigationSort;
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