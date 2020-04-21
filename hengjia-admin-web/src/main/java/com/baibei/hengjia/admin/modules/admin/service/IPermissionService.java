package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.PermissionDTO;
import com.baibei.hengjia.admin.modules.admin.model.Permission;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Permission服务接口
 */
public interface IPermissionService extends Service<Permission> {

    List<Permission> listByIds(List<Long> ids);


    List<Permission> findByPid(Long pid);

    Object getPermissionTree(List<Permission> permissions);

    List<PermissionDTO> listByRoleId(Long roleId);

    MyPageInfo<PermissionDTO> buildTree(List<PermissionDTO> permissionDTOS);

    List<PermissionDTO> queryAll(String name);


    void create(Permission resources);

    ApiResult delete(Long id);
}
