package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.dao.UsersRolesMapper;
import com.baibei.hengjia.admin.modules.admin.model.UsersRoles;
import com.baibei.hengjia.admin.modules.admin.service.IUsersRolesService;
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
 * @description: UsersRoles服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UsersRolesServiceImpl extends AbstractService<UsersRoles> implements IUsersRolesService {

    @Autowired
    private UsersRolesMapper usersRolesMapper;


    @Override
    public List<Long> getRoleIdList(Long userId) {
        Condition condition = new Condition(UsersRoles.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<UsersRoles> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (UsersRoles usersRoles : list) {
            result.add(usersRoles.getRoleId());
        }
        return result;
    }

    @Override
    public void deleteByUserRole(Long userId) {
        Condition condition = new Condition(UsersRoles.class);
        condition.createCriteria().andEqualTo("userId", userId);
        usersRolesMapper.deleteByCondition(condition);
    }
}
