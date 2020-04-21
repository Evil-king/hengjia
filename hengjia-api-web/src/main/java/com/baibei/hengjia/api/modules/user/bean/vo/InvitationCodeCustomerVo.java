package com.baibei.hengjia.api.modules.user.bean.vo;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/6/4 15:33
 * @description:
 */
@Data
public class InvitationCodeCustomerVo extends CustomerNoDto {
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 当天是否交易标识(存在交易则会返回具体交易的商品编号，不存在则会返回null)
     *
     */
    private String tradeFlag;
    /**
     * 真实姓名
     */
    private String realname;

}
