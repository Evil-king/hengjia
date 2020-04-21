package com.baibei.hengjia.admin.modules.security.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.UserDTO;
import com.baibei.hengjia.admin.modules.admin.model.Permission;
import com.baibei.hengjia.admin.modules.admin.service.IPermissionService;
import com.baibei.hengjia.admin.modules.admin.service.IRoleService;
import com.baibei.hengjia.admin.modules.admin.service.IRolesPermissionsService;
import com.baibei.hengjia.admin.modules.admin.service.IUsersRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "role")
public class JwtPermissionService {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUsersRolesService usersRolesService;
    @Autowired
    private IRolesPermissionsService rolesPermissionsService;
    @Autowired
    private IPermissionService permissionService;


    //@Cacheable(key = "'loadPermissionByUser:' + #p0.username")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");

        List<Permission> permissionList = getPermission(user.getId());
        if (permissionList == null) {
            return new ArrayList<>();
        }
        return permissionList.stream().map(permission1 -> new SimpleGrantedAuthority(permission1.getName())).collect(Collectors.toList());
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    private List<Permission> getPermission(Long userId) {
        // 查询用户角色ID列表
        List<Long> roleIdList = usersRolesService.getRoleIdList(userId);
        // 查询角色有的权限列表
        List<Long> permissionIdList = rolesPermissionsService.getPermissionList(roleIdList);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            return permissionService.listByIds(permissionIdList);
        }
        return null;
    }
}
