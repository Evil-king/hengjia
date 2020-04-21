package com.baibei.hengjia.admin.modules.admin.dao;

import com.baibei.hengjia.admin.modules.admin.model.Role;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    List<Role> findByUserRole(Long userId);
}