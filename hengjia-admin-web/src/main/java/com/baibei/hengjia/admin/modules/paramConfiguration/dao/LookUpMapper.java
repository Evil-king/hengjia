package com.baibei.hengjia.admin.modules.paramConfiguration.dao;

import com.baibei.hengjia.admin.modules.paramConfiguration.model.LookUp;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface LookUpMapper extends MyMapper<LookUp> {
    int deleteById(long id);

    int selectByName(@Param("name") String name);
}