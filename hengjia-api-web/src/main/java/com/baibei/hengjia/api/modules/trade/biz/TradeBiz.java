package com.baibei.hengjia.api.modules.trade.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.account.service.ICustomerIntegralService;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.product.service.IProductStockService;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.sms.util.RandomUtils;
import com.baibei.hengjia.api.modules.sms.util.SmsUtil;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeDeListDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.IntegralAndProfit;
import com.baibei.hengjia.api.modules.trade.biz.validator.*;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.api.modules.utils.TradeUtil;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.*;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.MapUtil;
import com.baibei.hengjia.common.tool.utils.NoUtil;
import com.baibei.hengjia.common.tool.validate.ValidateService;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/3 7:33 PM
 * @description:
 */
@Component
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TradeBiz {
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IProductStockService productStockService;
    @Autowired
    private ICustomerIntegralService customerIntegralService;
    @Autowired
    private IDealHoldRefService dealHoldRefService;
    @Autowired
    private IProductMarketService productMarketService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IPubSmsService pubSmsService;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICouponAccountService couponAccountService;
    @Autowired
    private IAutoWhiteListService autoWhiteListService;
    @Autowired
    private IAutoBlacklistService autoBlacklistService;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private IAutoConfigService autoConfigService;
    @Autowired
    private IEntrustDetailsRefService entrustDetailsRefService;

    private static ExecutorService executor = new ThreadPoolExecutor(5, 10,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(50));
    /**
     * 买入手续费
     */
    @Value("${buy.fee.rate}")
    private String buyFeeRate;

    /**
     * 卖出手续费
     */
    @Value("${sell.fee.rate}")
    private String sellFeeRate;

    /**
     * 转积分比例
     */
    @Value("${integral.rate}")
    private String integralRate;

    /**
     * 排除用户不发短信
     */
    @Value("${message.customerNo}")
//    @Value("2578,0015670994,0011322060")
    private String msgCustomerNo;

    @Value("${auto.trade.buy.sms}")
    private String autoTradeBuySms;

    @Value("${auto.trade.sell.sms}")
    private String autoTradeSellSms;

    @Value("${trade.coupon.amount}")
    private String couponAmount;

    @Value("${hengjia.lister}")
    private String hengjiaLister;

    @Value("${trade.hongmuFund.rate}")
    private String hongmuFundRate;

    @Value("${distributor.customerNo}")
    private String distributorCustomerNo;
    /**
     * 挂牌买入
     *
     * @param tradeListDto
     * @return
     */
    public ApiResult<String> listBuy(TradeListDto tradeListDto) {
        long start = System.currentTimeMillis();
        String customerNo = tradeListDto.getCustomerNo();
        String productTradeNo = tradeListDto.getProductTradeNo();
        String direction = tradeListDto.getDirection();
        String price = tradeListDto.getPrice();
        int count = tradeListDto.getCount();
        // step1.业务参数校验
        ValidatorDataContext tradeDataContext = new ValidatorDataContext();
        tradeDataContext.setRequestData(tradeListDto);
        tradeDataContext.getDataContextMap().put("customerNo", tradeListDto.getCustomerNo());
        tradeDataContext.getDataContextMap().put("direction", tradeListDto.getDirection());
        tradeDataContext.getDataContextMap().put("type", "list");
        // TradeDayValidator=开休市时间校验,CustomerTradeStatusValidator=客户交易状态校验,CustomerBalanceValidator=客户余额校验,
        // ProductTradeStatusValidator=商品交易状态校验
        Class[] validators = new Class[]{TradeDayValidator.class, CustomerTradeStatusValidator.class, ProductTradeStatusValidator.class,
                CustomerBalanceValidator.class, DeListCustomerAddressValidator.class, SignValidator.class, OneHundredValidator.class,
                CustomerTradeRiskValidator.class};
        validateService.setValidatos(validators);
        validateService.validate(tradeDataContext);
        // step2.创建委托单,初始化状态
        String entrustNo = NoUtil.getEntrustNo();
        BigDecimal amount = new BigDecimal(tradeListDto.getPrice()).multiply(new BigDecimal(tradeListDto.getCount()));
        // 计算买入单手续费,挂牌报价*购买数量*买入手续费费率,向上进位,保留两位小数
        BigDecimal fee = amount.multiply(new BigDecimal(buyFeeRate)).setScale(BigDecimal.ROUND_CEILING, 2);
        Object canShowTemp = tradeDataContext.getDataContextMap().get("canShow");
        Byte canShow = canShowTemp == null ? Byte.valueOf("1") : Byte.valueOf(canShowTemp.toString());
        entrustOrderService.initEntrustOrder(customerNo, productTradeNo, direction, price,
                entrustNo, count, fee, Constants.HoldType.MAIN, canShow);
        // step3.冻结客户资金(购买金额+手续费)
        frozenCustomerBalance(tradeListDto.getCustomerNo(), amount.add(fee), "2",
                FreezingAmountTradeTypeEnum.LIST_BUY_FREEZE.getCode() + "", entrustNo);
        log.info("listBuy time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success("挂牌买入成功");
    }

    /**
     * 冻结/解冻客户资金
     *
     * @param customerNo
     * @param amount
     * @param reType
     * @param tradeType
     * @param orderNo
     */
    private void frozenCustomerBalance(String customerNo, BigDecimal amount, String reType, String tradeType, String orderNo) {
        ChangeAmountDto frozenAmountDto = new ChangeAmountDto();
        frozenAmountDto.setCustomerNo(customerNo);
        frozenAmountDto.setChangeAmount(amount);
        frozenAmountDto.setOrderNo(orderNo);
        frozenAmountDto.setTradeType(Byte.valueOf(tradeType));
        frozenAmountDto.setReType(Byte.valueOf(reType));
        accountService.changeFreezenAmountByType(frozenAmountDto);
    }

    /**
     * 挂牌卖出
     *
     * @param tradeListDto
     * @return
     */
    public ApiResult<String> listSell(TradeListDto tradeListDto) {
        long start = System.currentTimeMillis();
        // step1.业务参数校验
        ValidatorDataContext tradeDataContext = new ValidatorDataContext();
        tradeDataContext.setRequestData(tradeListDto);
        tradeDataContext.getDataContextMap().put("customerNo", tradeListDto.getCustomerNo());
        tradeDataContext.getDataContextMap().put("direction", tradeListDto.getDirection());
        tradeDataContext.getDataContextMap().put("detailsId", tradeListDto.getId());
        tradeDataContext.getDataContextMap().put("count", tradeListDto.getCount());
        Class[] validators = new Class[]{TradeDayValidator.class, CustomerTradeStatusValidator.class, ProductTradeStatusValidator.class,
                CustomerProductHoldValidator.class, DeListCustomerAddressValidator.class, SignValidator.class, BuyMatchValidator.class,
                HoldDetailsValidator.class, TradeSellValidator.class};
        validateService.setValidatos(validators);
        validateService.validate(tradeDataContext);
        Object obj = tradeDataContext.getDataContextMap().get("holdDetails");
        if (obj == null) {
            throw new ServiceException("持有记录不存在");
        }
        HoldDetails holdDetails = (HoldDetails) obj;
        // 创建委托单
        EntrustOrder entrustOrder = entrustOrderService.initEntrustOrder(tradeListDto.getCustomerNo(), tradeListDto.getProductTradeNo(),
                tradeListDto.getDirection(), tradeListDto.getPrice(), NoUtil.getEntrustNo(), tradeListDto.getCount(),
                null, holdDetails.getHoldType(), Byte.valueOf("1"));
        // 冻结客户商品持仓汇总
        boolean flag = holdTotalService.frozenCustomerProductHold(tradeListDto.getCustomerNo(), tradeListDto.getProductTradeNo(),
                tradeListDto.getCount(), holdDetails.getHoldType());
        if (!flag) {
            throw new ServiceException("冻结客户持仓商品失败");
        }
        // 冻结客户持仓明细
        BigDecimal diff = holdDetails.getRemaindCount().subtract(new BigDecimal(tradeListDto.getCount()));
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("可卖商品数量不足");
        }
        boolean detailsFlag = holdDetailsService.frozen(holdDetails, tradeListDto.getCount());
        if (!detailsFlag) {
            log.info("冻结客户持有失败,holdDetails={}", JSON.toJSONString(holdDetails));
            throw new ServiceException("冻结客户持有失败");
        }
        // 维护委托单与持仓明细的关系表
        EntrustDetailsRef ref = new EntrustDetailsRef();
        ref.setEntrustNo(entrustOrder.getEntrustNo());
        ref.setHoldDetailsId(holdDetails.getId());
        ref.setDeductCount(new BigDecimal(tradeListDto.getCount()));
        entrustDetailsRefService.save(ref);
        // 写入卖一手零售一手折扣Redis标志
        String sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
        if (Constants.SellHoldType.MAIN.equals(sellHoldType) || Constants.SellHoldType.MATCH.equals(sellHoldType)) {
            String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, tradeListDto.getCustomerNo(), sellHoldType);
            redisUtil.set(key, tradeListDto.getCount());
            redisUtil.expireAt(key, DateUtil.getEndDay());
        }
        log.info("listSell time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success("挂牌卖出成功");
    }

    /**
     * 将挂牌交易数据记入Redis
     *
     * @param productTradeNo
     * @param direction
     * @param price
     * @param count
     * @param entrustOrder
     */
    private void listToRedis(String productTradeNo, String direction, String price, int count, EntrustOrder entrustOrder) {
        DecimalFormat format = new DecimalFormat("0.00");
        price = format.format(new BigDecimal(price));
        // case1.挂牌买入
        if (Constants.TradeDirection.BUY.equals(direction)) {
            // 累加商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_BUY, productTradeNo);
            redisUtil.hincrBy(countKey, price, count);
            // 设置挂牌买入委托单
            String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_BUY, productTradeNo, price);
            redisUtil.zdd(timeKey, entrustOrder.getEntrustNo(), entrustOrder.getCreateTime().getTime());
            // case2.挂牌卖出
        } else {
            // 累加商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_SELL, productTradeNo);
            redisUtil.hincrBy(countKey, price, count);
            // 设置挂牌买入委托单
            String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_SELL, productTradeNo, price);
            redisUtil.zdd(timeKey, entrustOrder.getEntrustNo(), entrustOrder.getCreateTime().getTime());
        }
        // 设置委托单信息
        String entrustInfoKey = MessageFormat.format(RedisConstant.TRADE_ENTRUSTINFO, entrustOrder.getEntrustNo());
        redisUtil.hsetAll(entrustInfoKey, MapUtil.objectToMap(entrustOrder));
    }


    /**
     * 摘牌卖出
     *
     * @param tradeDeListDto
     * @return
     */
    public ApiResult<String> delistSell(TradeDeListDto tradeDeListDto) {
        long start = System.currentTimeMillis();
        String customerNo = tradeDeListDto.getCustomerNo();
        int count = tradeDeListDto.getCount();
        // step1.业务参数校验
        ValidatorDataContext tradeDataContext = new ValidatorDataContext();
        tradeDataContext.setRequestData(tradeDeListDto);
        tradeDataContext.getDataContextMap().put("customerNo", tradeDeListDto.getCustomerNo());
        tradeDataContext.getDataContextMap().put("direction", tradeDeListDto.getDirection());
        tradeDataContext.getDataContextMap().put("detailsId", tradeDeListDto.getId());
        tradeDataContext.getDataContextMap().put("count", tradeDeListDto.getCount());
        Class[] validators = new Class[]{TradeDayValidator.class, CustomerTradeStatusValidator.class, DeListEntrustOrderValidator.class,
                DeListCustomerProductHoldValidator.class, DeListCustomerAddressValidator.class, SignValidator.class, BuyMatchValidator.class,
                HoldDetailsValidator.class, TradeSellValidator.class};
        validateService.setValidatos(validators);
        validateService.validate(tradeDataContext);
        Object obj = tradeDataContext.getDataContextMap().get("holdDetails");
        if (obj == null) {
            throw new ServiceException("持有记录不存在");
        }
        HoldDetails holdDetails = (HoldDetails) obj;
        Object entrustObj = tradeDataContext.getDataContextMap().get("entrustOrder");
        if (entrustObj == null) {
            throw new ServiceException("委托单不存在");
        }
        EntrustOrder entrustOrder = (EntrustOrder) entrustObj;
        String productTradeNo = entrustOrder.getProductTradeNo();
        String holdType = holdDetails.getHoldType();
        String dealNo = NoUtil.getDealNo();
        // 扣除客户商品持仓汇总&&客户商品持仓明细
        List<IntegralAndProfit> integralAndProfitList = deductHoldTotalAndDetails(dealNo, customerNo, productTradeNo, count, holdType, "deduct", entrustOrder.getPrice(), holdDetails);
        // 卖出手续费=卖出数量 * 挂买报价 * 卖出手续费费率
        BigDecimal fee = BigDecimal.valueOf(count).multiply(entrustOrder.getPrice()).multiply(new BigDecimal(sellFeeRate)).setScale(2, BigDecimal.ROUND_CEILING);
        BigDecimal buyFee = BigDecimal.valueOf(count).multiply(entrustOrder.getPrice()).multiply(new BigDecimal(buyFeeRate)).setScale(2, BigDecimal.ROUND_CEILING);
        BigDecimal amount = BigDecimal.valueOf(count).multiply(entrustOrder.getPrice());
        BigDecimal integral = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal couponAmount = BigDecimal.ZERO;
        BigDecimal hongmuFundAmount = BigDecimal.ZERO;
        // 计算成交单转积分与盈利
        for (IntegralAndProfit integralAndProfit : integralAndProfitList) {
            integral = integral.add(integralAndProfit.getIntegral());
            profit = profit.add(integralAndProfit.getProfit());
            couponAmount = couponAmount.add(integralAndProfit.getCouponAmount());
            hongmuFundAmount = hongmuFundAmount.add(integralAndProfit.getHongmuFund());
        }
        if (integral.compareTo(BigDecimal.ZERO) > 0) {
            // step3.给用户增加积分
            ChangeIntegralDto integralDto = new ChangeIntegralDto();
            integralDto.setCustomerNo(customerNo);
            integralDto.setChangeAmount(integral);
            integralDto.setOrderNo(dealNo);
            integralDto.setTradeType(Byte.valueOf(IntegralTradeTypeEnum.TRADING_GIVING.getCode() + ""));
            integralDto.setReType(Byte.valueOf("2"));
            integralDto.setIntegralNo(Long.valueOf(101));
            customerIntegralService.changeIntegral(integralDto);
            log.info("用户增加积分成功,integralDto={}", JSON.toJSONString(integral));
        }
        if (couponAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 将提货款给挂牌商
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(hengjiaLister);
            changeAmountDto.setChangeAmount(couponAmount);
            changeAmountDto.setOrderNo(dealNo);
            changeAmountDto.setTradeType(FundTradeTypeEnum.DELIVERYTICKET_IN.getCode());
            changeAmountDto.setReType(Byte.valueOf("2"));
            accountService.changeAccount(changeAmountDto);
            // 修改客户的提货款余额
            ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
            changeCouponAccountDto.setCustomerNo(customerNo);
            changeCouponAccountDto.setChangeAmount(couponAmount);
            changeCouponAccountDto.setOrderNo(dealNo);
            changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.DELIVERY_TICKET_GET.getCode());
            changeCouponAccountDto.setReType(Byte.valueOf("2"));
            changeCouponAccountDto.setProductTradeNo(productTradeNo);
            changeCouponAccountDto.setCouponType(Constants.CouponType.DELIVERYTICKET);
            couponAccountService.changeAmount(changeCouponAccountDto);
        }
        if (hongmuFundAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 将红木基金给经销商
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(distributorCustomerNo);
            changeAmountDto.setChangeAmount(hongmuFundAmount);
            changeAmountDto.setOrderNo(dealNo);
            changeAmountDto.setTradeType(FundTradeTypeEnum.MARKET_ADJUST.getCode());
            changeAmountDto.setReType(Byte.valueOf("2"));
            accountService.changeAccount(changeAmountDto);
        }
        // 摘卖方获得资金= 卖出获得资金  - 手续费 - 转积分 - 转提货款-红木基金
        BigDecimal totalAmount = amount.subtract(fee).subtract(integral).subtract(couponAmount).subtract(hongmuFundAmount);
        // step4.增加摘牌方的资金
        Byte tradeType = Constants.HoldType.MAIN.equals(holdType) ? FundTradeTypeEnum.MAIN_SELL.getCode() : FundTradeTypeEnum.MATCH_SELL.getCode();
        ChangeAmountDto delistAmountDto = new ChangeAmountDto();
        delistAmountDto.setCustomerNo(customerNo);
        delistAmountDto.setChangeAmount(totalAmount);
        delistAmountDto.setOrderNo(dealNo);
        delistAmountDto.setTradeType(tradeType);
        delistAmountDto.setReType(Byte.valueOf("2"));
        accountService.changeAccount(delistAmountDto);
        log.info("增加挂牌方资金成功,delistAmountDto={}", JSON.toJSONString(delistAmountDto));
        // step5.解冻扣除挂牌方的冻结余额
        ChangeAmountDto frozenDto = new ChangeAmountDto();
        frozenDto.setCustomerNo(entrustOrder.getCustomerNo());
        frozenDto.setChangeAmount(amount.add(buyFee));
        frozenDto.setOrderNo(entrustOrder.getEntrustNo());
        frozenDto.setTradeType(Byte.valueOf(FreezingAmountTradeTypeEnum.DELIST_BUY_FREEZE.getCode() + ""));
        frozenDto.setReType(Byte.valueOf("1"));
        frozenDto.setOrderNo(dealNo);
        accountService.thawAmountByTrade(frozenDto);
        log.info("解冻扣除挂牌方的冻结余额成功,frozenDto={}", JSON.toJSONString(frozenDto));
        // step6.增加挂牌方的商品持仓
        addHold(entrustOrder.getCustomerNo(), productTradeNo, count, entrustOrder.getEntrustNo());
        log.info("增加挂牌方的商品持仓成功");
        // step7.创建成交单
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(dealNo);
        dealOrder.setEntrustId(entrustOrder.getId());
        dealOrder.setProductTradeNo(entrustOrder.getProductTradeNo());
        dealOrder.setBuyCustomerNo(entrustOrder.getCustomerNo());
        dealOrder.setSellCustomerNo(customerNo);
        dealOrder.setBuyFee(buyFee);
        dealOrder.setSellFee(fee);
        dealOrder.setProfit(profit);
        dealOrder.setIntegral(integral);
        dealOrder.setType(Constants.TradeDirection.SELL);
        dealOrder.setPrice(entrustOrder.getPrice());
        dealOrder.setCount(count);
        dealOrder.setHoldType(holdType);
        if (StringUtils.isEmpty(tradeDeListDto.getResource())) {
            dealOrder.setResource(DealOrderResourceEnum.NORMAL.getCode());
        } else {
            dealOrder.setResource(tradeDeListDto.getResource());
        }
        dealOrder.setIntegralReturnFlag("no");
        dealOrder.setCouponAmount(couponAmount);
        dealOrder.setHongmuFund(hongmuFundAmount);
        dealOrderService.save(dealOrder);
        log.info("创建成交单成功,dealOrder={}", JSON.toJSONString(dealOrder));
        // step9.更新委托单状态
        updateEntrustOrder(entrustOrder, tradeDeListDto.getCount().toString());
        log.info("delistBuy更新委托单状态成功...");
        // 写入卖一手零售一手折扣Redis标志
        String sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
        if (Constants.SellHoldType.MAIN.equals(sellHoldType) || Constants.SellHoldType.MATCH.equals(sellHoldType)) {
            String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, tradeDeListDto.getCustomerNo(), sellHoldType);
            redisUtil.set(key, tradeDeListDto.getCount());
            redisUtil.expireAt(key, DateUtil.getEndDay());
        }
        //发送短信
        executor.submit(() -> {
            // 发送委托者委托单成交短信
            boolean entrustSmsFlag = isNotNeedSendSms(entrustOrder.getCustomerNo());
            if (!entrustSmsFlag) {
                CustomerVo customerVo = customerService.findUserByCustomerNo(entrustOrder.getCustomerNo());
                String sms = smsUtil.operatorSms("2", "2", RandomUtils.getRandomNumber(6), customerVo.getMobile());
                pubSmsService.getSms(customerVo.getMobile(), sms);
            }
            // 预约自动卖出成交短信
            if (DealOrderResourceEnum.AUTO_TRADE.getCode().equals(tradeDeListDto.getResource())) {
                boolean dealSmsFlag = isNotNeedSendSms(tradeDeListDto.getCustomerNo());
                if (!dealSmsFlag) {
                    CustomerVo customerVo = customerService.findUserByCustomerNo(tradeDeListDto.getCustomerNo());
                    pubSmsService.sendNormalSms(customerVo.getMobile(), autoTradeBuySms);
                }
            }
        });
        log.info("delistBuy time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success("摘买入牌成功");
    }


    /**
     * 修改委托单状态
     *
     * @param entrustOrder
     * @param count
     */
    private void updateEntrustOrder(EntrustOrder entrustOrder, String count) {
        entrustOrder.setModifyTime(new Date());
        entrustOrder.setDealCount(entrustOrder.getDealCount() + Integer.valueOf(count));
        if (entrustOrder.getEntrustCount().intValue() == entrustOrder.getDealCount().intValue()) {
            entrustOrder.setResult(Constants.EntrustOrderResult.ALL_DEAL);
            String key = MessageFormat.format(RedisConstant.ACCOUNT_WITHDRAW_AMOUT, entrustOrder.getCustomerNo() + entrustOrder.getEntrustNo());
            redisUtil.delete(key);
        } else {
            entrustOrder.setResult(Constants.EntrustOrderResult.SOME_DEAL);
        }
        boolean flag = entrustOrderService.update(entrustOrder);
        if (!flag) {
            throw new ServiceException("更新委托单状态失败");
        }
    }


    /**
     * 摘牌成交移除Redis委托数据
     *
     * @param direction
     * @param entrustOrder
     */
    private void delistForRedis(String direction, EntrustOrder entrustOrder) {
        String productTradeNo = entrustOrder.getProductTradeNo();
        String price = entrustOrder.getPrice().toPlainString();
        // 部分成交扣减数量即可
        if (Constants.TradeDirection.BUY.equals(direction)) {
            // 减少商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_SELL, productTradeNo);
            redisUtil.hincrBy(countKey, price, -entrustOrder.getDealCount());
        } else {
            // 减少商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_BUY, productTradeNo);
            redisUtil.hincrBy(countKey, price, -entrustOrder.getDealCount());
        }
        // 全部成交需要删除委托单数据
        if (Constants.EntrustOrderResult.ALL_DEAL.equals(entrustOrder.getResult())) {
            if (Constants.TradeDirection.BUY.equals(direction)) {
                // 移除挂牌买入委托单
                String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_SELL, productTradeNo, price);
                redisUtil.zremove(timeKey, entrustOrder.getEntrustNo());
            } else {
                // 移除挂牌买入委托单
                String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_BUY, productTradeNo, price);
                redisUtil.zremove(timeKey, entrustOrder.getEntrustNo());
            }
            // 删除委托单信息
            String entrustInfoKey = MessageFormat.format(RedisConstant.TRADE_ENTRUSTINFO, entrustOrder.getEntrustNo());
            redisUtil.hdelete(entrustInfoKey, MapUtil.objectToMap(entrustOrder).keySet());
        }
    }

    /**
     * 扣除客户商品持仓汇总,并且同步修改客户商品持仓明细
     *
     * @param dealNo
     * @param customerNo
     * @param productTradeNo
     * @param count
     * @param holdType
     * @param operation,deduct=直接扣除,unfrozen=解冻扣除
     */
    private List<IntegralAndProfit> deductHoldTotalAndDetails(String dealNo, String customerNo, String productTradeNo,
                                                              int count, String holdType, String operation, BigDecimal price, HoldDetails holdDetails) {
        BigDecimal sellCount = new BigDecimal(count);
        // 扣除客户商品持仓汇总数据
        if ("deduct".equals(operation)) {
            boolean flag = holdTotalService.deductCustomerProductHold(customerNo, productTradeNo, count, holdType);
            if (!flag) {
                throw new ServiceException("扣除摘牌方商品持仓失败");
            }
            // 扣除客户持仓明细
            boolean detailsFlag = holdDetailsService.deduct(holdDetails, sellCount);
            if (!detailsFlag) {
                log.info("扣除客户持有失败，customerNo={}，holdDetails={}", customerNo, JSON.toJSON(holdDetails));
                throw new ServiceException("扣除客户持有失败");
            }
        } else {
            boolean flag = holdTotalService.unfrozenCustomerProductHold(customerNo, productTradeNo, count, holdType);
            if (!flag) {
                throw new ServiceException("解冻摘牌方商品持仓失败");
            }
            // 扣除客户持仓明细
            boolean detailsFlag = holdDetailsService.frozenDeduct(holdDetails, sellCount);
            if (!detailsFlag) {
                log.info("冻结扣除客户持有失败，customerNo={}，holdDetails={}", customerNo, JSON.toJSON(holdDetails));
                throw new ServiceException("扣除客户持有失败");
            }
        }
        IntegralAndProfit integralAndProfit = new IntegralAndProfit();
        // 积分获得=(卖出价-成本价) * 数量 * 转积分比例
        integralAndProfit.setIntegral((price.subtract(holdDetails.getCost())).multiply(sellCount).multiply(new BigDecimal(integralRate)));
        // 配票盈亏=卖出价-成本价 * 数量 - 积分
        integralAndProfit.setProfit((price.subtract(holdDetails.getCost())).multiply(sellCount).subtract(integralAndProfit.getIntegral()));
        // 提货券计算
        if (holdDetails.getCost().compareTo(new BigDecimal("960")) == 0 && Constants.HoldResource.PLAN.equals(holdDetails.getResource())) {
            integralAndProfit.setCouponAmount(new BigDecimal(couponAmount).multiply(sellCount));
        }
        // 兑换仓单红木基金计算
        if (holdDetails.getCost().compareTo(BigDecimal.ZERO) == 0) {
            // 计算现金收益部分=商品价格*数量-转积分部分
            BigDecimal money = price.multiply(sellCount).subtract(integralAndProfit.getIntegral());
            integralAndProfit.setHongmuFund(money.multiply(new BigDecimal(hongmuFundRate)));
        }
        List<IntegralAndProfit> list = new ArrayList<>();
        list.add(integralAndProfit);
        // 保存成交单与持仓详情关系表
        DealHoldRef ref = new DealHoldRef();
        ref.setDealNo(dealNo);
        ref.setHoldDetailsId(holdDetails.getId());
        ref.setDeductCount(sellCount);
        ref.setCreateTime(new Date());
        ref.setFlag(Byte.valueOf(Constants.Flag.VALID));
        dealHoldRefService.save(ref);
        return list;
    }

    /**
     * 保存持仓明细,修改/新增持仓汇总
     *
     * @param customerNo
     * @param productTradeNo
     * @param count
     * @param entrustNo
     */
    private void addHold(String customerNo, String productTradeNo, Integer count, String entrustNo) {
        ProductMarket productMarket = productMarketService.findByProductTradeNo(productTradeNo);
        if (productMarket == null) {
            throw new ServiceException("商品上市信息不存在");
        }
        // 1.增加商品持仓明细
        HoldDetails details = new HoldDetails();
        details.setCustomerNo(customerNo);
        details.setProductTradeNo(productTradeNo);
        details.setOriginalCount(new BigDecimal(count));
        details.setRemaindCount(new BigDecimal(count));
        details.setCost(productMarket.getIssuePrice());
        details.setTradeTime(tradeDayService.getAddNTradeDay(productMarket.getFrozenDay()));
        details.setHoldNo(entrustNo);
        details.setHoldTime(new Date());
        details.setResource(Constants.HoldResource.DEAL);
        details.setScanner(Byte.valueOf("0"));
        details.setHoldType(Constants.HoldType.MAIN);
        holdDetailsService.save(details);
        // 2.增加/修改商品持仓汇总
        HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(customerNo, productTradeNo, Constants.HoldType.MAIN);
        if (holdTotal == null) {
            HoldTotal total = new HoldTotal();
            total.setCustomerNo(customerNo);
            total.setTotalCount(new BigDecimal(count));
            total.setFrozenCount(new BigDecimal(0));
            total.setCanSellCount(new BigDecimal(0));
            total.setCost(productMarket.getIssuePrice());
            total.setHoldType(Constants.HoldType.MAIN);
            total.setProductTradeNo(productTradeNo);
            holdTotalService.save(total);
        } else {
            Condition condition = new Condition(HoldTotal.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("flag", Byte.valueOf(Constants.Flag.VALID));
            criteria.andEqualTo("customerNo", customerNo);
            criteria.andEqualTo("productTradeNo", productTradeNo);
            criteria.andEqualTo("holdType", Constants.HoldType.MAIN);
            criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
            HoldTotal newTotal = new HoldTotal();
            newTotal.setModifyTime(new Date());
            newTotal.setTotalCount(holdTotal.getTotalCount().add(new BigDecimal(count)));
            holdTotalService.updateByConditionSelective(newTotal, condition);
        }
    }

    /**
     * 摘牌买入
     *
     * @param tradeDeListDto
     * @return
     */
    public ApiResult<String> delistBuy(TradeDeListDto tradeDeListDto) {
        long start = System.currentTimeMillis();
        String customerNo = tradeDeListDto.getCustomerNo();
        int count = tradeDeListDto.getCount();
        // step1.业务参数校验
        ValidatorDataContext tradeDataContext = new ValidatorDataContext();
        tradeDataContext.setRequestData(tradeDeListDto);
        tradeDataContext.getDataContextMap().put("customerNo", tradeDeListDto.getCustomerNo());
        tradeDataContext.getDataContextMap().put("direction", tradeDeListDto.getDirection());
        tradeDataContext.getDataContextMap().put("type", "delist");
        // TradeDayValidator=开休市时间校验,CustomerTradeStatusValidator=客户交易状态校验,DeListEntrustOrderValidator=委托单校验器
        // DeListCustomerBalanceValidator=客户余额校验器,DeListCustomerAddressValidator=客户地址信息校验器
        Class[] validators = new Class[]{TradeDayValidator.class, CustomerTradeStatusValidator.class, DeListEntrustOrderValidator.class,
                DeListCustomerBalanceValidator.class, DeListCustomerAddressValidator.class, SignValidator.class, OneHundredValidator.class,
                CustomerTradeRiskValidator.class};
        validateService.setValidatos(validators);
        validateService.validate(tradeDataContext);
        EntrustOrder entrustOrder = (EntrustOrder) tradeDataContext.getDataContextMap().get("entrustOrder");
        if (entrustOrder == null) {
            throw new ServiceException("委托单不存在");
        }
        String productTradeNo = entrustOrder.getProductTradeNo();
        BigDecimal totalAmount = entrustOrder.getPrice().multiply(new BigDecimal(tradeDeListDto.getCount()));
        String dealNo = NoUtil.getDealNo();
        // step2.扣减摘牌方资金
        BigDecimal delistFee = totalAmount.multiply(new BigDecimal(buyFeeRate));
        ChangeAmountDto delistAmountDto = new ChangeAmountDto();
        delistAmountDto.setCustomerNo(customerNo);
        delistAmountDto.setChangeAmount(totalAmount.add(delistFee));    //总金额+手续费
        delistAmountDto.setOrderNo(dealNo);
        delistAmountDto.setTradeType(FundTradeTypeEnum.MAIN_BUY.getCode());
        delistAmountDto.setReType(Byte.valueOf("1"));
        accountService.changeAccount(delistAmountDto);
        log.info("扣减摘牌方资金delistAmountDto={}", JSON.toJSONString(delistAmountDto));
        // step3.增加摘牌方商品持仓
        addHold(customerNo, productTradeNo, count, entrustOrder.getEntrustNo());
        log.info("增加摘牌方商品持仓");
        //step4.解冻扣除挂牌方商品持仓汇总,并且同步修改客户商品持仓明细
        List<EntrustDetailsRef> refList = entrustDetailsRefService.findByEntrustNo(tradeDeListDto.getEntrustNo());
        if (CollectionUtils.isEmpty(refList)) {
            throw new ServiceException("委托持仓关系为空");
        }
        HoldDetails holdDetails = holdDetailsService.findById(refList.get(0).getHoldDetailsId());
        if (holdDetails == null) {
            log.info("挂牌方持有不存在，detailsId={}", holdDetails.getId());
            throw new ServiceException("挂牌方持有不存在");
        }
        List<IntegralAndProfit> integralAndProfitList = deductHoldTotalAndDetails(dealNo, entrustOrder.getCustomerNo(), entrustOrder.getProductTradeNo(),
                tradeDeListDto.getCount(), entrustOrder.getHoldType(), "unfrozen", entrustOrder.getPrice(), holdDetails);
        BigDecimal integral = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal couponAmount = BigDecimal.ZERO;
        BigDecimal hongmuFundAmount = BigDecimal.ZERO;
        // 计算成交单转积分与盈利
        for (IntegralAndProfit integralAndProfit : integralAndProfitList) {
            integral = integral.add(integralAndProfit.getIntegral());
            profit = profit.add(integralAndProfit.getProfit());
            couponAmount = couponAmount.add(integralAndProfit.getCouponAmount());
            hongmuFundAmount = hongmuFundAmount.add(integralAndProfit.getHongmuFund());
        }
        if (integral.compareTo(BigDecimal.ZERO) > 0) {
            // step3.给用户增加积分
            ChangeIntegralDto integralDto = new ChangeIntegralDto();
            integralDto.setCustomerNo(entrustOrder.getCustomerNo());
            integralDto.setChangeAmount(integral);
            integralDto.setOrderNo(dealNo);
            integralDto.setTradeType(Byte.valueOf(IntegralTradeTypeEnum.TRADING_GIVING.getCode() + ""));
            integralDto.setReType(Byte.valueOf("2"));
            integralDto.setIntegralNo(Long.valueOf(101));
            customerIntegralService.changeIntegral(integralDto);
            log.info("用户增加积分成功,integralDto={}", JSON.toJSONString(integral));
        }
        if (couponAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 将提货款给挂牌商
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(hengjiaLister);
            changeAmountDto.setChangeAmount(couponAmount);
            changeAmountDto.setOrderNo(dealNo);
            changeAmountDto.setTradeType(FundTradeTypeEnum.DELIVERYTICKET_IN.getCode());
            changeAmountDto.setReType(Byte.valueOf("2"));
            accountService.changeAccount(changeAmountDto);
            // 修改客户的提货款余额
            ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
            changeCouponAccountDto.setCustomerNo(entrustOrder.getCustomerNo());
            changeCouponAccountDto.setChangeAmount(couponAmount);
            changeCouponAccountDto.setOrderNo(dealNo);
            changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.DELIVERY_TICKET_GET.getCode());
            changeCouponAccountDto.setReType(Byte.valueOf("2"));
            changeCouponAccountDto.setProductTradeNo(productTradeNo);
            changeCouponAccountDto.setCouponType(Constants.CouponType.DELIVERYTICKET);
            couponAccountService.changeAmount(changeCouponAccountDto);
        }
        if (hongmuFundAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 将红木基金给经销商
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(distributorCustomerNo);
            changeAmountDto.setChangeAmount(hongmuFundAmount);
            changeAmountDto.setOrderNo(dealNo);
            changeAmountDto.setTradeType(FundTradeTypeEnum.MARKET_ADJUST.getCode());
            changeAmountDto.setReType(Byte.valueOf("2"));
            accountService.changeAccount(changeAmountDto);
        }
        // step5.计算挂牌方配票卖出的转积分数据
        BigDecimal listFee = totalAmount.multiply(new BigDecimal(sellFeeRate)); // 挂牌方手续费
        BigDecimal changeAmount = totalAmount.subtract(listFee).subtract(integral).subtract(couponAmount).subtract(hongmuFundAmount);
        // step7.增加挂牌方资金
        Byte tradeType = Constants.HoldType.MAIN.equals(entrustOrder.getHoldType()) ? FundTradeTypeEnum.MAIN_SELL.getCode() : FundTradeTypeEnum.MATCH_SELL.getCode();
        ChangeAmountDto listAmountDto = new ChangeAmountDto();
        listAmountDto.setCustomerNo(entrustOrder.getCustomerNo());
        listAmountDto.setChangeAmount(changeAmount);
        listAmountDto.setOrderNo(dealNo);
        listAmountDto.setTradeType(tradeType);
        listAmountDto.setReType(Byte.valueOf("2"));
        accountService.changeAccount(listAmountDto);
        log.info("增加挂牌方资金listAmountDto={}", JSON.toJSONString(listAmountDto));
        // step8.创建成交单
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(dealNo);
        dealOrder.setEntrustId(entrustOrder.getId());
        dealOrder.setProductTradeNo(entrustOrder.getProductTradeNo());
        dealOrder.setBuyCustomerNo(tradeDeListDto.getCustomerNo());
        dealOrder.setSellCustomerNo(entrustOrder.getCustomerNo());
        dealOrder.setBuyFee(delistFee);
        dealOrder.setSellFee(listFee);
        dealOrder.setProfit(profit);
        dealOrder.setIntegral(integral);
        dealOrder.setType(Constants.TradeDirection.BUY);
        dealOrder.setPrice(entrustOrder.getPrice());
        dealOrder.setCount(tradeDeListDto.getCount());
        if (tradeDeListDto.getResource() == null) {
            dealOrder.setResource(DealOrderResourceEnum.NORMAL.getCode());
        } else {
            dealOrder.setResource(tradeDeListDto.getResource());
        }
        // 摘牌买入固定为本票
        dealOrder.setHoldType(Constants.HoldType.MAIN);
        dealOrder.setIntegralReturnFlag("no");
        dealOrder.setCouponAmount(couponAmount);
        dealOrder.setHongmuFund(hongmuFundAmount);
        dealOrderService.save(dealOrder);
        log.info("创建成交单,dealOrder={}", JSON.toJSONString(dealOrder));
        // step9.修改委托单的成交信息
        updateEntrustOrder(entrustOrder, tradeDeListDto.getCount().toString());
        // 委托单委托时间是当天才打标识
        if (DateUtil.isToday(entrustOrder.getEntrustTime())) {
            String sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
            String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, entrustOrder.getCustomerNo(), sellHoldType);
            redisUtil.set(key, tradeDeListDto.getCount());
            redisUtil.expireAt(key, DateUtil.getEndDay());
        }
        //发送短信
        executor.submit(() -> {
            // 发送委托者委托单成交短信
            boolean entrustSmsFlag = isNotNeedSendSms(entrustOrder.getCustomerNo());
            if (!entrustSmsFlag) {
                CustomerVo customerVo = customerService.findUserByCustomerNo(entrustOrder.getCustomerNo());
                String sms = smsUtil.operatorSms("2", "2", RandomUtils.getRandomNumber(6), customerVo.getMobile());
                pubSmsService.getSms(customerVo.getMobile(), sms);
            }
            // 预约自动买入成交短信
            if (DealOrderResourceEnum.AUTO_TRADE.getCode().equals(tradeDeListDto.getResource())) {
                boolean dealSmsFlag = isNotNeedSendSms(dealOrder.getBuyCustomerNo());
                if (!dealSmsFlag) {
                    CustomerVo customerVo = customerService.findUserByCustomerNo(dealOrder.getBuyCustomerNo());
                    pubSmsService.sendNormalSms(customerVo.getMobile(), autoTradeBuySms);
                }
            }
        });
        log.info("delistSell time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success("摘牌买入成功");
    }

    /**
     * 是否需要发交易短信通知
     *
     * @param customerNo
     * @return
     */
    private boolean isNotNeedSendSms(String customerNo) {
        String str[] = msgCustomerNo.split(",");
        List<String> list = Arrays.asList(str);
        if (!CollectionUtils.isEmpty(list) && list.contains(customerNo)) {
            return true;
        }
        return false;
    }

    /**
     * 卖出校验
     *
     * @param customerBaseDto
     * @return
     */
    public ApiResult sellValidate(CustomerBaseDto customerBaseDto) {
        String customerNo = customerBaseDto.getCustomerNo();
        // 校验是否在白名单内
        boolean whiteFlag = autoWhiteListService.isWhiteList(customerNo);
        if (whiteFlag) {
            return ApiResult.success();
        }
        // 校验是否签约
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo);
        if (signingRecord == null) {
            return new ApiResult(ResultEnum.NOT_SIGN);
        }
        // 校验是否填写地址
        List<CustomerAddress> list = customerAddressService.findByCustomerNo(customerNo);
        if (CollectionUtils.isEmpty(list)) {
            return new ApiResult(ResultEnum.NO_ADDRESS);
        }
        // 校验客户风控
        CustomerVo customerVo = customerService.findUserByCustomerNo(customerNo);
        if (CustomerStatusEnum.LIMIT_SELL.getCode() == Integer.parseInt(customerVo.getCustomerStatus())) {
            return ApiResult.badParam("账号状态异常，请联系客服");
        }
        // 挂牌商超级用户不允许卖出交易
        if (customerVo.getCustomerType().intValue() == 2) {
            return ApiResult.badParam("账号状态异常，请联系客服");
        }
        // 校验是否在黑名单内
        boolean blackFlag = autoBlacklistService.isBlackList(customerNo);
        if (blackFlag) {
            return ApiResult.badParam("账号状态异常，请联系客服");
        }
        // 是否开启智能购销
        AutoConfig autoConfig = autoConfigService.findByCustomerNo(customerNo);
        if (autoConfig != null && "on".equals(autoConfig.getStatus())) {
            return new ApiResult(ResultEnum.AOTO_TRADE_ON);
        } else {
            return new ApiResult(ResultEnum.AUTO_TRADE_OFF);
        }
    }
}
