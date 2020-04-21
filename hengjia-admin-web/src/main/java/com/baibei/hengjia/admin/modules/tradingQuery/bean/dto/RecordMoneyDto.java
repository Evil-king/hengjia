package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/15 10:35
 * @description:
 */
@Data
public class RecordMoneyDto extends PageParam {
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 流水号
     */
    private String recordNo;
    /**
     * 交易项目（详情见枚举：FundTradeTypeEnum）
     */
    private String tradeType;
    /**
     * 类型1：支出 2：收入
     */
    private String retype;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
}
