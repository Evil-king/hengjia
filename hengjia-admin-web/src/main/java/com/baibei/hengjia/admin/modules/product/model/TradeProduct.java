package com.baibei.hengjia.admin.modules.product.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_trade_product")
public class TradeProduct {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 供货商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 所属用户编码
     */
    @Column(name = "belong_no")
    private String belongNo;

    /**
     * 商品货号
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 商品原始数量
     */
    private Integer count;

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
     * 获取供货商编码
     *
     * @return customer_no - 供货商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置供货商编码
     *
     * @param customerNo 供货商编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取所属用户编码
     *
     * @return belong_no - 所属用户编码
     */
    public String getBelongNo() {
        return belongNo;
    }

    /**
     * 设置所属用户编码
     *
     * @param belongNo 所属用户编码
     */
    public void setBelongNo(String belongNo) {
        this.belongNo = belongNo;
    }

    /**
     * 获取商品货号
     *
     * @return spu_no - 商品货号
     */
    public String getSpuNo() {
        return spuNo;
    }

    /**
     * 设置商品货号
     *
     * @param spuNo 商品货号
     */
    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    /**
     * 获取成本价
     *
     * @return cost - 成本价
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本价
     *
     * @param cost 成本价
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取商品原始数量
     *
     * @return count - 商品原始数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置商品原始数量
     *
     * @param count 商品原始数量
     */
    public void setCount(Integer count) {
        this.count = count;
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