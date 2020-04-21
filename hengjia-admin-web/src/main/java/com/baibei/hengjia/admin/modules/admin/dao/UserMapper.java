package com.baibei.hengjia.admin.modules.admin.dao;

import com.baibei.hengjia.admin.modules.admin.bean.dto.UserPageDto;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    List<User> findByUser(UserPageDto userPageDto);
}