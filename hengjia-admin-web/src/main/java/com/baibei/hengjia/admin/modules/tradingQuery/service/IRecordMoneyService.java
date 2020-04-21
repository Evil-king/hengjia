package com.baibei.hengjia.admin.modules.tradingQuery.service;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordMoney;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/07/15 10:10:53
 * @description: recordMoney服务接口
 */
public interface IRecordMoneyService extends Service<RecordMoney> {

    MyPageInfo<RecordMoneyVo> pageList(RecordMoneyDto recordMoneyDto);

    List<RecordMoneyVo> RecordMoneyVoList(RecordMoneyDto recordMoneyDto);

    BigDecimal findSumByDateAndCustomerNoAndTradeType(String customerNo, String tradeType, String startTime, String endTime);

}
