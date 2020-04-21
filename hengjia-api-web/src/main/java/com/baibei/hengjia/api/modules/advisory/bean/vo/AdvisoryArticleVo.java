package com.baibei.hengjia.api.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryArticleVo {
    private String articleTitle;
    private String articleImage;
    private String articleType;
    private String articleUrl;
    private String articleContent;
    private String createTime;
}
