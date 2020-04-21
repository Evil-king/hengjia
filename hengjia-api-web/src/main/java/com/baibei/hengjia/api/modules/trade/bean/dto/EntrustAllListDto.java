package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:35 PM
 * @description:
 */
@Data
public class EntrustAllListDto extends CustomerBaseAndPageDto {
    @NotBlank(message = "方向不能为空")
    private String direction;
    private String productName;
    private String productTradeNo;

    private String transferType;
}
