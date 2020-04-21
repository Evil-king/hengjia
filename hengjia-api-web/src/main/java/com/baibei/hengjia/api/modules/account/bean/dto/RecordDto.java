package com.baibei.hengjia.api.modules.account.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/6/5 9:43
 * @description:
 */
@Data
public class RecordDto extends PageParam {
    @NotBlank(message = "交易账号不能为空")
    private String customerNo;
    /**
     * 收入或是支出 1 收入 2 支出
     */
    private Integer retype;

    /**
     * 券类型（vouchers：兑换券 deliveryTicket:提货券）,考虑兼容旧版本安卓和IOS
     */
    private String couponType;
}
