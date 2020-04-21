package com.baibei.hengjia.admin.modules.customer.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/7/15 16:14
 * @description:
 */
@Data
public class CustomerVo  extends BaseRowModel {
    private String id;
    /**
     * 用户编号
     */
    @ExcelProperty(value = "用户编号", index = 1)
    private String customerNo;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 2)
    private String username;
    /**
     * 用户类型 1：普通用户 2：挂牌商 3：经销商
     */
    @ExcelProperty(value = "用户类型", index = 3)
    private String customerType;
    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名", index = 4)
    private String realname;
    /**
     * 身份证
     */
    @ExcelProperty(value = "身份证号", index = 5)
    private String idCard;
    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号", index = 6)
    private String mobile;
    /**
     * 归属挂牌商
     */
    @ExcelProperty(value = "归属挂牌商", index = 7)
    private String memberNo;
    /**
     * 直属推荐人
     */
    @ExcelProperty(value = "直属推荐人", index = 8)
    private String recommenderNo;
    /**
     * 总资产
     */
    @ExcelProperty(value = "总资产", index = 9)
    private BigDecimal totalAssets;
    /**
     * 可用余额
     */
    @ExcelProperty(value = "可用余额", index = 10)
    private BigDecimal  balance;
    /**
     * 可提资金
     */
    @ExcelProperty(value = "可提资金", index = 11)
    private BigDecimal withdrawCash;
    /**
     * 冻结资金
     */
    @ExcelProperty(value = "冻结资金", index = 12)
    private BigDecimal frozenMoney;
    /**
     * 积分余额
     */
    @ExcelProperty(value = "积分余额", index = 13)
    private BigDecimal integralBalance;
    /**
     * 券余额
     */
    @ExcelProperty(value = "券余额", index = 14)
    private BigDecimal couponAccount;
    /**
     * 持有本票
     */
    @ExcelProperty(value = "持有本票", index = 15)
    private BigDecimal holdMain;
    /**
     * 持有配票
     */
    @ExcelProperty(value = "持有配票", index = 16)
    private BigDecimal holdMatch;
    /**
     * 是否提货
     */
    @ExcelProperty(value = "是否提货", index = 17)
    private String isPickUp;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 18)
    private String createTime;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 19)
    private String status;
}
