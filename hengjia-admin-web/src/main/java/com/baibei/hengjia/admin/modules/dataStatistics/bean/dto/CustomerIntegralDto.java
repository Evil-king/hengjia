package com.baibei.hengjia.admin.modules.dataStatistics.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/8/15 15:33
 * @description:
 */
@Data
public class CustomerIntegralDto extends PageParam {
    private String customerNo;
    private String username;
    private String realname;
    private String startTime;
    private String endTime;
}
