package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/15 15:08
 * @description:
 */
@Data
public class MyHoldNewDto extends CustomerBaseAndPageDto {
    @NotBlank(message = "类型不能为空")
    private String type;
}