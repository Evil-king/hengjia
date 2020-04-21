package com.baibei.hengjia.api.modules.trade.service;
import com.baibei.hengjia.api.modules.trade.model.CustomerTradeRisk;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: 会跳舞的机器人
* @date: 2019/09/03 12:58:48
* @description: CustomerTradeRisk服务接口
*/
public interface ICustomerTradeRiskService extends Service<CustomerTradeRisk> {

    CustomerTradeRisk findByCustomerNo(String customerNo);

}
