package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.dto.ExchangeDto;
import com.baibei.hengjia.api.modules.match.model.ExchangeLog;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;


/**
* @author: Longer
* @date: 2019/08/05 15:48:26
* @description: ExchangeLog服务接口
*/
public interface IExchangeLogService extends Service<ExchangeLog> {

    /**
     * 券兑换商品
     * @param exchangeDto
     * @return
     */
    void exchange(ExchangeDto exchangeDto);

    /**
     * 新增流水
     * @param exchangeLog
     */
    boolean addLogs(ExchangeLog exchangeLog);

    /**
     * 获取某用户当天兑换的数量
     * @param customerNo
     * @param productTradeNo
     * @return
     */
    int getCurrentDayExchangeCount(String customerNo,String productTradeNo);
}
