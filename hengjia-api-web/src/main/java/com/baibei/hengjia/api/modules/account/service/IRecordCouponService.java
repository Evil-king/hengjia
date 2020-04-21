package com.baibei.hengjia.api.modules.account.service;
import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordCoupon;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
* @author: hyc
* @date: 2019/08/05 10:08:02
* @description: RecordCoupon服务接口
*/
public interface IRecordCouponService extends Service<RecordCoupon> {


    MyPageInfo<RecordVo> getAll(RecordDto recordCouponDto);
}
