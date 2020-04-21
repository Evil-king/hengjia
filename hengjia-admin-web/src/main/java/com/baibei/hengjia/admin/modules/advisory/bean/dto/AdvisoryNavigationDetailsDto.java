package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AdvisoryNavigationDetailsDto extends PageParam {
    private String navigationId;
    private String title;
    private String createTime;
    private String modifyTime;
}
