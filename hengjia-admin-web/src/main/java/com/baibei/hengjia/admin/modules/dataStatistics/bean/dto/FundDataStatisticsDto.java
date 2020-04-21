package com.baibei.hengjia.admin.modules.dataStatistics.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/8/22 14:26
 * @description:
 */
@Data
public class FundDataStatisticsDto extends PageParam {
    private String customerNo;
    private String username;
    private String realname;
    private String startTime;
    private String endTime;
    /**
     * 这只是一个标识，判断查询时间内是否包含于当天的
     */
    private String flag;
}
