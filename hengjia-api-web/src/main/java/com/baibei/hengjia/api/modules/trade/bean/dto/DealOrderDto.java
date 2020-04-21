package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/6/6 13:48
 * @description:
 */
@Data
public class DealOrderDto extends PageParam {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
    private String startTime;
    private String endTime;
}
