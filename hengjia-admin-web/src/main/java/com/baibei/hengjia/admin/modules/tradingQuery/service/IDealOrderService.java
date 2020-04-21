package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.DealOrderDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.DealOrderDataStatisticsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DealOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DealOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.DealOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/17 10:48:24
* @description: DealOrder服务接口
*/
public interface IDealOrderService extends Service<DealOrder> {

    MyPageInfo<DealOrderVo> pageList(DealOrderDto dealOrderDto);

    List<DealOrderVo> list(DealOrderDto dealOrderDto);

    BigDecimal findTradeFeeByCustomerNo(String customerNo,String startTime,String endTime);

    BigDecimal findSellMoneyByCustomer(String customerNo, String createTime);

    MyPageInfo<DealOrderDataStatisticsVo> dealOrderPageList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto);

    List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVoList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto);
}
