package com.baibei.hengjia.api.modules.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_match_config")
public class MatchConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 开关（on:开启；off:关闭）
     */
    @Column(name = "match_switch")
    private String matchSwitch;

    /**
     * 开关类型（tradeMatch:交易配票；deliveryMatch:提货配票）
     */
    @Column(name = "switch_type")
    private String switchType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 1:启用；0：禁用
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
     * 获取开关（on:开启；off:关闭）
     *
     * @return match_switch - 开关（on:开启；off:关闭）
     */
    public String getMatchSwitch() {
        return matchSwitch;
    }

    /**
     * 设置开关（on:开启；off:关闭）
     *
     * @param matchSwitch 开关（on:开启；off:关闭）
     */
    public void setMatchSwitch(String matchSwitch) {
        this.matchSwitch = matchSwitch;
    }

    /**
     * 获取开关类型（tradeMatch:交易配票；deliveryMatch:提货配票）
     *
     * @return switch_type - 开关类型（tradeMatch:交易配票；deliveryMatch:提货配票）
     */
    public String getSwitchType() {
        return switchType;
    }

    /**
     * 设置开关类型（tradeMatch:交易配票；deliveryMatch:提货配票）
     *
     * @param switchType 开关类型（tradeMatch:交易配票；deliveryMatch:提货配票）
     */
    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取1:启用；0：禁用
     *
     * @return flag - 1:启用；0：禁用
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置1:启用；0：禁用
     *
     * @param flag 1:启用；0：禁用
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "MatchConfig{" +
                "id=" + id +
                ", matchSwitch='" + matchSwitch + '\'' +
                ", switchType='" + switchType + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", flag=" + flag +
                '}';
    }
}