package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class HoldTotalDto extends PageParam {

    private String customerNo;

    private String phoneNumber;

    private String productTradeNo;
}
