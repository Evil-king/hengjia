package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.dao.RecordIntegralMapper;
import com.baibei.hengjia.api.modules.account.model.RecordIntegral;
import com.baibei.hengjia.api.modules.account.service.IRecordIntegralService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:41:05
* @description: RecordIntegral服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordIntegralServiceImpl extends AbstractService<RecordIntegral> implements IRecordIntegralService {

    @Autowired
    private RecordIntegralMapper recordIntegralMapper;

    @Override
    public MyPageInfo<RecordVo> getAll(RecordDto recordIntegralDto) {
        PageHelper.startPage(recordIntegralDto.getCurrentPage(), recordIntegralDto.getPageSize());
        List<RecordVo> list = recordIntegralMapper.getAll(recordIntegralDto);
        MyPageInfo<RecordVo> myPageInfo = new MyPageInfo<>(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTradeType(IntegralTradeTypeEnum.getMsg(new Byte(list.get(i).getTradeType())));
            BigDecimal bg = new BigDecimal(list.get(i).getChangeAmount());
            list.get(i).setChangeAmount(bg.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        return myPageInfo;
    }
}
