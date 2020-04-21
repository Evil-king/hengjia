package com.baibei.hengjia.api.modules.user.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: hyc
 * @date: 2019/5/29 9:52
 * @description:
 */
@Data
public class MobileDto implements Serializable {
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    /**
     * 1：注册,2,忘记密码，5 忘记资金密码
     */
    @NotBlank(message = "短信类型不能为空")
    private String type;
}
