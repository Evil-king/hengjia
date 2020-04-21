package com.baibei.hengjia.api.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryBannerVo {
    private String bannerTitle;
    private String url;
    private String linkUrl;
    private int bannerSort;
    private String bannerContent;
    private String createTime;
}
