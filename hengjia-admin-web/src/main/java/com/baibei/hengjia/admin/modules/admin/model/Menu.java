package com.baibei.hengjia.admin.modules.admin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_admin_menu")
public class Menu {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否外链
     */
    @Column(name = "i_frame")
    private Boolean iFrame;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 组件
     */
    private String component;

    /**
     * 上级菜单ID
     */
    private Long pid;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接地址
     */
    private String path;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否外链
     *
     * @return i_frame - 是否外链
     */
    public Boolean getiFrame() {
        return iFrame;
    }

    /**
     * 设置是否外链
     *
     * @param iFrame 是否外链
     */
    public void setiFrame(Boolean iFrame) {
        this.iFrame = iFrame;
    }

    /**
     * 获取菜单名称
     *
     * @return name - 菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置菜单名称
     *
     * @param name 菜单名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取组件
     *
     * @return component - 组件
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置组件
     *
     * @param component 组件
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取上级菜单ID
     *
     * @return pid - 上级菜单ID
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置上级菜单ID
     *
     * @param pid 上级菜单ID
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Long getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Long sort) {
        this.sort = sort;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取链接地址
     *
     * @return path - 链接地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置链接地址
     *
     * @param path 链接地址
     */
    public void setPath(String path) {
        this.path = path;
    }
}