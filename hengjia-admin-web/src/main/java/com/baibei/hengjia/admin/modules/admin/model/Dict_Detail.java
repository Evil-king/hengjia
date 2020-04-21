package com.baibei.hengjia.admin.modules.admin.model;

import javax.persistence.*;

@Table(name = "tbl_admin_dict_detail")
public class Dict_Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private String sort;

    /**
     * 字典id
     */
    @Column(name = "dict_id")
    private Long dictId;

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
     * 获取字典标签
     *
     * @return label - 字典标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置字典标签
     *
     * @param label 字典标签
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 获取字典值
     *
     * @return value - 字典值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置字典值
     *
     * @param value 字典值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 获取字典id
     *
     * @return dict_id - 字典id
     */
    public Long getDictId() {
        return dictId;
    }

    /**
     * 设置字典id
     *
     * @param dictId 字典id
     */
    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }
}