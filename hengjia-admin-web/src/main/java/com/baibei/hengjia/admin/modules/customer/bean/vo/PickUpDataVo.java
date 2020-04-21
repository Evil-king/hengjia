package com.baibei.hengjia.admin.modules.customer.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/18 17:34
 * @description:
 */
@Data
public class PickUpDataVo {

    /**
     * 收货人姓名
     */
    private String receivingName;
    /**
     * 收货人地址
     */
    private String receivingAddress;
    /**
     * 收货人联系电话
     */
    private String mobile;
}
