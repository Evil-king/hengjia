package com.baibei.hengjia.admin.modules.settlement.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class CleanResultDto extends PageParam {
    private String customerNo;
    private String startTime;
    private String endTime;
}
