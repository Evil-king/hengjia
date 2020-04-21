package com.baibei.hengjia.api.modules.user.bean.vo;

        import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/6/6 11:22
 * @description:
 */
@Data
public class CustomerTokenVo extends CustomerVo{
    /**
     * 用户token
     */
    private String accessToken;
    /**
     * 刷新用户token的token
     */
    private String refreshToken;
}
