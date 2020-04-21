package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.TradeWhiteListMapper;
import com.baibei.hengjia.api.modules.trade.model.BuyMatchWhiteList;
import com.baibei.hengjia.api.modules.trade.model.TradeWhiteList;
import com.baibei.hengjia.api.modules.trade.service.ITradeWhiteListService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/17 15:48:14
 * @description: TradeWhiteList服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeWhiteListServiceImpl extends AbstractService<TradeWhiteList> implements ITradeWhiteListService {

    @Autowired
    private TradeWhiteListMapper tblTraTradeWhitelistMapper;

    @Override
    public boolean isWhiteList(String customerNo, String type) {
        Condition condition = new Condition(TradeWhiteList.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("status", Constants.ValidStatus.VALID);
        criteria.andEqualTo("type", type);
        return !CollectionUtils.isEmpty(findByCondition(condition));
    }
}
