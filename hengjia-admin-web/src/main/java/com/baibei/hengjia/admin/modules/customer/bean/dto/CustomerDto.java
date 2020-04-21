package com.baibei.hengjia.admin.modules.customer.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/15 16:13
 * @description:
 */
@Data
public class CustomerDto extends PageParam {
    private String customerNo;
    private String username;
    private String customerType;
    private String mobile;
    private String realname;
    private String isPickUp;
    private String status;
    private String memberNo;
    private String startTime;
    private String endTime;
}
