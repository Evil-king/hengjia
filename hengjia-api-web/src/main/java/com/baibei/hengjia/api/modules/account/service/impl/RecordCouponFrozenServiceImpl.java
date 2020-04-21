package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.dao.RecordCouponFrozenMapper;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.api.modules.account.model.RecordCoupon;
import com.baibei.hengjia.api.modules.account.model.RecordCouponFrozen;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponFrozenService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/10/14 11:13:00
* @description: RecordCouponFrozen服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordCouponFrozenServiceImpl extends AbstractService<RecordCouponFrozen> implements IRecordCouponFrozenService {

    @Autowired
    private RecordCouponFrozenMapper tblRecordCouponFrozenMapper;

    @Override
    public void insertOne(ChangeCouponAccountDto changeCouponAccountDto, CouponAccount couponAccount) {
        RecordCouponFrozen recordCoupon=new RecordCouponFrozen();
        recordCoupon.setRecordNo(changeCouponAccountDto.getOrderNo());
        //交易商编码
        recordCoupon.setCustomerNo(changeCouponAccountDto.getCustomerNo());
        //如果
        String change=changeCouponAccountDto.getReType()==1? "-":"";
        //变动数额
        recordCoupon.setChangeAmount(change+changeCouponAccountDto.getChangeAmount().setScale(2).toString());
        //券类型
        recordCoupon.setCouponType(changeCouponAccountDto.getCouponType());
        //商品交易类型
        recordCoupon.setProductTradeNo(changeCouponAccountDto.getProductTradeNo());
        //券余额
        recordCoupon.setFrozenBalance(couponAccount.getFrozenBalance());
        //交易类型
        recordCoupon.setTradeType(changeCouponAccountDto.getTradeType());
        //冻结类型 1：支出，2：收入
        recordCoupon.setRetype(changeCouponAccountDto.getReType());
        recordCoupon.setCreateTime(new Date());
        recordCoupon.setModifyTime(new Date());
        //是否有效
        recordCoupon.setFlag(new Byte("1"));
        tblRecordCouponFrozenMapper.insertSelective(recordCoupon);
    }

    @Override
    public MyPageInfo<RecordVo> getAll(RecordDto recordCouponDto) {
        PageHelper.startPage(recordCouponDto.getCurrentPage(), recordCouponDto.getPageSize());
        List<RecordVo> list = tblRecordCouponFrozenMapper.getAll(recordCouponDto);
        MyPageInfo<RecordVo> myPageInfo = new MyPageInfo<>(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTradeType(CouponAccountTradeTypeEnum.getMsg(new Byte(list.get(i).getTradeType())));
            BigDecimal bg = new BigDecimal(list.get(i).getChangeAmount());
            list.get(i).setChangeAmount(bg.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        return myPageInfo;
    }
}
