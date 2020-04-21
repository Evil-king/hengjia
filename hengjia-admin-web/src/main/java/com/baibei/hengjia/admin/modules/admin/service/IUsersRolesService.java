package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.model.UsersRoles;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: UsersRoles服务接口
 */
public interface IUsersRolesService extends Service<UsersRoles> {


    /**
     * 获取用户的角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> getRoleIdList(Long userId);

    /**
     * 根据用户对应的角色
     *
     * @param userId
     */
    void deleteByUserRole(Long userId);
}
