package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.MatchConfig;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: Longer
* @date: 2019/06/05 10:46:05
* @description: 配票开关
*/
public interface IMatchConfigService extends Service<MatchConfig> {

    /**
     * 获取配票开关
     * switchType:开关类型
     * @return
     */
    MatchConfig getSwitch(String switchType);

    /**
     * 操控开关
     * @param swtch 开关状态(on:开启；off:关闭)
     * @param switchType 开关类型(tradeMatch:交易配票；deliveryMatch:提货配票)
     */
    int matchSwitch(String swtch,String switchType);


    /**
     * 操控开关。只有在交易日，并且不是交易时间，才可以操作开关
     * @param swtch 开关状态(on:开启；off:关闭)
     * @param switchType 开关类型(tradeMatch:交易配票；deliveryMatch:提货配票)
     */
    int matchSwitchByTradeDay(String swtch,String switchType);

    void test();

}
