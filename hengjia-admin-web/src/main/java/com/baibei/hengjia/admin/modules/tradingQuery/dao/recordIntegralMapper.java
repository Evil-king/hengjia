package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordIntegral;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface recordIntegralMapper extends MyMapper<RecordIntegral> {
    List<RecordIntegralVo> pageList(RecordIntegralDto recordIntegralDto);

    BigDecimal findByTradetypeAndCustomerNo(@Param("retype") String retype, @Param("customerNo") String customerNo,@Param("startTime")String startTime, @Param("endTime")String endTime);
}