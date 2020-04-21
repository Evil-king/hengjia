package com.baibei.hengjia.admin.modules.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baibei.hengjia.admin.modules.admin.bean.dto.MenuDTO;
import com.baibei.hengjia.admin.modules.admin.bean.vo.MenuMetaVo;
import com.baibei.hengjia.admin.modules.admin.bean.vo.MenuVo;
import com.baibei.hengjia.admin.modules.admin.dao.MenuMapper;
import com.baibei.hengjia.admin.modules.admin.model.Menu;
import com.baibei.hengjia.admin.modules.admin.model.RolesMenus;
import com.baibei.hengjia.admin.modules.admin.service.IMenuService;
import com.baibei.hengjia.admin.modules.admin.service.IRolesMenusService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Menu服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends AbstractService<Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private IRolesMenusService rolesMenusService;

    @Override
    public Object getMenuTree(List<Menu> menus) {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu != null) {
                        List<Menu> menuList = findByPid(menu.getId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", menu.getId());
                        map.put("label", menu.getName());
                        if (menuList != null && menuList.size() != 0) {
                            map.put("children", getMenuTree(menuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Menu> findByPid(long pid) {
        Condition condition = new Condition(Menu.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid", pid);
        return findByCondition(condition);
    }

    @Override
    public Map buildTree(List<MenuDTO> menuDTOS) {
        List<MenuDTO> trees = new ArrayList<MenuDTO>();

        for (MenuDTO menuDTO : menuDTOS) {

            if ("0".equals(menuDTO.getPid().toString())) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDTOS) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<MenuDTO>());
                    }
                    menuDTO.getChildren().add(it);
                }
            }
        }
        Map map = new HashMap();
        map.put("content", trees.size() == 0 ? menuDTOS : trees);
        map.put("totalElements", menuDTOS != null ? menuDTOS.size() : 0);
        return map;
    }

    @Override
    public MyPageInfo<MenuDTO> buildTree2(List<MenuDTO> menuDTOS) {
        List<MenuDTO> trees = new ArrayList<MenuDTO>();

        for (MenuDTO menuDTO : menuDTOS) {

            if ("0".equals(menuDTO.getPid().toString())) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDTOS) {
                if (it.getPid().toString().equals(menuDTO.getId().toString())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<MenuDTO>());
                    }
                    menuDTO.getChildren().add(it);
                }
            }
        }
        return new MyPageInfo<>(trees.size() == 0 ? menuDTOS : trees);
    }

    @Override
    public List<MenuDTO> findByRoles(List<Long> roleIdList) {
        List<Menu> menus = new LinkedList<>();
        List<Long> menuIdList = null;
        for (Long roleId : roleIdList) {
            menuIdList = rolesMenusService.getMenuIdList(roleId);
            List<Menu> menus1 = getByIds(menuIdList);
            menus.addAll(menus1);
        }
        return BeanUtil.copyProperties(menus, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> findByRoleId(Long roleId) {
        return findByMenus(rolesMenusService.getMenuIdList(roleId));
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOS) {
        List<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDTO> menuDTOList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(menuDTO.getName());
                        menuVo.setPath(menuDTO.getPath());

                        // 如果不是外链
                        if (!menuDTO.getIFrame()) {
                            if (menuDTO.getPid().equals(0L)) {
                                //一级目录需要加斜杠，不然访问 会跳转404页面
                                menuVo.setPath("/" + menuDTO.getPath());
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon()));
                        if (menuDTOList != null && menuDTOList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDTOList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid().equals(0L)) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<MenuVo>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Menu> getByIds(List<Long> menuIdList) {
        if (!CollectionUtils.isEmpty(menuIdList)) {
            Condition condition = new Condition(Menu.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andIn("id", menuIdList);
            return  findByCondition(condition);
        }
        return new ArrayList<>();
    }

    @Override
    public List<MenuDTO> findByMenus(List<Long> menuIdList) {
        List<Menu> menusList = getByIds(menuIdList);
        return BeanUtil.copyProperties(menusList, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> queryAll(String name) {
        Condition condition = new Condition(Menu.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        return BeanUtil.copyProperties(findByCondition(condition), MenuDTO.class);
    }

    @Override
    public void create(Menu menu) {
        menu.setCreateTime(new Date());
        save(menu);
    }

    @Override
    public ApiResult delete(Long id) {
        List<Menu> menuList = this.findByPid(id);
        if (menuList.size() > 0) {
            throw new ServiceException("删除失败，存在下级菜单");
        }
        //查询菜单是否被角色引用
        List<RolesMenus> refByMenuId = rolesMenusService.getRefByMenuId(id);
        if (refByMenuId.size() > 0) {
            throw new ServiceException("删除失败，该菜单已被引用");
        }
        this.deleteById(id);
        return ApiResult.success();
    }
}
