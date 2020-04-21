package com.baibei.hengjia.admin.modules.admin.web;


import com.baibei.hengjia.admin.modules.admin.bean.dto.MenuDTO;
import com.baibei.hengjia.admin.modules.admin.model.Menu;
import com.baibei.hengjia.admin.modules.admin.model.RolesMenus;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.service.*;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jie
 * @date 2018-12-03
 */
@RestController
@RequestMapping("api")
public class MenuController {

    @Autowired
    private IMenuService menuService;

   /* @Autowired
    private IMenuQueryService menuQueryService;*/

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUsersRolesService usersRolesService;
    @Autowired
    private IRolesMenusService rolesMenusService;
/*
    @Autowired
    private IMenuMapper menuMapper;*/

    private static final String ENTITY_NAME = "menu";

    /**
     * 构建前端路由所需要的菜单
     *
     * @return
     */
    @GetMapping(value = "/menus/build")
    public ResponseEntity buildMenus() {
        User user = userService.findByName(SecurityUtils.getUsername());
        List<MenuDTO> menuDTOList = menuService.findByRoles(usersRolesService.getRoleIdList(user.getId()));
        List<MenuDTO> menuDTOTree = (List<MenuDTO>) menuService.buildTree(menuDTOList).get("content");
        return new ResponseEntity(menuService.buildMenus(menuDTOTree), HttpStatus.OK);
    }

    /**
     * 返回全部的菜单
     *
     * @return
     */
    @GetMapping(value = "/menus/tree")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE','MENU_EDIT','ROLES_SELECT','ROLES_ALL')")
    public ResponseEntity getMenuTree() {
        return new ResponseEntity(menuService.getMenuTree(menuService.findByPid(0L)), HttpStatus.OK);
    }

    //@Log("查询菜单")
    @GetMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ApiResult<MyPageInfo<MenuDTO>> getMenus(@RequestParam(required = false) String name) {
        List<MenuDTO> menuDTOList = menuService.queryAll(name);
        return ApiResult.success(menuService.buildTree2(menuDTOList));
    }


    //@Log("新增菜单")
    @PostMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE')")
    public ApiResult create(@Validated @RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new ServiceException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        resources.setiFrame(false);
        menuService.create(resources);
        return ApiResult.success();
    }

    @PutMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ApiResult update(@Validated @RequestBody Menu resources){
        menuService.update(resources);
        return ApiResult.success();
    }

    @DeleteMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public ApiResult delete(@PathVariable Long id){
        return  menuService.delete(id);
    }

    /*
    @Log("修改菜单")
    @PutMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResponseEntity update(@Validated(Menu.Update.class) @RequestBody Menu resources){
        menuService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除菜单")
    @DeleteMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        List<Menu> menuList = menuService.findByPid(id);

        // 特殊情况，对级联删除进行处理
        for (Menu menu : menuList) {
            roleService.untiedMenu(menu);
            menuService.delete(menu.getId());
        }
        roleService.untiedMenu(menuService.findOne(id));
        menuService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }*/
}
