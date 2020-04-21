package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 14:28
 * @description:
 */
@Data
public class AutoTradeOperateDto extends CustomerBaseDto {
    @NotBlank(message = "状态不能为空")
    private String status;
}