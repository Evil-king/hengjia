package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AdvisoryVideoListDto extends PageParam {
    private String videoTitle;
    private String createTime;
    private String modifyTime;
}
