package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.model.RolesMenus;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: RolesMenus服务接口
 */
public interface IRolesMenusService extends Service<RolesMenus> {

    /**
     * 根据角色ID获取菜单ID集合
     *
     * @param roleId
     * @return
     */
    List<Long> getMenuIdList(Long roleId);

    /**
     * 根据菜单id查询
     */
    List<RolesMenus> getRefByMenuId(Long menuId);

    /**
     * 删除角色对应的菜单
     *
     * @param roleId
     */
    void deleteByRoleMenus(Long roleId);
}
