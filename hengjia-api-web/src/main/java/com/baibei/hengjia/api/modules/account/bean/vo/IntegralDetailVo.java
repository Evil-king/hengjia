package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/6/4 11:32
 * @description:
 */
@Data
public class IntegralDetailVo {
    /**
     * 积分余额
     */
    private BigDecimal balance;
    /**
     * 头像地址
     */
    private String userPicture;
    /**
     * 用户名
     */
    private String username;
}
