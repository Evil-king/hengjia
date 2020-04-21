package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

/**
 * @author: Longer
 * @date: 2019/6/16 17:51 PM
 * @description:
 */
@Data
public class MyDeliveryHoldDto extends CustomerBaseDto {
    private String productTradeNo;
    /**
     * 本票：main
     * 配票：matcx
     */
    private String holdType;
}
