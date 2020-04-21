package com.baibei.hengjia.api.modules.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_ref")
public class CustomerRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 会员编码
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 直属推荐人编码
     */
    @Column(name = "recommender_id")
    private String recommenderId;

    /**
     * 操作类型（1：注册，2：用户转移）
     */
    @Column(name = "operation_type")
    private Byte operationType;

    /**
     * 操作人（后台用户 0为注册）
     */
    private Long operator;

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
     * 状态（1：正常；0：已删除）
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
     * 获取交易商编码
     *
     * @return customer_no - 交易商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码
     *
     * @param customerNo 交易商编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取会员编码
     *
     * @return member_no - 会员编码
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置会员编码
     *
     * @param memberNo 会员编码
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取直属推荐人编码
     *
     * @return recommender_id - 直属推荐人编码
     */
    public String getRecommenderId() {
        return recommenderId;
    }

    /**
     * 设置直属推荐人编码
     *
     * @param recommenderId 直属推荐人编码
     */
    public void setRecommenderId(String recommenderId) {
        this.recommenderId = recommenderId;
    }

    /**
     * 获取操作类型（1：注册，2：用户转移）
     *
     * @return operation_type - 操作类型（1：注册，2：用户转移）
     */
    public Byte getOperationType() {
        return operationType;
    }

    /**
     * 设置操作类型（1：注册，2：用户转移）
     *
     * @param operationType 操作类型（1：注册，2：用户转移）
     */
    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    /**
     * 获取操作人（后台用户 0为注册）
     *
     * @return operator - 操作人（后台用户 0为注册）
     */
    public Long getOperator() {
        return operator;
    }

    /**
     * 设置操作人（后台用户 0为注册）
     *
     * @param operator 操作人（后台用户 0为注册）
     */
    public void setOperator(Long operator) {
        this.operator = operator;
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
     * 获取状态（1：正常；0：已删除）
     *
     * @return flag - 状态（1：正常；0：已删除）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：正常；0：已删除）
     *
     * @param flag 状态（1：正常；0：已删除）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}