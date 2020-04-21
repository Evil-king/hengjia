package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname DeliveryQueryDto
 * @Description 提货记录查询属性封装
 * @Date 2019/6/6 9:58
 * @Created by Longer
 */
@Data
public class DeliveryQueryDto extends PageParam {

    // 客户编码
    @NotBlank(message = "客户编码不能为空")
    private String customerNo;

    private String startTime;

    private String endTime;

}
