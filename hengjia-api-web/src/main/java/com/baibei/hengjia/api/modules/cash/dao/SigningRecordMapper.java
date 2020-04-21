package com.baibei.hengjia.api.modules.cash.dao;

import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SigningRecordMapper extends MyMapper<SigningRecord> {
    List<String> findSupAcctId();

    /**
     * 查询当天用户是否签约
     * @return
     */
    List<SigningRecord> findTodaySigning(@Param("customerNo") String customerNo);
}