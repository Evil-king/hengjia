package com.baibei.hengjia.admin.modules.settlement.dao;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.model.BankOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface BankOrderMapper extends MyMapper<BankOrder> {
    List<BankOrderVo> myList(BankOrderDto bankOrderDto);
}