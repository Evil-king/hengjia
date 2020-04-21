package com.baibei.hengjia.admin.config;

import com.baibei.hengjia.admin.modules.admin.model.Dept;
import com.baibei.hengjia.admin.modules.admin.model.Role;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.service.IDeptService;
import com.baibei.hengjia.admin.modules.admin.service.IRoleService;
import com.baibei.hengjia.admin.modules.admin.service.IUserService;
import com.baibei.hengjia.admin.modules.admin.service.IUsersRolesService;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限配置
 *
 * @author jie
 * @date 2019-4-1
 */
@Component
public class DataScope {

    private final String[] scopeType = {"全部", "本级", "自定义"};

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUsersRolesService usersRolesService;

    @Autowired
    private IDeptService deptService;

    public Set<Long> getDeptIds() {

        User user = userService.findByName(SecurityUtils.getUsername());

        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();

        // 查询用户角色
        List<Role> roleList = roleService.listByIds(usersRolesService.getRoleIdList(user.getId()));
        for (Role role : roleList) {
            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>();
            }
            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDeptId());
            }
            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                List<Dept> depts = deptService.getByRoleId(role.getId());
                for (Dept dept : depts) {
                    deptIds.add(dept.getId());
                    List<Dept> deptChildren = deptService.findByPid(dept.getId());
                    if (deptChildren != null && deptChildren.size() != 0) {
                        deptIds.addAll(getDeptChildren(deptChildren));
                    }
                }
            }
        }
        return deptIds;
    }


    public List<Long> getDeptChildren(List<Dept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept != null && dept.getEnabled()) {
                        List<Dept> depts = deptService.findByPid(dept.getId());
                        if (deptList != null && deptList.size() != 0) {
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }
}
