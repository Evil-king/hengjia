package com.baibei.hengjia.admin.modules.admin.web;

import cn.hutool.core.lang.Dict;
import com.baibei.hengjia.admin.modules.admin.bean.dto.RoleDTO;
import com.baibei.hengjia.admin.modules.admin.model.Role;
import com.baibei.hengjia.admin.modules.admin.service.IRoleService;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jie
 * @date 2018-12-03
 */
@RestController
@RequestMapping("api")
public class RoleController {

    @Autowired
    private IRoleService roleService;


    private static final String ENTITY_NAME = "role";


    /**
     * 获取单个role
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ResponseEntity getRoles(@PathVariable Long id) {
        return new ResponseEntity(roleService.getRole(id), HttpStatus.OK);
    }

    /**
     * 获取用户的拥有的角色
     *
     * @param pageParam
     * @return
     */
    @GetMapping(value = "/roles/all")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    public ResponseEntity getAll(PageParam pageParam) {
        if (pageParam.getCurrentPage() == null) {
            pageParam.setCurrentPage(1);
        }
        if (pageParam.getPageSize() == null) {
            pageParam.setPageSize(2000);
        }
        MyPageInfo<Role> roleDTOMyPageInfo = roleService.rolePageList(pageParam);//获取用户拥有的角色
        return new ResponseEntity(roleDTOMyPageInfo.getList(), HttpStatus.OK);
    }

    /**
     * 返回全部的角色，新增用户时下拉选择
     *
     * @param pageable
     * @return
     */
    /*@GetMapping(value = "/roles/all")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    public ResponseEntity getAll(@PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable) {

        return new ResponseEntity(roleQueryService.queryAll(pageable), HttpStatus.OK);
    }*/

    //@Log("查询角色")
    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ApiResult<MyPageInfo<RoleDTO>> getRoles(RoleDTO roleDTO) {
        return ApiResult.success(roleService.pageList(roleDTO));
    }

    /**
     * 角色等级
     *
     * @return
     */
    @GetMapping(value = "/roles/level")
    public ResponseEntity getLevel() {
        List<Integer> levels = roleService.findByUserRole(SecurityUtils.getUserId()).stream().map(Role::getLevel).collect(Collectors.toList());
        return new ResponseEntity(Dict.create().set("level", Collections.min(levels)), HttpStatus.OK);
    }

    /**
     * 创建角色
     *
     * @param resources
     * @return
     */
    @PostMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    public ApiResult create(@Validated @RequestBody Role resources) {
        if (resources.getId() != null) {
            throw new ServiceException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        Role role;
        try {
            role = roleService.create(resources);
        } catch (ServletException e) {
            e.printStackTrace();
            return ApiResult.error(e.getMessage());
        }
        return ApiResult.success(role);
    }

    /**
     * 修改角色
     *
     * @param resources
     * @return
     */
    @PutMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResult update(@Validated @RequestBody Role resources) {
        if (!this.roleService.findOnlyRoleName(resources.getName())) {
            return ApiResult.error("角色名已经存在");
        }
        roleService.update(resources);
        return ApiResult.success();
    }

    /**
     * 修改角色的权限
     *
     * @param resources
     * @return
     */
    @PutMapping(value = "/roles/permission")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResult updatePermission(@RequestBody RoleDTO resources) {
        try {
            roleService.updatePermission(resources, roleService.findById(resources.getId()));
        } catch (ServletException e) {
            e.printStackTrace();
            ApiResult.error(e.getMessage());
        }
        return ApiResult.success();
    }


    /**
     * 修改角色的菜单
     *
     * @param resources
     * @return
     */
    @PutMapping(value = "/roles/menu")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResult updateMenu(@RequestBody RoleDTO resources) {
        try {
            roleService.updateMenu(resources, roleService.findById(resources.getId()));
        } catch (ServletException e) {
            e.printStackTrace();
            return ApiResult.error(e.getMessage());
        }
        return ApiResult.success();
    }


    /*@Log("新增角色")


    }

    @Log("删除角色")
    @DeleteMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        roleService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }*/
}
