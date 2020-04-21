package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.dao.RecordMoneyMapper;
import com.baibei.hengjia.api.modules.account.model.RecordMoney;
import com.baibei.hengjia.api.modules.account.service.IRecordMoneyService;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author: hyc
 * @date: 2019/06/03 14:41:55
 * @description: RecordMoney服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordMoneyServiceImpl extends AbstractService<RecordMoney> implements IRecordMoneyService {

    @Autowired
    private RecordMoneyMapper recordMoneyMapper;

    @Override
    public MyPageInfo<RecordVo> getAll(RecordDto recordDto) {
        PageHelper.startPage(recordDto.getCurrentPage(), recordDto.getPageSize());
        List<RecordVo> list = recordMoneyMapper.getAll(recordDto);
        MyPageInfo<RecordVo> myPageInfo = new MyPageInfo<>(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTradeType(FundTradeTypeEnum.getMsg(new Byte(list.get(i).getTradeType())));
            BigDecimal bg = new BigDecimal(list.get(i).getChangeAmount());
            list.get(i).setChangeAmount(bg.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        return myPageInfo;
    }

    @Override
    public List<CustomerCountVo> sumChangeAmount(Map<String, Object> param) {
        return recordMoneyMapper.sumChangeAmount(param);
    }
}
