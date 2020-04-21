package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.recordIntegralMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordIntegral;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordIntegralService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
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
 * @date: 2019/07/15 10:10:32
 * @description: recordIntegral服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordIntegralServiceImpl extends AbstractService<RecordIntegral> implements IRecordIntegralService {

    @Autowired
    private recordIntegralMapper tblRecordIntegralMapper;

    @Override
    public MyPageInfo<RecordIntegralVo> pageList(RecordIntegralDto recordIntegralDto) {
        PageHelper.startPage(recordIntegralDto.getCurrentPage(), recordIntegralDto.getPageSize());
        List<RecordIntegralVo> recordIntegralVos = RecordIntegralVoList(recordIntegralDto);
        MyPageInfo<RecordIntegralVo> page = new MyPageInfo<>(recordIntegralVos);
        return page;
    }

    public List<RecordIntegralVo> RecordIntegralVoList(RecordIntegralDto recordIntegralDto) {
        List<RecordIntegralVo> recordIntegralVos = tblRecordIntegralMapper.pageList(recordIntegralDto);
        for (int i = 0; i < recordIntegralVos.size(); i++) {
            RecordIntegralVo recordIntegralVo = recordIntegralVos.get(i);
            recordIntegralVo.setTradeType(IntegralTradeTypeEnum.getMsg(Byte.valueOf(recordIntegralVos.get(i).getTradeType())));
            recordIntegralVo.setRealName(MobileUtils.changeName(recordIntegralVo.getRealName()));
            recordIntegralVo.setMobile(MobileUtils.changeMobile(recordIntegralVo.getMobile()));
        }
        return recordIntegralVos;
    }

    @Override
    public BigDecimal findByTradetypeAndCustomerNo(String retype, String customerNo, String startTime, String endTime) {
        return tblRecordIntegralMapper.findByTradetypeAndCustomerNo(retype, customerNo, startTime, endTime);
    }
}
