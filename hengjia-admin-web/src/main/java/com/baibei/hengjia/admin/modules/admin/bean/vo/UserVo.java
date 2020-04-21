package com.baibei.hengjia.admin.modules.admin.bean.vo;

import com.baibei.hengjia.admin.modules.admin.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/31 10:18 PM
 * @description:
 */
@Data
public class UserVo {
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private String phone;

    private Long enabled;

    private String password;

    private Date createTime;

    private Date lastPasswordResetTime;

    private Long deptId;

    private String deptName;

    private List<Role> roles;
}
