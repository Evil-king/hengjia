package com.baibei.hengjia.admin.modules.settlement.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_signing_record")
public class SigningRecord {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 资金汇总账号
     */
    @Column(name = "sup_acct_Id")
    private String supAcctId;

    /**
     * 会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     */
    @Column(name = "cust_acct_Id")
    private String custAcctId;

    /**
     * 会员名称
     */
    @Column(name = "cust_name")
    private String custName;

    /**
     * 会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     */
    @Column(name = "id_type")
    private String idType;

    /**
     * 会员证件名称
     */
    @Column(name = "id_code")
    private String idCode;

    /**
     * 出入金账号
     */
    @Column(name = "related_acct_Id")
    private String relatedAcctId;

    /**
     * 账号性质(3：出金账号&入金账号)
     */
    @Column(name = "acct_flag")
    private String acctFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否删除(1:指定,2:修改,3:删除)
     */
    @Column(name = "func_flag")
    private Byte funcFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 转账方式(1、本行2、同城3、异地汇款)
     */
    @Column(name = "tran_type")
    private String tranType;

    /**
     * 账号名称
     */
    @Column(name = "acct_name")
    private String acctName;

    /**
     * 银行号
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 开户行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 原出入金账号
     */
    @Column(name = "old_related_acct_Id")
    private String oldRelatedAcctId;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 交易网流水号
     */
    @Column(name = "third_log_no")
    private String thirdLogNo;

    /**
     * 会员代码((由交易网生成,标识会员在平台身份的ID)
     */
    @Column(name = "third_cust_id")
    private String thirdCustId;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取资金汇总账号
     *
     * @return sup_acct_Id - 资金汇总账号
     */
    public String getSupAcctId() {
        return supAcctId;
    }

    /**
     * 设置资金汇总账号
     *
     * @param supAcctId 资金汇总账号
     */
    public void setSupAcctId(String supAcctId) {
        this.supAcctId = supAcctId;
    }

    /**
     * 获取会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     *
     * @return cust_acct_Id - 会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     */
    public String getCustAcctId() {
        return custAcctId;
    }

    /**
     * 设置会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     *
     * @param custAcctId 会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     */
    public void setCustAcctId(String custAcctId) {
        this.custAcctId = custAcctId;
    }

    /**
     * 获取会员名称
     *
     * @return cust_name - 会员名称
     */
    public String getCustName() {
        return custName;
    }

    /**
     * 设置会员名称
     *
     * @param custName 会员名称
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * 获取会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     *
     * @return id_type - 会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     */
    public String getIdType() {
        return idType;
    }

    /**
     * 设置会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     *
     * @param idType 会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }

    /**
     * 获取会员证件名称
     *
     * @return id_code - 会员证件名称
     */
    public String getIdCode() {
        return idCode;
    }

    /**
     * 设置会员证件名称
     *
     * @param idCode 会员证件名称
     */
    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    /**
     * 获取出入金账号
     *
     * @return related_acct_Id - 出入金账号
     */
    public String getRelatedAcctId() {
        return relatedAcctId;
    }

    /**
     * 设置出入金账号
     *
     * @param relatedAcctId 出入金账号
     */
    public void setRelatedAcctId(String relatedAcctId) {
        this.relatedAcctId = relatedAcctId;
    }

    /**
     * 获取账号性质(3：出金账号&入金账号)
     *
     * @return acct_flag - 账号性质(3：出金账号&入金账号)
     */
    public String getAcctFlag() {
        return acctFlag;
    }

    /**
     * 设置账号性质(3：出金账号&入金账号)
     *
     * @param acctFlag 账号性质(3：出金账号&入金账号)
     */
    public void setAcctFlag(String acctFlag) {
        this.acctFlag = acctFlag;
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
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取是否删除(1:指定,2:修改,3:删除)
     *
     * @return func_flag - 是否删除(1:指定,2:修改,3:删除)
     */
    public Byte getFuncFlag() {
        return funcFlag;
    }

    /**
     * 设置是否删除(1:指定,2:修改,3:删除)
     *
     * @param funcFlag 是否删除(1:指定,2:修改,3:删除)
     */
    public void setFuncFlag(Byte funcFlag) {
        this.funcFlag = funcFlag;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取转账方式(1、本行2、同城3、异地汇款)
     *
     * @return tran_type - 转账方式(1、本行2、同城3、异地汇款)
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * 设置转账方式(1、本行2、同城3、异地汇款)
     *
     * @param tranType 转账方式(1、本行2、同城3、异地汇款)
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    /**
     * 获取账号名称
     *
     * @return acct_name - 账号名称
     */
    public String getAcctName() {
        return acctName;
    }

    /**
     * 设置账号名称
     *
     * @param acctName 账号名称
     */
    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    /**
     * 获取银行号
     *
     * @return bank_code - 银行号
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置银行号
     *
     * @param bankCode 银行号
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * 获取开户行名称
     *
     * @return bank_name - 开户行名称
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户行名称
     *
     * @param bankName 开户行名称
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取原出入金账号
     *
     * @return old_related_acct_Id - 原出入金账号
     */
    public String getOldRelatedAcctId() {
        return oldRelatedAcctId;
    }

    /**
     * 设置原出入金账号
     *
     * @param oldRelatedAcctId 原出入金账号
     */
    public void setOldRelatedAcctId(String oldRelatedAcctId) {
        this.oldRelatedAcctId = oldRelatedAcctId;
    }

    /**
     * 获取保留域
     *
     * @return reserve - 保留域
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * 设置保留域
     *
     * @param reserve 保留域
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    /**
     * 获取交易网流水号
     *
     * @return third_log_no - 交易网流水号
     */
    public String getThirdLogNo() {
        return thirdLogNo;
    }

    /**
     * 设置交易网流水号
     *
     * @param thirdLogNo 交易网流水号
     */
    public void setThirdLogNo(String thirdLogNo) {
        this.thirdLogNo = thirdLogNo;
    }

    /**
     * 获取会员代码((由交易网生成,标识会员在平台身份的ID)
     *
     * @return third_cust_id - 会员代码((由交易网生成,标识会员在平台身份的ID)
     */
    public String getThirdCustId() {
        return thirdCustId;
    }

    /**
     * 设置会员代码((由交易网生成,标识会员在平台身份的ID)
     *
     * @param thirdCustId 会员代码((由交易网生成,标识会员在平台身份的ID)
     */
    public void setThirdCustId(String thirdCustId) {
        this.thirdCustId = thirdCustId;
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