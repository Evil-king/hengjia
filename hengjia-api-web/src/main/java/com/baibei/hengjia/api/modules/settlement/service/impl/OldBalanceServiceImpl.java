package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.OldBalanceMapper;
import com.baibei.hengjia.api.modules.settlement.model.OldBalance;
import com.baibei.hengjia.api.modules.settlement.service.IOldBalanceService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/07/04 18:50:24
 * @description: OldBalance服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OldBalanceServiceImpl extends AbstractService<OldBalance> implements IOldBalanceService {

    @Autowired
    private OldBalanceMapper tblSetOldBalanceMapper;

    @Override
    public OldBalance findByCustomerNo(String customerNo) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new IllegalArgumentException("customerNo can not be null");
        }
        Condition condition = new Condition(OldBalance.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        List<OldBalance> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public boolean softDelete(OldBalance oldBalance) {
        oldBalance.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
        oldBalance.setModifyTime(new Date());
        return update(oldBalance);
    }
}
