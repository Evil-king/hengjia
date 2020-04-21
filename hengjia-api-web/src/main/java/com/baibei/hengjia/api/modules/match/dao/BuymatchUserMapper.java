package com.baibei.hengjia.api.modules.match.dao;

import com.baibei.hengjia.api.modules.match.bean.vo.BuymatchUsersVo;
import com.baibei.hengjia.api.modules.match.model.BuymatchUser;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BuymatchUserMapper extends MyMapper<BuymatchUser> {

    List<BuymatchUsersVo> selectCustomerNos();

    int updateStatusWithRun(@Param("batchNo") String batchNo, @Param("customerNo") String customerNo,@Param("productTradeNo") String productTradeNo);

}