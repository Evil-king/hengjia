package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.OrderDepositVo;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;

import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/26 8:49 PM
 * @description:
 */
public class CleanServiceImpl extends AbstractCashFunction<OrderDepositDto, OrderDepositVo> {


    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        return null;
    }

    @Override
    public String responseResult(Map<String, String> map) {
        return null;
    }

    @Override
    public CashFunctionType getType() {
        return null;
    }




}
