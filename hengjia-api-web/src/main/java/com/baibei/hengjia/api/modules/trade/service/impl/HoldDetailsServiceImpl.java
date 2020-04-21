package com.baibei.hengjia.api.modules.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: HoldDetails服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldDetailsServiceImpl extends AbstractService<HoldDetails> implements IHoldDetailsService {

    @Autowired
    private HoldDetailsMapper tblTraHoldDetailsMapper;

    @Autowired
    private IHoldTotalService holdTotalService;

    @Override
    public List<HoldDetails> listByParams(String customerNo, String productTradeNo, String holdType, String sort, String order) {
        Condition condition = new Condition(HoldDetails.class);
        condition.setOrderByClause(new StringBuffer(sort).append(" ").append(order).toString());
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("holdType", holdType);
        criteria.andGreaterThan("remaindCount", 0);
        criteria.andEqualTo("scanner", "1");
        return findByCondition(condition);
    }

    @Override
    public List<HoldDetails> listByParamsForTrade(String customerNo, String productTradeNo, String holdType) {
        Condition condition = new Condition(HoldDetails.class);
        condition.setOrderByClause("cost desc,trade_time asc");
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        if (!StringUtils.isEmpty(holdType)) {
            criteria.andEqualTo("holdType", holdType);
        }
        criteria.andGreaterThan("remaindCount", 0);
        criteria.andEqualTo("scanner", "1");
        return findByCondition(condition);
    }

    @Override
    public List<HoldDetails> listByParamsWithoutScan(String customerNo, String productTradeNo, String holdType, String sort, String order) {
        Condition condition = new Condition(HoldDetails.class);
        condition.setOrderByClause(new StringBuffer(sort).append(" ").append(order).toString());
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("holdType", holdType);
        criteria.andGreaterThan("remaindCount", 0);
        return findByCondition(condition);
    }

    @Override
    public List<HoldDetails> findCanTradeList() {
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("scanner", 0);
        criteria.andLessThanOrEqualTo("tradeTime", new Date());
        return findByCondition(condition);
    }

    @Override
    public boolean deductMemberProductHold(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType) {
        boolean result = true;
        //获取挂牌商所有持仓明细
        List<HoldDetails> detailsList = this.listByParams(memberNo, productTradeNo, holdType, "trade_time", "asc");
        List<HoldDetails> orignalDetailsList = new ArrayList<>();
        for (int i = 0; i < detailsList.size(); i++) {
            HoldDetails holdDetails = new HoldDetails();
            holdDetails.setRemaindCount(detailsList.get(i).getRemaindCount());
            holdDetails.setOriginalCount(detailsList.get(i).getOriginalCount());
            orignalDetailsList.add(holdDetails);
        }
        if (StringUtils.isEmpty(detailsList)) {
            throw new ServiceException("挂牌商无持仓明细");
        }
        //逐一扣减
        detuchRecursion(detailsList.size() - 1, detailsList, changeAmount);
        for (int i = 0; i < detailsList.size(); i++) {
            BigDecimal beforeCount = orignalDetailsList.get(i).getRemaindCount();//未扣减之前的可用数量
            BigDecimal beforeOrignalCount = orignalDetailsList.get(i).getOriginalCount();
            BigDecimal changeCount = detailsList.get(i).getRemaindCount();//扣减之后的可用数量
            if (beforeCount.compareTo(changeCount) != 0) {
                // 乐观锁实现方式
                Condition condition = new Condition(HoldDetails.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("customerNo", memberNo);
                criteria.andEqualTo("productTradeNo", productTradeNo);
                criteria.andEqualTo("originalCount", beforeOrignalCount);
                criteria.andEqualTo("remaindCount", beforeCount);
                criteria.andEqualTo("holdType", holdType);
                criteria.andEqualTo("id", detailsList.get(i).getId());
                detailsList.get(i).setModifyTime(new Date());
                boolean b = updateByConditionSelective(detailsList.get(i), condition);
                if (!b) {
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public boolean deductProductHoldByTradeTime(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType) {
        boolean result = true;
        BigDecimal detuchCanSellCount = new BigDecimal(0);
        //获取挂牌商所有持仓明细
        List<HoldDetails> detailsList = this.listByParamsWithoutScan(memberNo, productTradeNo, holdType, "trade_time", "asc");
        List<HoldDetails> orignalDetailsList = new ArrayList<>();
        for (int i = 0; i < detailsList.size(); i++) {
            HoldDetails holdDetails = new HoldDetails();
            holdDetails.setRemaindCount(detailsList.get(i).getRemaindCount());
            holdDetails.setOriginalCount(detailsList.get(i).getOriginalCount());
            orignalDetailsList.add(holdDetails);
        }
        if (StringUtils.isEmpty(detailsList)) {
            throw new ServiceException("挂牌商无持仓明细");
        }
        //逐一扣减
        detuchRecursion(detailsList.size() - 1, detailsList, changeAmount);
        for (int i = 0; i < detailsList.size(); i++) {
            BigDecimal beforeCount = orignalDetailsList.get(i).getRemaindCount();//未扣减之前的可用数量
            BigDecimal beforeOrignalCount = orignalDetailsList.get(i).getOriginalCount();
            BigDecimal changeCount = detailsList.get(i).getRemaindCount();//扣减之后的可用数量
            if (beforeCount.compareTo(changeCount) != 0) {
                // 乐观锁实现方式
                Condition condition = new Condition(HoldDetails.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("customerNo", memberNo);
                criteria.andEqualTo("productTradeNo", productTradeNo);
                criteria.andEqualTo("originalCount", beforeOrignalCount);
                criteria.andEqualTo("remaindCount", beforeCount);
                criteria.andEqualTo("holdType", holdType);
                criteria.andEqualTo("id", detailsList.get(i).getId());
                detailsList.get(i).setModifyTime(new Date());
                boolean b = updateByConditionSelective(detailsList.get(i), condition);
                if (!b) {
                    result = false;
                }
                if (detailsList.get(i).getTradeTime().getTime() < new Date().getTime()) {//已解锁
                    detuchCanSellCount = detuchCanSellCount.add((beforeCount.subtract(changeCount)));
                }
            }
        }
        //扣减总持仓
        boolean b = holdTotalService.deductProductHoldByTradeTime(memberNo, productTradeNo, changeAmount, holdType, detuchCanSellCount);
        if (!b) {
            result = false;
        }
        return result;
    }

    @Override
    public List<HoldDetails> findByCustomerAndProductNo(String customerNo, String productTradeNo, String holdType) {
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        criteria.andEqualTo("holdType", holdType);
        criteria.andGreaterThan("remaindCount", BigDecimal.ZERO);
        return findByCondition(condition);
    }

    @Override
    public List<HoldDetails> findHoldDetailsForNext(Map<String, Object> param) {
        return tblTraHoldDetailsMapper.findHoldDetailsForNext(param);
    }


    @Override
    public List<HoldDetails> findByDealOrderNO(List<String> dealOrderNoList) {
        return tblTraHoldDetailsMapper.findByDealOrderNO(dealOrderNoList);
    }

    @Override
    public boolean frozen(HoldDetails holdDetails, int frozenCount) {
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", holdDetails.getId());
        criteria.andEqualTo("frozenCount", holdDetails.getFrozenCount());
        criteria.andEqualTo("remaindCount", holdDetails.getRemaindCount());

        HoldDetails newDetails = new HoldDetails();
        newDetails.setFrozenCount(holdDetails.getFrozenCount().add(new BigDecimal(frozenCount)));
        newDetails.setRemaindCount(holdDetails.getRemaindCount().subtract(new BigDecimal(frozenCount)));
        newDetails.setModifyTime(new Date());
        return updateByConditionSelective(newDetails, condition);
    }

    @Override
    public boolean unfrozen(HoldDetails holdDetails, BigDecimal count) {
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", holdDetails.getId());
        criteria.andEqualTo("frozenCount", holdDetails.getFrozenCount());
        criteria.andEqualTo("remaindCount", holdDetails.getRemaindCount());

        HoldDetails newDetails = new HoldDetails();
        newDetails.setFrozenCount(holdDetails.getFrozenCount().subtract(count));
        newDetails.setRemaindCount(holdDetails.getRemaindCount().add(count));
        newDetails.setModifyTime(new Date());
        return updateByConditionSelective(newDetails, condition);
    }

    @Override
    public boolean deduct(HoldDetails holdDetails, BigDecimal count) {
        if (holdDetails.getRemaindCount().compareTo(count) < 0) {
            log.info("持有扣除数量不足，扣除数量为{}，剩余数量为{}，holdDetails={}", count, holdDetails.getRemaindCount(), JSON.toJSON(holdDetails));
            throw new ServiceException("持有扣除数量不足");
        }
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", holdDetails.getId());
        criteria.andEqualTo("originalCount", holdDetails.getOriginalCount());
        criteria.andEqualTo("remaindCount", holdDetails.getRemaindCount());

        HoldDetails newDetails = new HoldDetails();
        newDetails.setOriginalCount(holdDetails.getOriginalCount().subtract(count));
        newDetails.setRemaindCount(holdDetails.getRemaindCount().subtract(count));
        newDetails.setModifyTime(new Date());
        return updateByConditionSelective(newDetails, condition);
    }

    @Override
    public boolean frozenDeduct(HoldDetails holdDetails, BigDecimal count) {
        if (holdDetails.getFrozenCount().compareTo(count) < 0) {
            log.info("持有扣除冻结数量不足，扣除数量为{}，冻结数量为{}，holdDetails={}", count, holdDetails.getRemaindCount(), JSON.toJSON(holdDetails));
            throw new ServiceException("持有扣除数量不足");
        }
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", holdDetails.getId());
        criteria.andEqualTo("originalCount", holdDetails.getOriginalCount());
        criteria.andEqualTo("frozenCount", holdDetails.getFrozenCount());

        HoldDetails newDetails = new HoldDetails();
        newDetails.setOriginalCount(holdDetails.getOriginalCount().subtract(count));
        newDetails.setFrozenCount(holdDetails.getFrozenCount().subtract(count));
        newDetails.setModifyTime(new Date());
        return updateByConditionSelective(newDetails, condition);
    }

    /**
     * 递归扣减挂牌商持仓明细
     *
     * @param n            挂牌商持仓明细总量-1
     * @param detailsList
     * @param changeAmount
     * @return
     */
    public int detuchRecursion(int n, List<HoldDetails> detailsList, BigDecimal changeAmount) {
        if (n < 0) {
            if (changeAmount.compareTo(new BigDecimal(0)) != 0) {
                throw new ServiceException("挂牌商明细扣减失败，明细商品总额不足");
            }
            return -1;
        }
        HoldDetails holdDetails = detailsList.get(n);
        System.out.println(holdDetails.getRemaindCount());
        if (detailsList.get(n).getRemaindCount().compareTo(changeAmount) >= 0) {
            detailsList.get(n).setRemaindCount(detailsList.get(n).getRemaindCount().subtract(changeAmount));
            return -1;
        } else {
            BigDecimal remainAmount = changeAmount.subtract(detailsList.get(n).getRemaindCount());
            detailsList.get(n).setRemaindCount(new BigDecimal(0));
            return detuchRecursion(n - 1, detailsList, remainAmount);
        }
    }

    public static void main(String[] args) {
        List<HoldDetails> detailsList = new ArrayList<>();
        HoldDetails h1 = new HoldDetails();
        h1.setId(1L);
        h1.setRemaindCount(new BigDecimal(10));
        HoldDetails h2 = new HoldDetails();
        h2.setId(2L);
        h2.setRemaindCount(new BigDecimal(10));
        HoldDetails h3 = new HoldDetails();
        h3.setId(3L);
        h3.setRemaindCount(new BigDecimal(10));
        HoldDetails h4 = new HoldDetails();
        h4.setId(4L);
        h4.setRemaindCount(new BigDecimal(10));
        detailsList.add(h1);
        detailsList.add(h2);
        detailsList.add(h3);
        detailsList.add(h4);
        /*detuchRecursion(detailsList.size()-1, detailsList,new BigDecimal(15));*/

        System.out.println("aaaa");

    }

}
