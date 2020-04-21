package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.dao.RolesPermissionsMapper;
import com.baibei.hengjia.admin.modules.admin.model.RolesPermissions;
import com.baibei.hengjia.admin.modules.admin.service.IRolesPermissionsService;
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
 * @description: RolesPermissions服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolesPermissionsServiceImpl extends AbstractService<RolesPermissions> implements IRolesPermissionsService {

    @Autowired
    private RolesPermissionsMapper rolesPermissionsMapper;

    @Override
    public List<Long> getPermissionList(List<Long> roleIdList) {
        Condition condition = new Condition(RolesPermissions.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("roleId", roleIdList);
        List<RolesPermissions> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (RolesPermissions rolesPermissions : list) {
            result.add(rolesPermissions.getPermissionId());
        }
        return result;
    }

    @Override
    public List<RolesPermissions> getRefByPermissionId(Long permissionId) {
        Condition condition = new Condition(RolesPermissions.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("permissionId", permissionId);
        List<RolesPermissions> list = findByCondition(condition);
        return list;
    }

    @Override
    public void deleteByRolePermissions(Long roleId) {
        Condition condition = new Condition(RolesPermissions.class);
        condition.createCriteria().andEqualTo("roleId", roleId);
        rolesPermissionsMapper.deleteByCondition(condition);
    }
}
