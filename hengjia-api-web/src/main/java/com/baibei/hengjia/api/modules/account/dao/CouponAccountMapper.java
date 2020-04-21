package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface CouponAccountMapper extends MyMapper<CouponAccount> {
    List<CouponAccountVo> selectCouponAccount(CouponAccountDto couponAccountDto);
}