package com.baibei.hengjia.api.modules.match.bean.vo;

import lombok.Data;

/**
 * @Classname BuymatchUsersVo
 * @Description 符合买入配货的用户信息
 * @Date 2019/8/5 14:17
 * @Created by Longer
 */
@Data
public class BuymatchUsersVo {
    private String customerNo;
    /**
     * 商品交易编码
     */
    private String productTradeNo;

    private String type;

    private String holdType;
}
