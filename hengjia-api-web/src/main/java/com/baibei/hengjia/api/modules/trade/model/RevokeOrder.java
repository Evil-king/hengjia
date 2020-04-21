package com.baibei.hengjia.api.modules.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_revoke_order")
public class RevokeOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联委托单ID
     */
    @Column(name = "entrust_id")
    private Long entrustId;

    /**
     * 撤销数量
     */
    private Integer count;

    /**
     * 撤销时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

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
     * 获取关联委托单ID
     *
     * @return entrust_id - 关联委托单ID
     */
    public Long getEntrustId() {
        return entrustId;
    }

    /**
     * 设置关联委托单ID
     *
     * @param entrustId 关联委托单ID
     */
    public void setEntrustId(Long entrustId) {
        this.entrustId = entrustId;
    }

    /**
     * 获取撤销数量
     *
     * @return count - 撤销数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置撤销数量
     *
     * @param count 撤销数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取撤销时间
     *
     * @return create_time - 撤销时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置撤销时间
     *
     * @param createTime 撤销时间
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