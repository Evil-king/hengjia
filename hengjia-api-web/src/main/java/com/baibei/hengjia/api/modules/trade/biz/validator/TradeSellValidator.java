package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.service.ITradeWhiteListService;
import com.baibei.hengjia.api.modules.utils.TradeUtil;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/16 13:52
 * @description:
 */
@Component
@Slf4j
public class TradeSellValidator extends ValidatorTemplate {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITradeWhiteListService tradeWhiteListService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("零售商品当天是否已卖出或者已有挂卖单校验器正在执行");
        Object obj = validatorDataContext.getDataContextMap().get("holdDetails");
        if (obj == null) {
            throw new ServiceException("持有数据不存在");
        }
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编码不存在");
        }
        // 判断是否白名单
        boolean whitelistFlag = tradeWhiteListService.isWhiteList(customerNo.toString(), Constants.TradeWhitelistType.TODAY_SELL);
        if (whitelistFlag) {
            log.info("客户{}为零售商品当天是否已卖出或者已有挂卖单白名单", customerNo.toString());
            return;
        }
        Object sellCount = validatorDataContext.getDataContextMap().get("count");
        HoldDetails holdDetails = (HoldDetails) obj;
        String sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
        // 先校验每天智能卖出一手
        if (Constants.SellHoldType.MAIN.equals(sellHoldType)) {
            if (Integer.valueOf(sellCount.toString()) > 1) {
                throw new ServiceException("每交易日仅能卖出1件零售商品");
            }
        } else if (Constants.SellHoldType.MATCH.equals(sellHoldType)) {
            if (Integer.valueOf(sellCount.toString()) > 1) {
                throw new ServiceException("每交易日仅能卖出1张折扣仓单");
            }
        }
        // 再校验是否有卖单
        if (Constants.SellHoldType.MAIN.equals(sellHoldType) || Constants.SellHoldType.MATCH.equals(sellHoldType)) {
            String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, customerNo.toString(), sellHoldType);
            String res = redisUtil.get(key);
            if (!StringUtils.isEmpty(res) && Integer.valueOf(res) >= 1) {
                throw new ServiceException(ResultEnum.EXISTS_SELL_ORDER);
            }
        }
    }
}