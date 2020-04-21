package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.MenuDTO;
import com.baibei.hengjia.admin.modules.admin.bean.vo.MenuVo;
import com.baibei.hengjia.admin.modules.admin.model.Menu;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Menu服务接口
 */
public interface IMenuService extends Service<Menu> {


    /**
     * permission tree
     *
     * @return
     */
    Object getMenuTree(List<Menu> menus);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    List<Menu> findByPid(long pid);

    /**
     * build Tree
     *
     * @param menuDTOS
     * @return
     */
    Map buildTree(List<MenuDTO> menuDTOS);

    MyPageInfo<MenuDTO> buildTree2(List<MenuDTO> menuDTOS);


    /**
     * findByRoles
     *
     * @param roleIdList
     * @return
     */
    List<MenuDTO> findByRoles(List<Long> roleIdList);

    /**
     * findByRoleId
     *
     * @param roleId
     * @return
     */
    List<MenuDTO> findByRoleId(Long roleId);

    /**
     * buildMenus
     *
     * @param menuDTOS
     * @return
     */
    List<MenuVo> buildMenus(List<MenuDTO> menuDTOS);

    /**
     * getByIds
     *
     * @param menuIdList
     * @return
     */
    List<Menu> getByIds(List<Long> menuIdList);

    /**
     * 根据菜单Id获取菜单信息
     * @param menuIdList
     * @return
     */
    List<MenuDTO> findByMenus(List<Long> menuIdList);


    List<MenuDTO> queryAll(String name);

    void create(Menu menu);

    ApiResult delete(Long id);

}
