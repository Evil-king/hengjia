package com.baibei.hengjia.api.modules.cash.bean.dto;

import com.baibei.hengjia.api.modules.cash.base.BaseRequest;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class OrderDepositDto extends BaseRequest {

    /**
     * 入金金额
     */
    @NotNull
    @Range(min = 0, max = 999999999)
    private BigDecimal tranAmount;

    /**
     * 入金账号
     */
    @NotNull
    @Size(max = 32)
    private String inAcctId;


    /**
     * 入金账号名称
     */
    @NotNull
    @Size(max = 120)
    private String inAcctIdName;

    /**
     * 币种
     */
    @NotNull
    @Size(max = 3, min = 3)
    private String ccyCode;

    /**
     * 会计日期
     */
    @NotNull
    private Date acctDate;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 会员子账号
     */
    @NotNull
    @Size(max = 32)
    private String custAcctId;


}
