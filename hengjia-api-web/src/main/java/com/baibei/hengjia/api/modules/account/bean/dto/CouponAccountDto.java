package com.baibei.hengjia.api.modules.account.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @Classname CouponAccountVo
 * @Description TODO
 * @Date 2019/8/5 14:59
 * @Created by Longer
 */
@Data
public class CouponAccountDto extends PageParam {
    /**
     * 用户编码
     */
    @NotBlank(message = "用户编码不能为空")
    private String customerNo;

    private Long couponAccountId;

}
