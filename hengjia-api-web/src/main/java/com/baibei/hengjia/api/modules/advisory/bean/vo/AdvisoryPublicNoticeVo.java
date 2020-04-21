package com.baibei.hengjia.api.modules.advisory.bean.vo;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AdvisoryPublicNoticeVo extends CustomerBaseDto {
    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String content;

    private String image;

    private String link;

    private String hidden;

    private Integer seq;
}
