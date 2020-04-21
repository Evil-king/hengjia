package com.baibei.hengjia.admin.modules.settlement.dao;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.FundDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.FundDataStatisticsVo;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.model.CleanData;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CleanDataMapper extends MyMapper<CleanData> {
    List<CleanData> CleanDataPageVoList(CleanDataPageDto cleanDataPageDto);

    List<FundDataStatisticsVo> fundPageList(FundDataStatisticsDto fundDataStatisticsDto);

    /**
     * 找到当天的期初金额，在代码里实际上就是找昨天的期末金额
     * @param createTime
     * @return
     */
    BigDecimal findTodayInitMoney(@Param("createTime") String createTime,@Param("customerNo") String customerNo);

    List<FundDataStatisticsVo> findByFundDataStatisticsDto(FundDataStatisticsDto fundDataStatisticsDto);

    BigDecimal findInitMoneyAndMonday(@Param("createTime") String createTime,@Param("customerNo") String customerNo);
}