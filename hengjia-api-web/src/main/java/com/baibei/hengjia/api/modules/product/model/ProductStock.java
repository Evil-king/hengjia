package com.baibei.hengjia.api.modules.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_stock")
public class ProductStock {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 货号，商品的唯一编码
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 冗余商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 归属挂牌商
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 归属用户编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 入库数量
     */
    @Column(name = "putin_count")
    private Integer putinCount;

    /**
     * 补货数量
     */
    @Column(name = "replenish_count")
    private Integer replenishCount;

    /**
     * 总数量
     */
    @Column(name = "total_count")
    private Integer totalCount;

    /**
     * 提货数量
     */
    @Column(name = "take_count")
    private Integer takeCount;

    /**
     * 剩余数量
     */
    @Column(name = "remind_count")
    private Integer remindCount;

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
     * 获取货号，商品的唯一编码
     *
     * @return spu_no - 货号，商品的唯一编码
     */
    public String getSpuNo() {
        return spuNo;
    }

    /**
     * 设置货号，商品的唯一编码
     *
     * @param spuNo 货号，商品的唯一编码
     */
    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    /**
     * 获取冗余商品名称
     *
     * @return product_name - 冗余商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置冗余商品名称
     *
     * @param productName 冗余商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取归属挂牌商
     *
     * @return member_no - 归属挂牌商
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置归属挂牌商
     *
     * @param memberNo 归属挂牌商
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取归属用户编码
     *
     * @return customer_no - 归属用户编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置归属用户编码
     *
     * @param customerNo 归属用户编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取成本
     *
     * @return cost - 成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本
     *
     * @param cost 成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取入库数量
     *
     * @return putin_count - 入库数量
     */
    public Integer getPutinCount() {
        return putinCount;
    }

    /**
     * 设置入库数量
     *
     * @param putinCount 入库数量
     */
    public void setPutinCount(Integer putinCount) {
        this.putinCount = putinCount;
    }

    /**
     * 获取补货数量
     *
     * @return replenish_count - 补货数量
     */
    public Integer getReplenishCount() {
        return replenishCount;
    }

    /**
     * 设置补货数量
     *
     * @param replenishCount 补货数量
     */
    public void setReplenishCount(Integer replenishCount) {
        this.replenishCount = replenishCount;
    }

    /**
     * 获取总数量
     *
     * @return total_count - 总数量
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总数量
     *
     * @param totalCount 总数量
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取提货数量
     *
     * @return take_count - 提货数量
     */
    public Integer getTakeCount() {
        return takeCount;
    }

    /**
     * 设置提货数量
     *
     * @param takeCount 提货数量
     */
    public void setTakeCount(Integer takeCount) {
        this.takeCount = takeCount;
    }

    /**
     * 获取剩余数量
     *
     * @return remind_count - 剩余数量
     */
    public Integer getRemindCount() {
        return remindCount;
    }

    /**
     * 设置剩余数量
     *
     * @param remindCount 剩余数量
     */
    public void setRemindCount(Integer remindCount) {
        this.remindCount = remindCount;
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