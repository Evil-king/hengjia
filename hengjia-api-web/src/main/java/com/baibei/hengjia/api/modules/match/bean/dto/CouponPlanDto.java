package com.baibei.hengjia.api.modules.match.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/8 9:16
 * @description:
 */
@Data
public class CouponPlanDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    private List<MatchPlanDetailDto> matchPlanDetailDtos;
}
