package com.baibei.hengjia.api.modules.cash.dao;

import com.baibei.hengjia.api.modules.cash.model.AccountBook;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

public interface AccountBookMapper extends MyMapper<AccountBook> {

    void clear();
}