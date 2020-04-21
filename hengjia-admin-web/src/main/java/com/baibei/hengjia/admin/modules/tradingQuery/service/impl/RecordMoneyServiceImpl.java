package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.recordMoneyMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordMoney;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordMoneyService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/15 10:10:53
* @description: recordMoney服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordMoneyServiceImpl extends AbstractService<RecordMoney> implements IRecordMoneyService {

    @Autowired
    private recordMoneyMapper tblRecordMoneyMapper;

    @Override
    public MyPageInfo<RecordMoneyVo> pageList(RecordMoneyDto recordMoneyDto) {
        PageHelper.startPage(recordMoneyDto.getCurrentPage(), recordMoneyDto.getPageSize());
        List<RecordMoneyVo> recordMoneyVos=RecordMoneyVoList(recordMoneyDto);
        MyPageInfo<RecordMoneyVo> page = new MyPageInfo<>(recordMoneyVos);
        return page;
    }

    @Override
    public List<RecordMoneyVo> RecordMoneyVoList(RecordMoneyDto recordMoneyDto) {
        List<RecordMoneyVo> recordMoneyVos=tblRecordMoneyMapper.pageList(recordMoneyDto);
        for (int i = 0; i < recordMoneyVos.size(); i++) {
            RecordMoneyVo recordMoneyVo=recordMoneyVos.get(i);
            recordMoneyVo.setTradeType(FundTradeTypeEnum.getMsg(Byte.valueOf(recordMoneyVos.get(i).getTradeType())));
            recordMoneyVo.setRealName(MobileUtils.changeName(recordMoneyVo.getRealName()));
            recordMoneyVo.setMobile(MobileUtils.changeMobile(recordMoneyVo.getMobile()));
        }
        return recordMoneyVos;
    }

    @Override
    public BigDecimal findSumByDateAndCustomerNoAndTradeType(String customerNo, String tradeType, String startTime, String endTime) {
        return tblRecordMoneyMapper.findSumByDateAndCustomerNoAndTradeType(customerNo,tradeType,startTime,endTime);
    }

}
