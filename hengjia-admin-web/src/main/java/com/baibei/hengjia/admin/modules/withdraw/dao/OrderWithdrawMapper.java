package com.baibei.hengjia.admin.modules.withdraw.dao;

import com.baibei.hengjia.admin.modules.withdraw.bean.dto.WithdrawListDto;
import com.baibei.hengjia.admin.modules.withdraw.bean.vo.WithdrawListVo;
import com.baibei.hengjia.admin.modules.withdraw.model.OrderWithdraw;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface OrderWithdrawMapper extends MyMapper<OrderWithdraw> {
    List<WithdrawListVo> pageList(WithdrawListDto withdrawListDto);

}