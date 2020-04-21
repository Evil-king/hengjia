package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.vo.CustomerDealOrderVo;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AccountMapper extends MyMapper<Account> {
    void updateWithdrawTiming(String customerNo);

    List<CustomerDealOrderVo> selectByCustomerDealOrder();
}