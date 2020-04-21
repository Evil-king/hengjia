package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
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

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 8:51 PM
 * @description: 商品上市状态校验器
 */
@Component
@Slf4j
public class ProductTradeStatusValidator extends ValidatorTemplate {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProductMarketService productMarketService;


    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("商品上市状态校验器正在执行...");
        TradeListDto tradeListDto = (TradeListDto) validatorDataContext.getRequestData();
        String key = MessageFormat.format(RedisConstant.PRODUCT_TRADE_NO, tradeListDto.getProductTradeNo());
        Object value = redisUtil.hmget(key, "tradeStatus");
        Object tepmPrice = redisUtil.hmget(key, "issuePrice");
        String tradeStatus;
        BigDecimal issuePrice;
        // 调用商品服务进行查下
        if (value != null && tepmPrice != null) {
            tradeStatus = value.toString();
            issuePrice = new BigDecimal(tepmPrice.toString());
        } else {
            ProductMarket productMarket = productMarketService.findByProductTradeNo(tradeListDto.getProductTradeNo());
            if (productMarket == null) {
                throw new ServiceException("上市商品不存在");
            }
            tradeStatus = productMarket.getTradeStatus();
            issuePrice = productMarket.getIssuePrice();
        }
        if (StringUtils.isEmpty(tradeStatus) || !Constants.ProductMarketTradeStatus.TRADING.equals(tradeStatus)) {
            throw new ServiceException(ResultEnum.PRODUCT_NOT_TRADING);
        }
        if (issuePrice.compareTo(new BigDecimal(tradeListDto.getPrice())) != 0) {
            throw new ServiceException("挂牌价格错误");
        }
    }
}
