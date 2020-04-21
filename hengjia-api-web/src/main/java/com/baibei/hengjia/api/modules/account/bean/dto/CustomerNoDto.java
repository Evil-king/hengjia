package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: hyc
 * @date: 2019/5/24 3:49 PM
 * @description:
 */
@Data
public class CustomerNoDto implements Serializable {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;

}
