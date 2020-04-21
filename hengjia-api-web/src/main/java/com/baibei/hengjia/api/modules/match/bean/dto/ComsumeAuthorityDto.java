package com.baibei.hengjia.api.modules.match.bean.dto;

import lombok.Data;

/**
 * @Classname ComsumeAuthorityDto
 * @Description 消费配货权dto
 * @Date 2019/10/10 9:54
 * @Created by Longer
 */
@Data
public class ComsumeAuthorityDto {
    private String customerNo;

    private String productTradeNo;

    private Integer changeAmount;

    private String orderNo;

    private String remark;
}
