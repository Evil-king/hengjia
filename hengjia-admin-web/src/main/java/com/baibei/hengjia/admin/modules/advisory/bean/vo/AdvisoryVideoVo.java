package com.baibei.hengjia.admin.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryVideoVo {
    private long id;
    private String navigationId;
    private String videoTitle;
    private String videoIndex;
    private String videoType;
    private String videoSort;
    private String videoUrl;
    private String videoDisplay;
    private String createTime;
    private String modifyTime;
}
