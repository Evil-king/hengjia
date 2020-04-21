package com.baibei.hengjia.admin.modules.settlement.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AmountReturnDto extends PageParam {
    private String batchNo;
    private String customerNo;
    private String status;
    private String type;
}
