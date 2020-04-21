package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import lombok.Data;

@Data
public class AdvisoryArticleDto {
    private String id;
    private String navigationId;
    private String articleTitle;
    private String articleImage;
    private String articleType;
    private String articleUrl;
    private String articleContent;
    private String articleDisplay;
}
