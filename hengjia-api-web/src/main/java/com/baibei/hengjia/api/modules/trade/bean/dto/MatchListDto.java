package com.baibei.hengjia.api.modules.trade.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Classname MatchListDto
 * @Description 配票dto
 * @Date 2019/6/10 11:01
 * @Created by Longer
 */
@Data
public class MatchListDto {
    private List<MatchDto> matchDtoList;
}
