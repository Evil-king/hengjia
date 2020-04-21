package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class EntrustOrderDto extends PageParam {

    private String holdType;

    /**
     * 委托方向
     */
    private String direction;

    /**
     * 用户账号
     */
    private String customerNo;

    /**
     * 委托单号
     */
    private String entrustNo;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 委托结果
     */
    private String result;

    /**
     * 委托开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    /**
     * 委托结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
}
