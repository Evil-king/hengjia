package com.baibei.hengjia.api.modules.user.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/6/4 18:03
 * @description:
 */
@Data
public class AddressDto extends CustomerNoDto {
    /**
     * 修改时用到
     */
    private Long id;
    /**
     * 详细收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String receivingAddress;
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String receivingName;
    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    private String receivingMobile;
    /**
     * 区
     */
    @NotBlank(message = "区名不能为空")
    private String area;

    /**
     * 市
     */
    @NotBlank(message = "市名不能为空")
    private String city;

    /**
     * 省
     */
    @NotBlank(message = "省名不能为空")
    private String province;
}
