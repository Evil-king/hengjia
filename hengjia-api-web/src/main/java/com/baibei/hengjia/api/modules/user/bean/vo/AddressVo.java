package com.baibei.hengjia.api.modules.user.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/6/4 16:44
 * @description:
 */
@Data
public class AddressVo {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 详细收货地址
     */
    private String receivingAddress;
    /**
     * 收货人姓名
     */
    private String receivingName;
    /**
     * 收货人手机号
     */
    private String receivingMobile;
    /**
     * 是否默认地址
     */
    private Boolean defaultAddress;
    /**
     * 区
     */
    private String area;

    /**
     * 市
     */
    private String city;

    /**
     * 省
     */
    private String province;
}
