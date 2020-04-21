package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordMoney;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RecordMoneyMapper extends MyMapper<RecordMoney> {
    List<RecordVo> getAll(RecordDto recordDto);

    List<BigDecimal> selectSellOut(String customerNo);

    List<RecordMoney> selectByCustomerNoAndDate(@Param("customerNo") String customerNo, @Param("tradeType") String tradeType, @Param("date") String date);

    List<CustomerCountVo> sumChangeAmount(Map<String,Object> param);
}