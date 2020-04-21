package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.dao.RolesDeptsMapper;
import com.baibei.hengjia.admin.modules.admin.model.RolesDepts;
import com.baibei.hengjia.admin.modules.admin.service.IRolesDeptsService;
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
 * @description: RolesDepts服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolesDeptsServiceImpl extends AbstractService<RolesDepts> implements IRolesDeptsService {

    @Autowired
    private RolesDeptsMapper rolesDeptsMapper;

    @Override
    public List<Long> getDeptIdList(Long roleId) {
        Condition condition = new Condition(RolesDepts.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        List<RolesDepts> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (RolesDepts rolesDepts : list) {
            result.add(rolesDepts.getDeptId());
        }
        return result;
    }
}
