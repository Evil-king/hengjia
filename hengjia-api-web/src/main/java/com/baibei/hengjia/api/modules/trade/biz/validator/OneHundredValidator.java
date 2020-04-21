package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeDeListDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
import com.baibei.hengjia.api.modules.trade.model.EntrustOrder;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.api.modules.trade.service.IEntrustOrderService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/17 8:52 PM
 * @description: 当天累计买入超过100手校验器
 */
@Component
@Slf4j
public class OneHundredValidator extends ValidatorTemplate {
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProductMarketService productMarketService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("买入超过100手校验器正在执行...");
        String customerNo, productTradeNo, direction;
        Integer count, oneHundred;
        Object requestData = validatorDataContext.getRequestData();
        if (requestData instanceof TradeListDto) {
            TradeListDto tradeListDto = (TradeListDto) requestData;
            customerNo = tradeListDto.getCustomerNo();
            productTradeNo = tradeListDto.getProductTradeNo();
            count = tradeListDto.getCount();
            oneHundred = tradeListDto.getOneHundred();
            direction = tradeListDto.getDirection();
        } else if (requestData instanceof TradeDeListDto) {
            TradeDeListDto tradeDeListDto = (TradeDeListDto) requestData;
            EntrustOrder entrustOrder = entrustOrderService.findByOrderNo(tradeDeListDto.getEntrustNo());
            if (entrustOrder == null) {
                throw new ServiceException("委托单不存在");
            }
            customerNo = tradeDeListDto.getCustomerNo();
            productTradeNo = entrustOrder.getProductTradeNo();
            count = tradeDeListDto.getCount();
            oneHundred = tradeDeListDto.getOneHundred();
            direction = tradeDeListDto.getDirection();
        } else {
            throw new ServiceException("类型不匹配");
        }
        // 只校验买入操作
        if (!Constants.TradeDirection.BUY.equals(direction)) {
            return;
        }
        if (oneHundred == null) {
            throw new ServiceException("超过指定手数参数为空");
        }
        String key = MessageFormat.format(RedisConstant.TRADE_BUY_ONEHUNDRED, customerNo, productTradeNo);
        if (0 == oneHundred.intValue()) {
            String value = redisUtil.get(key);
            if (StringUtils.isEmpty(value)) {
                ProductMarket productMarket = productMarketService.findByProductTradeNo(productTradeNo);
                Integer existCount = dealOrderService.sumBuyCount(customerNo, productTradeNo,
                        DateUtil.yyyyMMddWithLine.get().format(new Date()));
                if (count + existCount > productMarket.getDeliveryNum()) {
                    throw new ServiceException(productMarket.getDeliveryNum().toString(), ResultEnum.ONEHUNDREW.getCode());
                }
            }
        } else {
            redisUtil.set(key, "1");
            redisUtil.expireAt(key, DateUtil.getEndDay());
        }
    }
}
