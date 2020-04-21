package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.bean.dto.TradeDeListDto;
import com.baibei.hengjia.api.modules.trade.model.EntrustOrder;
import com.baibei.hengjia.api.modules.trade.service.IEntrustOrderService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 1:53 PM
 * @description: 摘牌的委托单是否合法校验器
 */
@Component
@Slf4j
public class DeListEntrustOrderValidator extends ValidatorTemplate {
    @Autowired
    private IEntrustOrderService entrustOrderService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("委托单校验器正在执行...");
        TradeDeListDto tradeDeListDto = (TradeDeListDto) validatorDataContext.getRequestData();
        EntrustOrder entrustOrder = entrustOrderService.findByOrderNo(tradeDeListDto.getEntrustNo());
        if (entrustOrder == null) {
            throw new ServiceException("委托单不存在");
        }
        if (Constants.EntrustOrderResult.ALL_DEAL.equals(entrustOrder.getResult())) {
            throw new ServiceException("委托单已成交");
        }
        if (Constants.EntrustOrderResult.REVOKE.equals(entrustOrder.getResult())) {
            throw new ServiceException("委托单已撤销");
        }
        // 不能摘自已的挂单
        if (tradeDeListDto.getCustomerNo().equals(entrustOrder.getCustomerNo())) {
            throw new ServiceException("不能摘自已的挂单");
        }
        // 摘牌买入不能摘挂买的单
        if (tradeDeListDto.getDirection().equals(entrustOrder.getDirection())) {
            throw new ServiceException("摘牌方向错误");
        }
        // 摘牌数量不能委托单剩余数量
        if (tradeDeListDto.getCount() > (entrustOrder.getEntrustCount() - entrustOrder.getDealCount())) {
            throw new ServiceException("摘牌数量不能大于委托数量");
        }
        validatorDataContext.getDataContextMap().put("productTradeNo", entrustOrder.getProductTradeNo());
        validatorDataContext.getDataContextMap().put("entrustOrder", entrustOrder);
    }
}
