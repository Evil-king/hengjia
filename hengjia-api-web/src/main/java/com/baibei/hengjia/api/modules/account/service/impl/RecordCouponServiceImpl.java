package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.dao.RecordCouponMapper;
import com.baibei.hengjia.api.modules.account.model.RecordCoupon;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/08/05 10:08:02
* @description: RecordCoupon服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordCouponServiceImpl extends AbstractService<RecordCoupon> implements IRecordCouponService {

    @Autowired
    private RecordCouponMapper tblRecordCouponMapper;

    @Override
    public MyPageInfo<RecordVo> getAll(RecordDto recordCouponDto) {
        PageHelper.startPage(recordCouponDto.getCurrentPage(), recordCouponDto.getPageSize());
        if(StringUtils.isEmpty(recordCouponDto.getCouponType())){
            //兼容，如果不传值默认查询券余额
            recordCouponDto.setCouponType(Constants.CouponType.VOUCHERS);
        }
        List<RecordVo> list = tblRecordCouponMapper.getAll(recordCouponDto);
        MyPageInfo<RecordVo> myPageInfo = new MyPageInfo<>(list);
        for (int i = 0; i < list.size(); i++) {
            if("103".equals(list.get(i).getTradeType())&&Constants.CouponType.VOUCHERS.equals(recordCouponDto.getCouponType())){
                list.get(i).setTradeType("活动赠送");
            }else {
                list.get(i).setTradeType(CouponAccountTradeTypeEnum.getMsg(new Byte(list.get(i).getTradeType())));
            }
            BigDecimal bg = new BigDecimal(list.get(i).getChangeAmount());
            list.get(i).setChangeAmount(bg.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        return myPageInfo;
    }
}
