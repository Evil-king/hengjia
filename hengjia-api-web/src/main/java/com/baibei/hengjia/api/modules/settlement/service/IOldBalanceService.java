package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.OldBalance;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: wenqing
 * @date: 2019/07/04 18:50:24
 * @description: OldBalance服务接口
 */
public interface IOldBalanceService extends Service<OldBalance> {

    OldBalance findByCustomerNo(String customerNo);

    boolean softDelete(OldBalance oldBalance);

}
