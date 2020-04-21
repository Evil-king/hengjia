package com.baibei.hengjia.api.modules.match.dao;

import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchAuthorityMapper extends MyMapper<MatchAuthority> {
    int updateAuthority(@Param("customerNo") String customerNo,@Param("productTradeNo") String productTradeNo);

    List<String> selectCustomerListWithAuthorityZero();

}