package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.EntrustWeightMapper;
import com.baibei.hengjia.api.modules.trade.model.EntrustWeight;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.service.IEntrustWeightService;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/07 10:19:25
 * @description: EntrustWeight服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class EntrustWeightServiceImpl extends AbstractService<EntrustWeight> implements IEntrustWeightService {

    @Autowired
    private EntrustWeightMapper tblTraEntrustWeightMapper;
    @Autowired
    private IHoldDetailsService holdDetailsService;

    @Override
    public void calculation(String customerNo, String productTradeNo, Date nextTradeDay) {
        Map<String,Object> params = new HashMap<>();
        params.put("customerNo",customerNo);
        params.put("productTradeNo",productTradeNo);
        params.put("nextTradeDay",nextTradeDay);
        List<HoldDetails> holdDetailsList = holdDetailsService.findHoldDetailsForNext(params);
        if (CollectionUtils.isEmpty(holdDetailsList)) {
            log.info("holdDetailsList is null,customerNo={},productTradeNo={}", customerNo, productTradeNo);
            return;
        }
        // (a的成本*a的数量+b的成本*b的数量)/总数量
        BigDecimal totalCount = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;
        for (HoldDetails holdDetails : holdDetailsList) {
            totalCount = totalCount.add(holdDetails.getRemaindCount());
            cost = cost.add(holdDetails.getCost().multiply(holdDetails.getRemaindCount()));
        }
        BigDecimal weight = cost.divide(totalCount, 2, BigDecimal.ROUND_FLOOR);
        EntrustWeight entrustWeight = new EntrustWeight();
        entrustWeight.setCustomerNo(customerNo);
        entrustWeight.setProductTradeNo(productTradeNo);
        entrustWeight.setWeight(weight);
        save(entrustWeight);
    }

    @Override
    public int deleteAll() {
        return tblTraEntrustWeightMapper.deleteByCondition(new Condition(EntrustWeight.class));
    }
}
