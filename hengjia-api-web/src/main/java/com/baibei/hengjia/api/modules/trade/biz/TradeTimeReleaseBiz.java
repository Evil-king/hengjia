package com.baibei.hengjia.api.modules.trade.biz;

import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/11 2:08 PM
 * @description:
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class TradeTimeReleaseBiz {
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IHoldTotalService holdTotalService;


    /**
     * release
     */
    public void release() {
        // step1.查询达到可交易日期的持仓详情数据
        List<HoldDetails> holdDetailsList = holdDetailsService.findCanTradeList();
        if (CollectionUtils.isEmpty(holdDetailsList)) {
            log.info("release holdDetailsList is null...");
        }
        String customerNo, productTradeNo, holdType;
        for (HoldDetails details : holdDetailsList) {
            customerNo = details.getCustomerNo();
            productTradeNo = details.getProductTradeNo();
            holdType = details.getHoldType();
            // step2.增加客户商品持仓汇总的可卖数量
            if (details.getRemaindCount().compareTo(BigDecimal.ZERO) > 0) {
                addCanSellCount(customerNo, productTradeNo, holdType, details.getRemaindCount());
            }
            log.info("detailId={},customerNo={},productTradeNo={},holdType={},originalCount={}", details.getId(), customerNo, productTradeNo, holdType, details.getOriginalCount());
            // step3.更新扫描状态
            updateScanner(details);
        }
    }

    /**
     * 增加客户商品持仓汇总的可卖数量
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @param count
     */
    private void addCanSellCount(String customerNo, String productTradeNo, String holdType, BigDecimal count) {
        HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            log.warn("商品持仓汇总记录不存在,customerNo={},productTradeNo={},holdType={}", customerNo, productTradeNo, holdType);
            return;
        }
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Byte.valueOf(Constants.Flag.VALID));
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("holdType", holdType);
        criteria.andEqualTo("canSellCount", holdTotal.getCanSellCount());
        HoldTotal newTotal = new HoldTotal();
        newTotal.setModifyTime(new Date());
        newTotal.setCanSellCount(holdTotal.getCanSellCount().add(count));
        holdTotalService.updateByConditionSelective(newTotal, condition);
    }

    /**
     * 更新持仓详情扫描状态
     *
     * @param details
     */
    private void updateScanner(HoldDetails details) {
        details.setModifyTime(new Date());
        details.setScanner(Byte.valueOf("1"));
        holdDetailsService.update(details);
    }
}
