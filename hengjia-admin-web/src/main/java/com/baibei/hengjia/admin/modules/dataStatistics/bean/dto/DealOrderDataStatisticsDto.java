package com.baibei.hengjia.admin.modules.dataStatistics.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/8/27 13:40
 * @description:
 */
@Data
public class DealOrderDataStatisticsDto extends PageParam {
    private String customerNo;
    private String username;
    private String realname;
    private String startTime;
    private String endTime;
}
