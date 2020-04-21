package com.baibei.hengjia.admin.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryArticleVo {
    private long id;
    private String navigationId;
    private String articleTitle;
    private String articleImage;
    private String articleType;
    private String articleUrl;
    private String articleContent;
    private String articleDisplay;
    private String createTime;
    private String modifyTime;
}
