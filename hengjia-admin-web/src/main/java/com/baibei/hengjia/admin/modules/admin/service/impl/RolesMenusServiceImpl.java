package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.dao.RolesMenusMapper;
import com.baibei.hengjia.admin.modules.admin.model.RolesMenus;
import com.baibei.hengjia.admin.modules.admin.service.IRolesMenusService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: RolesMenus服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolesMenusServiceImpl extends AbstractService<RolesMenus> implements IRolesMenusService {

    @Autowired
    private RolesMenusMapper rolesMenusMapper;

    @Override
    public List<Long> getMenuIdList(Long roleId) {
        Condition condition = new Condition(RolesMenus.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        List<RolesMenus> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (RolesMenus rolesMenus : list) {
            result.add(rolesMenus.getMenuId());
        }
        return result;
    }

    @Override
    public List<RolesMenus> getRefByMenuId(Long menuId) {
        Condition condition = new Condition(RolesMenus.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        List<RolesMenus> list = findByCondition(condition);
        return list;
    }

    @Override
    public void deleteByRoleMenus(Long roleId) {
        Condition condition = new Condition(RolesMenus.class);
        condition.createCriteria().andEqualTo("roleId", roleId);
        rolesMenusMapper.deleteByCondition(condition);
    }
}
