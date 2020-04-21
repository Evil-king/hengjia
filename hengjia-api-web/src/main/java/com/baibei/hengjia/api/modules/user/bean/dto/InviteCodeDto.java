package com.baibei.hengjia.api.modules.user.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/6/4 16:05
 * @description:
 */
@Data
public class InviteCodeDto extends PageParam {
    @NotBlank(message = "交易账号不能为空")
    private String customerNo;
    /**
     * 手机号
     */
    private String mobile;

    private String username;
}
