package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 8:52 PM
 * @description: 客户持仓商品校验器
 */
@Component
@Slf4j
public class CustomerProductHoldValidator extends ValidatorTemplate {
    @Autowired
    private IHoldTotalService holdTotalService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户持仓商品校验器正在执行...");
        TradeListDto tradeListDto = (TradeListDto) validatorDataContext.getRequestData();
        // 本票
        HoldTotal mainHold = holdTotalService.findByCustomerAndProductNo(tradeListDto.getCustomerNo(),
                tradeListDto.getProductTradeNo(), Constants.HoldType.MAIN);
        BigDecimal mainCount = mainHold == null ? BigDecimal.ZERO : mainHold.getCanSellCount();
        // 配票
        HoldTotal matchHold = holdTotalService.findByCustomerAndProductNo(tradeListDto.getCustomerNo(),
                tradeListDto.getProductTradeNo(), Constants.HoldType.MATCH);
        BigDecimal matchCount = matchHold == null ? BigDecimal.ZERO : matchHold.getCanSellCount();
        if ((mainCount.add(matchCount)).compareTo(BigDecimal.valueOf(tradeListDto.getCount())) < 0) {
            throw new ServiceException("客户可卖商品不足");
        }
    }
}
