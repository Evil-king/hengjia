package com.baibei.hengjia.api.modules.user.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/6/14 15:14
 * @description:
 */
@Data
public class FindAddressDto extends CustomerNoDto{
    @NotNull(message = "地址ID不能为空")
    private Long id;
}
