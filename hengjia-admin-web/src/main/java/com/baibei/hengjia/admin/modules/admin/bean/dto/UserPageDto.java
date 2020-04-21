package com.baibei.hengjia.admin.modules.admin.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/31 10:07 PM
 * @description:
 */
@Data
public class UserPageDto extends PageParam {

    private Long deptId;

    private Long enabled;

    private String username;

    private String email;
}
