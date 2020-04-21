package com.baibei.hengjia.admin.modules.admin.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author jie
 * @date 2018-11-23
 */
@Data
public class RoleDTO extends PageParam implements Serializable {

    private Long id;

    private String name;

    private String dataScope;

    private Integer level;

    private String remark;

    private List<PermissionDTO> permissions;

    private List<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private Timestamp createTime;
}
