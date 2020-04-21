package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.model.RolesPermissions;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: RolesPermissions服务接口
 */
public interface IRolesPermissionsService extends Service<RolesPermissions> {

    List<Long> getPermissionList(List<Long> roleIdList);

    /**
     * 查看某个权限是否有被引用
     *
     * @param permissionId 权限id
     * @return
     */
    List<RolesPermissions> getRefByPermissionId(Long permissionId);

    /**
     * 删除角色对应的权限
     *
     * @param roleId
     */
    void deleteByRolePermissions(Long roleId);
}
