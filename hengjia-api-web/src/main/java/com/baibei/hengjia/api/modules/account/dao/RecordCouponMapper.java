package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordCoupon;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface RecordCouponMapper extends MyMapper<RecordCoupon> {
    List<RecordVo> getAll(RecordDto recordCouponDto);
}