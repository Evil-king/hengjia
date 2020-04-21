package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:51 PM
 * @description:
 */
@Data
public class MyHoldDto extends CustomerBaseAndPageDto {
    private String productTradeNo;
    /**
     * 本票：main
     * 配票：matcx
     */
    private String holdType;
}
