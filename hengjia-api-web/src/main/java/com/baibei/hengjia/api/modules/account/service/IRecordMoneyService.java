package com.baibei.hengjia.api.modules.account.service;
import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordMoney;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;
import java.util.Map;


/**
* @author: hyc
* @date: 2019/06/03 14:41:55
* @description: RecordMoney服务接口
*/
public interface IRecordMoneyService extends Service<RecordMoney> {

    MyPageInfo<RecordVo> getAll(RecordDto recordDto);

    List<CustomerCountVo> sumChangeAmount(Map<String,Object> param);
}
