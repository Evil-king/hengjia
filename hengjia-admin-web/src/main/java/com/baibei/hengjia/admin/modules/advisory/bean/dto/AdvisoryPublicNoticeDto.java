package com.baibei.hengjia.admin.modules.advisory.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AdvisoryPublicNoticeDto {


    private Long id;

    @NotNull(message = "标题不能为空")
    private String title;

    private Integer seq;

    private String link;

    private String content;

    private String hidden;

    private String image;

    /**
     * 类型(mall:商场公告，trade:交易公告)
     */
    private String publicType;

    private Set<String> ids;

    /**
     * 批量操作类型 delete 删除,update 修改
     */
    private String batchType;
}
