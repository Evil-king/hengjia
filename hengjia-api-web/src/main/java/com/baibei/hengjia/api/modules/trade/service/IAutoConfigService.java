package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.bean.dto.AutoTradeOperateDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.AutoTradeSaveDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradePageVo;
import com.baibei.hengjia.api.modules.trade.model.AutoConfig;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoConfig服务接口
 */
public interface IAutoConfigService extends Service<AutoConfig> {

    AutoConfig findByCustomerNo(String customerNo);

    ApiResult<AutoTradePageVo> getPageInfo(String customerNo);

    ApiResult saveConfig(AutoTradeSaveDto autoTradeSaveDto);

    ApiResult operate(AutoTradeOperateDto autoTradeSaveDto);

    void closingTime();

    ApiResult trade();

    List<AutoConfig> getValidList();

}
