package com.baibei.hengjia.admin.modules.tradingQuery.service;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordIntegral;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/15 10:10:32
* @description: recordIntegral服务接口
*/
public interface IRecordIntegralService extends Service<RecordIntegral> {

    MyPageInfo<RecordIntegralVo> pageList(RecordIntegralDto recordIntegralDto);

    List<RecordIntegralVo> RecordIntegralVoList(RecordIntegralDto recordIntegralDto);

    BigDecimal findByTradetypeAndCustomerNo(String retype, String customerNo, String startTime, String endTime);
}
