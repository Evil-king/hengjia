package com.baibei.hengjia.admin.modules.settlement.service;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.FundDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.FundDataStatisticsVo;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.model.CleanData;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/08 16:02:12
 * @description: CleanData服务接口
 */
public interface ICleanDataService extends Service<CleanData> {

    MyPageInfo<CleanDataPageVo> pageList(CleanDataPageDto cleanDataPageDto);

    List<CleanDataPageVo> CleanDataPageVoList(CleanDataPageDto cleanDataPageDto);

    /**
     * 资金统计接口（需要查询期初金额）
     * @param fundDataStatisticsDto
     * @return
     */
    MyPageInfo<FundDataStatisticsVo> fundPageList(FundDataStatisticsDto fundDataStatisticsDto);

    List<FundDataStatisticsVo> fundDataStatisticsVoList(FundDataStatisticsDto fundDataStatisticsDto);
}
