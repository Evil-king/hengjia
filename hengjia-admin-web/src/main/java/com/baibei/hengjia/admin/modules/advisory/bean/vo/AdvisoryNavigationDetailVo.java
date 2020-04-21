package com.baibei.hengjia.admin.modules.advisory.bean.vo;

import lombok.Data;

@Data
public class AdvisoryNavigationDetailVo {
    private long id;
    private String typeId;
    private String title;
    private String image;
    private String type;
    private String url;
    private String sort;
    private String createTime;
    private String modifyTime;
}
