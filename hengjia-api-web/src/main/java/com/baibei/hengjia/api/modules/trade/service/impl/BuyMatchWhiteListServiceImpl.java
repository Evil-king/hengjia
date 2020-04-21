package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.BuyMatchWhiteListMapper;
import com.baibei.hengjia.api.modules.trade.model.BuyMatchWhiteList;
import com.baibei.hengjia.api.modules.trade.service.IBuyMatchWhiteListService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/12 15:34:23
 * @description: BuyMatchWhiteList服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BuyMatchWhiteListServiceImpl extends AbstractService<BuyMatchWhiteList> implements IBuyMatchWhiteListService {

    @Autowired
    private BuyMatchWhiteListMapper tblTraBuymatchWhitelistMapper;

    @Override
    public BuyMatchWhiteList findByParam(String customerNo, String status) {
        Condition condition = new Condition(BuyMatchWhiteList.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("status", status);
        return findOneByCondition(condition);
    }

    @Override
    public boolean isWhiteList(String customerNo) {
        BuyMatchWhiteList autoWhiteList = findByParam(customerNo, Constants.ValidStatus.VALID);
        return autoWhiteList != null;
    }

}
