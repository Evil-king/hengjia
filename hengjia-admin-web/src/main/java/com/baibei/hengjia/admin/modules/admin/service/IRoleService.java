package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.RoleDTO;
import com.baibei.hengjia.admin.modules.admin.model.Role;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;

import javax.servlet.ServletException;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Role服务接口
 */
public interface IRoleService extends Service<Role> {

    /**
     * 根据角色ID获取角色信息
     *
     * @param roleIdList
     * @return
     */
    List<Role> listByIds(List<Long> roleIdList);

    MyPageInfo<RoleDTO> pageList(RoleDTO roleDTO);


    MyPageInfo<Role> rolePageList(PageParam pageParam);

    /**
     * 判断角色唯一性
     *
     * @return
     */
    Boolean findOnlyRoleName(String roleName);

    /**
     * 获取角色的信息
     *
     * @param roleId
     * @return
     */
    RoleDTO getRole(Long roleId);

    /**
     * 查询用户的角色
     *
     * @param userId
     * @return
     */
    List<Role> findByUserRole(Long userId);

    /**
     * 创建角色
     *
     * @param role
     * @return
     */
    Role create(Role role) throws ServletException;

    /**
     * 增加权限
     *
     * @param roleDTO
     * @param role
     */
    void updatePermission(RoleDTO roleDTO, Role role) throws ServletException;

    /**
     * 增加菜单
     *
     * @param roleDTO
     * @param role
     */
    void updateMenu(RoleDTO roleDTO, Role role) throws ServletException;
}
