package com.baibei.hengjia.scheduler.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname ControlSwitchDto
 * @Description 操作开关dto
 * @Date 2019/8/8 14:42
 * @Created by Longer
 */
@Data
public class ControlSwitchDto {
    /**
     * 开关状态(on:开启；off:关闭)
      */
    private String swtch;

    /**
     * 开关类型(tradeMatch:交易配票；deliveryMatch:提货配票；buyMatch=买入配货)
     */
    private String switchType;
}
