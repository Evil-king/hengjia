package com.baibei.hengjia.api.modules.settlement.service;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.settlement.model.CleanHelp;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;
import java.util.Map;


/**
* @author: 会跳舞的机器人
* @date: 2019/10/21 14:26:31
* @description: CleanHelp服务接口
*/
public interface ICleanHelpService extends Service<CleanHelp> {

    List<CustomerCountVo> sumLoss(Map<String, Object> param);

    List<CustomerCountVo> sumProfit(Map<String, Object> param);

}
