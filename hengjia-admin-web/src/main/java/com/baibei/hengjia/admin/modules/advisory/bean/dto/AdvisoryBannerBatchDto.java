package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdvisoryBannerBatchDto {
    private List<String> idList;
    private String type;
}
