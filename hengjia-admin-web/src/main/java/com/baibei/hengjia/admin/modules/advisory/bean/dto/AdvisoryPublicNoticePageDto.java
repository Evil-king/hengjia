package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AdvisoryPublicNoticePageDto extends PageParam {

    /**
     * 查询标题使用
     */
    private String title;
}
