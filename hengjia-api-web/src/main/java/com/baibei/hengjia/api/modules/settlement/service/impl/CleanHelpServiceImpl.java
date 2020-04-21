package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.settlement.dao.CleanHelpMapper;
import com.baibei.hengjia.api.modules.settlement.model.CleanHelp;
import com.baibei.hengjia.api.modules.settlement.service.ICleanHelpService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
* @author: 会跳舞的机器人
* @date: 2019/10/21 14:26:31
* @description: CleanHelp服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CleanHelpServiceImpl extends AbstractService<CleanHelp> implements ICleanHelpService {

    @Autowired
    private CleanHelpMapper tblSetCleanHelpMapper;

    @Override
    public List<CustomerCountVo> sumLoss(Map<String, Object> param) {
        return tblSetCleanHelpMapper.sumLoss(param);
    }

    @Override
    public List<CustomerCountVo> sumProfit(Map<String, Object> param) {
        return tblSetCleanHelpMapper.sumProfit(param);
    }
}
