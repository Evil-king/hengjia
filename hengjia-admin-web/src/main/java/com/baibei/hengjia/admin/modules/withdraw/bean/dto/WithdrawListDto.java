package com.baibei.hengjia.admin.modules.withdraw.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class WithdrawListDto extends PageParam {

    private String orderNo;
    private String customerNo;
    private String userName;
    private String mobile;
    private String status;
    private String memberNo;
    private String startTime;
    private String endTime;
    private String externalNo;
    private String effective;
    private String reviewer;
}
