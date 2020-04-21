package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


/**
 * @Classname RevokeDto
 * @Description 撤单
 * @Date 2019/6/6 14:18
 * @Created by Longer
 */
@Getter
@Setter
public class RevokeDto extends CustomerBaseDto {
    /*
        委托单号
     */
    @NotBlank(message = "未指定委托单号")
    private String entrustNo;
}
