package com.baibei.hengjia.admin.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryBannerVo {
    private long id;
    private String bannerTitle;
    private String bannerImage;
    private String bannerUrl;
    private String bannerSort;
    private String bannerContent;
    private String bannerDisplay;
    private String createTime;
    private String modifyTime;
}
