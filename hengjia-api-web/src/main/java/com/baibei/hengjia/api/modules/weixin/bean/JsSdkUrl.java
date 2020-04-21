package com.baibei.hengjia.api.modules.weixin.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/18 4:49 PM
 * @description:
 */
@Data
public class JsSdkUrl {
    @NotBlank(message = "url不能为空")
    private String url;
}
