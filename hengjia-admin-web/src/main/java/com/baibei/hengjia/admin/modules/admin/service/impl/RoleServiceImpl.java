package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.admin.bean.dto.RoleDTO;
import com.baibei.hengjia.admin.modules.admin.dao.RoleMapper;
import com.baibei.hengjia.admin.modules.admin.model.Role;
import com.baibei.hengjia.admin.modules.admin.model.RolesMenus;
import com.baibei.hengjia.admin.modules.admin.model.RolesPermissions;
import com.baibei.hengjia.admin.modules.admin.service.*;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Role服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRolesMenusService rolesMenusService;

    @Autowired
    private IRolesPermissionsService rolesPermissionsService;

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public List<Role> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Condition condition = new Condition(Role.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("id", ids);
        return findByCondition(condition);
    }

    @Override
    public MyPageInfo<RoleDTO> pageList(RoleDTO roleDTO) {
        Condition condition = new Condition(Role.class);
        Example.Criteria criteria = condition.createCriteria();
        if (roleDTO.getName() != null) {
            criteria.andLike("name", roleDTO.getName());
        }
        MyPageInfo<Role> list = pageList(condition, roleDTO);
        MyPageInfo<RoleDTO> myPageInfo = PageUtil.transform(list, RoleDTO.class);
        // 设置角色对应的菜单和操作权限数据
        if (myPageInfo.getList() != null && !myPageInfo.getList().isEmpty()) {
            for (RoleDTO role : myPageInfo.getList()) {
                role.setMenus(menuService.findByRoleId(role.getId()));
                role.setPermissions(permissionService.listByRoleId(role.getId()));
            }
        }
        return myPageInfo;
    }

    @Override
    public MyPageInfo<Role> rolePageList(PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrentPage(), pageParam.getPageSize());
        List<Role> roles = this.findAll();
        MyPageInfo<Role> pageInfo = new MyPageInfo<>(roles);
        return pageInfo;
    }

    /**
     * 如果用户修改的数据存在唯一性
     *
     * @return
     */
    @Override
    public Boolean findOnlyRoleName(String roleName) {
        Condition condition = new Condition(Role.class);
        condition.createCriteria().andEqualTo("name", roleName);
        List<Role> roleList = this.findByCondition(condition);
        if (roleList.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public RoleDTO getRole(Long roleId) {
        Role role = this.findById(roleId);
        RoleDTO roleDTO = BeanUtil.copyProperties(role, RoleDTO.class);
        roleDTO.setMenus(menuService.findByRoleId(roleDTO.getId()));
        roleDTO.setPermissions(permissionService.listByRoleId(roleDTO.getId()));
        return roleDTO;
    }

    @Override
    public List<Role> findByUserRole(Long userId) {
        return roleMapper.findByUserRole(userId);
    }

    @Override
    public Role create(Role role) throws ServletException {
        Condition condition = new Condition(Role.class);
        condition.createCriteria().andEqualTo("name", role.getName());
        Role result = this.findOneByCondition(condition);
        if (result != null) {
            throw new ServletException("角色名称已经存在");
        }
        role.setCreateTime(new Date());
        this.save(role);
        return role;
    }

    @Override
    public void updatePermission(RoleDTO roleDTO, Role role) throws ServletException {
        logger.info("修改角色权限开始");
        if (role == null || role.getId() == null) {
            throw new ServletException("角色Id不能为空");
        }
        // stop 1 删除角色对应的权限
        this.rolesPermissionsService.deleteByRolePermissions(role.getId());
        List<RolesPermissions> rolesPermissionsList = new ArrayList<>();
        logger.info("roleDTO is {},role Id is {}", JSONObject.toJSONString(roleDTO), role.getId());
        roleDTO.getPermissions().stream().forEach(result -> {
            RolesPermissions rolesPermissions = new RolesPermissions();
            rolesPermissions.setPermissionId(result.getId());
            rolesPermissions.setRoleId(role.getId());
            rolesPermissionsList.add(rolesPermissions);
        });
        if (!rolesPermissionsList.isEmpty()) {
            this.rolesPermissionsService.save(rolesPermissionsList);
        }
        logger.info("修改角色权限结束");
    }

    @Override
    public void updateMenu(RoleDTO roleDTO, Role role) throws ServletException {
        logger.info("修改角色菜单开始");
        if (role == null || role.getId() == null) {
            throw new ServletException("角色Id不能为空");
        }
        logger.info("roleDTO is {},role Id is {}", JSONObject.toJSONString(roleDTO), role.getId());
        // stop 1 删除角色对应的菜单
        this.rolesMenusService.deleteByRoleMenus(role.getId());
        List<RolesMenus> rolesMenusList = new ArrayList<>();
        roleDTO.getMenus().stream().forEach(result -> {
            RolesMenus rolesMenus = new RolesMenus();
            rolesMenus.setRoleId(role.getId());
            rolesMenus.setMenuId(result.getId());
            rolesMenusList.add(rolesMenus);
        });
        if (!rolesMenusList.isEmpty()) {
            this.rolesMenusService.save(rolesMenusList);
        }
        logger.info("修改角色菜单结束");
    }
}
