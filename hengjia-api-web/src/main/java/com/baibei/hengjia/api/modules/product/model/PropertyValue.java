package com.baibei.hengjia.api.modules.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_property_value")
public class PropertyValue {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联规格名称自定义表的ID
     */
    @Column(name = "name_id")
    private Long nameId;

    /**
     * 规格值
     */
    private String value;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取关联规格名称自定义表的ID
     *
     * @return name_id - 关联规格名称自定义表的ID
     */
    public Long getNameId() {
        return nameId;
    }

    /**
     * 设置关联规格名称自定义表的ID
     *
     * @param nameId 关联规格名称自定义表的ID
     */
    public void setNameId(Long nameId) {
        this.nameId = nameId;
    }

    /**
     * 获取规格值
     *
     * @return value - 规格值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置规格值
     *
     * @param value 规格值
     */
    public void setValue(String value) {
        this.value = value;
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