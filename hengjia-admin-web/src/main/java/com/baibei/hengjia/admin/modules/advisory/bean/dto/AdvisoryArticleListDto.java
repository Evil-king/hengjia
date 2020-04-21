package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AdvisoryArticleListDto extends PageParam {
    private String articleTitle;
    private String createTime;
    private String modifyTime;
}
