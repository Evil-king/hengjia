package com.baibei.hengjia.api.modules.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.match.model.BuymatchLog;
import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.trade.bean.dto.*;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradeCountVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradePageVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.biz.TradeBiz;
import com.baibei.hengjia.api.modules.trade.dao.AutoConfigMapper;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.utils.TradeUtil;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.DealOrderResourceEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoConfig服务实现
 */
@Service
@Slf4j
public class AutoConfigServiceImpl extends AbstractService<AutoConfig> implements IAutoConfigService {
    @Autowired
    private AutoConfigMapper tblTraAutoConfigMapper;

    @Value("${auto.trade.config}")
    private String autoTradeConfig;

    @Value("${auto.trade.notice}")
    private String autoTradeNotice;

    @Value("${auto.trade.validate.balance}")
    private String autoTradeValidateBalance;

    @Value("${auto.trade.saveConfig.notice}")
    private String saveConfigNotice;

    @Value("${auto.trade.defaultBuyCount}")
    private String defaultBuyCount;

    @Value("${auto.trade.defaultBuyPrice}")
    private String defaultBuyPrice;

    @Value("${auto.trade.switch}")
    private String autoTradeSwitch;

    @Value("${auto.trade.closing.day}")
    private String autoTradeClosingDay;

    @Value("${auto.trade.batch.count}")
    private String autoTradeBatchCount;

    @Value("${auto_trade.default.closing.date}")
    private String closingDate;

    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAutoBlacklistService autoBlacklistService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private TradeBiz tradeBiz;
    @Autowired
    private IAutoRecordService autoRecordService;
    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private ITradeWhiteListService tradeWhiteListService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public AutoConfig findByCustomerNo(String customerNo) {
        Condition condition = new Condition(AutoConfig.class);
        buildValidCriteria(condition).andEqualTo("customerNo", customerNo);
        return findOneByCondition(condition);
    }

    @Override
    public ApiResult<AutoTradePageVo> getPageInfo(String customerNo) {
        AutoTradePageVo vo;
        try {
            vo = JSON.parseObject(autoTradeConfig, AutoTradePageVo.class);
        } catch (Exception e) {
            log.error("json转换异常", e);
            return ApiResult.error("数据配置异常");
        }
        // 默认为关闭
        vo.setStatus("off");
        // 设置配置信息
        AutoConfig autoConfig = findByCustomerNo(customerNo);
        if (autoConfig != null) {
            vo.setAutoBuyPrice(autoConfig.getAutoBuyPrice());
            vo.setAutoBuyCount(autoConfig.getAutoBuyCount());
            vo.setAutoSellPrice(autoConfig.getAutoSellPrice());
            vo.setAutoSellCount(autoConfig.getAutoSellCount());
            vo.setStatus(autoConfig.getStatus());
        }
        vo.setDefaultBuyCount(Integer.valueOf(defaultBuyCount));
        vo.setSaveNotice(saveConfigNotice);
        // 设置客户实名信息
        SigningRecord signingRecord = signingRecordService.findByThirdCustId(customerNo);
        if (signingRecord == null) {
            return ApiResult.error("客户不存在实名信息");
        }
        vo.setUserName(signingRecord.getCustName());
        vo.setCurrentDate(DateUtil.yyyyMMddWithLine.get().format(new Date()));
        // 设置截止日期
        vo.setClosingTime(closingDate);
        // 设置说明
        vo.setNoticeList(Arrays.asList(autoTradeNotice.split("&&")));
        return ApiResult.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult saveConfig(AutoTradeSaveDto autoTradeSaveDto) {
        boolean isTransferWhiteList = tradeWhiteListService.isWhiteList(autoTradeSaveDto.getCustomerNo(), Constants.TradeWhitelistType.TRANSFER_SELL);
        if (isTransferWhiteList) {
            return ApiResult.badParam("开启失败，请联系客服");
        }
        AutoConfig existAutoConfig = findByCustomerNo(autoTradeSaveDto.getCustomerNo());
        if (existAutoConfig != null) {
            // 先删除原先的
            this.softDeleteById(existAutoConfig.getId());
        }
        AutoTradePageVo vo;
        try {
            vo = JSON.parseObject(autoTradeConfig, AutoTradePageVo.class);
        } catch (Exception e) {
            log.error("json转换异常", e);
            return ApiResult.error("数据配置异常");
        }
        AutoConfig config = new AutoConfig();
        config.setCustomerNo(autoTradeSaveDto.getCustomerNo());
        config.setProductTradeNo(autoTradeSaveDto.getProductTradeNo());
        config.setStatus("off");
        config.setAutoBuyPrice(autoTradeSaveDto.getAutoBuyPrice());
        config.setAutoBuyCount(autoTradeSaveDto.getAutoBuyCount());
        config.setAutoSellPrice(autoTradeSaveDto.getAutoSellPrice());
        config.setAutoSellCount(autoTradeSaveDto.getAutoSellCount());
        try {
            config.setClosingTime(DateUtil.yyyyMMddHHmmssWithLine.get().parse(closingDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        config.setProductTradeName(vo.getProductTradeName());
        this.save(config);
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult operate(AutoTradeOperateDto autoTradeOperateDto) {
        AutoConfig existAutoConfig = findByCustomerNo(autoTradeOperateDto.getCustomerNo());
        // 关闭
        if ("off".equals(autoTradeOperateDto.getStatus())) {
            if (existAutoConfig == null) {
                return ApiResult.error("开启记录不存在");
            }
            existAutoConfig.setStatus("off");
            existAutoConfig.setModifyTime(new Date());
            this.update(existAutoConfig);
            // 开启
        } else {
            boolean isTransferWhiteList = tradeWhiteListService.isWhiteList(autoTradeOperateDto.getCustomerNo(), Constants.TradeWhitelistType.TRANSFER_SELL);
            if (isTransferWhiteList) {
                return ApiResult.badParam("开启失败，请联系客服");
            }
            // 校验余额
            Account account = accountService.checkAccount(autoTradeOperateDto.getCustomerNo());
            if (account == null) {
                return ApiResult.badParam("账户信息不存在");
            }
            if (account.getBalance().compareTo(new BigDecimal(autoTradeValidateBalance)) < 0) {
                return new ApiResult(ResultEnum.AUTO_TRADE_BALANCE_NOT_ENOUGHT.getCode(), autoTradeValidateBalance);
            }
            if (existAutoConfig != null) {
                existAutoConfig.setStatus("on");
                existAutoConfig.setModifyTime(new Date());
                this.update(existAutoConfig);
            } else {
                AutoTradePageVo vo;
                try {
                    vo = JSON.parseObject(autoTradeConfig, AutoTradePageVo.class);
                } catch (Exception e) {
                    log.error("json转换异常", e);
                    return ApiResult.error("数据配置异常");
                }
                AutoConfig config = BeanUtil.copyProperties(vo, AutoConfig.class);
                config.setCustomerNo(autoTradeOperateDto.getCustomerNo());
                config.setStatus("on");
                try {
                    config.setClosingTime(DateUtil.yyyyMMddHHmmssWithLine.get().parse(closingDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                this.save(config);
            }
        }
        return ApiResult.success();
    }

    @Override
    public void closingTime() {
        List<Long> ids = new ArrayList<>();
        List<AutoConfig> autoConfigList = tblTraAutoConfigMapper.closingTimeList();
        if (CollectionUtils.isEmpty(autoConfigList)) {
            return;
        }
        for (AutoConfig item : autoConfigList) {
            ids.add(item.getId());
        }
        Condition condition = new Condition(AutoConfig.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andIn("id", ids);
        AutoConfig autoConfig = new AutoConfig();
        autoConfig.setStatus("off");
        autoConfig.setModifyTime(new Date());
        updateByConditionSelective(autoConfig, condition);
    }

    /**
     * 不加入事务控制，避免其中一笔单失败，导致全部数据回滚
     *
     * @return
     */
    @Override
    public ApiResult trade() {
        // step1.判断全局开关
        if (StringUtils.isEmpty(autoTradeSwitch) || !"on".equals(autoTradeSwitch)) {
            return ApiResult.badParam("自动购销功能关闭");
        }
        // step2.判断开休市
        boolean tradeTime = tradeDayService.isTradeTime();
        if (!tradeTime) {
            return ApiResult.badParam("非交易时间");
        }
        // step3.获取自动购销配置客户列表
        List<AutoConfig> autoConfigList = getValidList();
        if (CollectionUtils.isEmpty(autoConfigList)) {
            log.info("自动购销配置列表为空");
            return ApiResult.success();
        }
        // step4.获取满足自动买入、自动卖出的客户队列
        // 自动摘牌买入队列
        List<AutoTradeCountVo> buyAutoConfigList = new LinkedList<>();
        // 自动摘牌卖出队列
        List<AutoTradeCountVo> sellAutoConfigList = new LinkedList<>();
        for (AutoConfig autoConfig : autoConfigList) {
            log.info("autoConfig={}正在执行", JSON.toJSONString(autoConfig));
            boolean isBlack = autoBlacklistService.isBlackList(autoConfig.getCustomerNo());
            if (isBlack) {
                log.info("客户{}在黑名单内，不予执行。", autoConfig.getCustomerNo());
                continue;
            }
            AutoTradeCountVo buyVo = getAutoBuyData(autoConfig);
            if (buyVo != null) {
                buyAutoConfigList.add(buyVo);
            }
            AutoTradeCountVo sellVo = getAutoSellData(autoConfig);
            if (sellVo != null) {
                sellAutoConfigList.add(sellVo);
            }
        }
        // 控制每次执行的数量
        if (!CollectionUtils.isEmpty(buyAutoConfigList) && buyAutoConfigList.size() > Integer.valueOf(autoTradeBatchCount)) {
            buyAutoConfigList = buyAutoConfigList.subList(0, Integer.valueOf(autoTradeBatchCount));
        }
        if (!CollectionUtils.isEmpty(sellAutoConfigList) && sellAutoConfigList.size() > Integer.valueOf(autoTradeBatchCount)) {
            sellAutoConfigList = sellAutoConfigList.subList(0, Integer.valueOf(autoTradeBatchCount));
        }
        // step5.自动买入、卖出
        autoBuy(buyAutoConfigList);
        autoSell(sellAutoConfigList);
        return ApiResult.success();
    }

    /**
     * 判断客户是否满足自动买入的条件
     *
     * @param autoConfig
     */
    public AutoTradeCountVo getAutoBuyData(AutoConfig autoConfig) {
        String customerNo = autoConfig.getCustomerNo();
        // 判断自动买入价格配置是否正确
        BigDecimal bigDefaultBuyPrice = new BigDecimal(defaultBuyPrice);
        if (bigDefaultBuyPrice.compareTo(autoConfig.getAutoBuyPrice()) != 0) {
            log.info("客户{}配置自动买入的价格为{}，不予执行", customerNo, autoConfig.getAutoBuyPrice());
            return null;
        }
        // 判断今日是否买入指定手数
        Integer dealCount = dealOrderService.sumBuyCount(customerNo, autoConfig.getProductTradeNo(), DateUtil.yyyyMMddWithLine.get().format(new Date()));
        log.info("客户{}今日买入{}手，自动买入配置{}手", customerNo, dealCount, autoConfig.getAutoBuyCount());
        if (dealCount != null && dealCount >= autoConfig.getAutoBuyCount()) {
            return null;
        }
        // 判断是否有委托单委托买入
        List<EntrustOrder> entrustOrderList = entrustOrderService.findNotDealEntrustOrder(customerNo, autoConfig.getProductTradeNo(), Constants.TradeDirection.BUY);
        if (!CollectionUtils.isEmpty(entrustOrderList)) {
            log.info("客户{}已委托买入", customerNo);
            return null;
        }
        // 判断余额是否足够
        Account account = accountService.checkAccount(customerNo);
        if (account == null) {
            log.info("账户记录不存在，customerNo={}", customerNo);
            return null;
        }
        int canBuyCount = calculationCanBuyCount(account.getBalance());
        if (canBuyCount <= 0) {
            log.info("客户{}余额不足，余额值为{}", customerNo, account.getBalance());
            return null;
        }
        // 计算客户剩余能买入的数量
        int restBuyCount = (autoConfig.getAutoBuyCount() - dealCount) > canBuyCount ? canBuyCount : (autoConfig.getAutoBuyCount() - dealCount);
        AutoTradeCountVo vo = new AutoTradeCountVo();
        vo.setCustomerNo(customerNo);
        vo.setProductTradeNo(autoConfig.getProductTradeNo());
        vo.setCount(restBuyCount);
        vo.setAutoPrice(autoConfig.getAutoBuyPrice());
        vo.setAutoCount(autoConfig.getAutoBuyCount());
        log.info("客户{}满足自动买入的条件，余额为{}，买入数量为{}", customerNo, account.getBalance(), restBuyCount);
        return vo;
    }


    /**
     * 判断客户是否满足自动卖出的条件
     *
     * @param autoConfig
     * @return
     */
    public AutoTradeCountVo getAutoSellData(AutoConfig autoConfig) {
        String customerNo = autoConfig.getCustomerNo();
        // 判断自动买入价格配置是否正确
        BigDecimal bigDefaultBuyPrice = new BigDecimal(defaultBuyPrice);
        if (bigDefaultBuyPrice.compareTo(autoConfig.getAutoSellPrice()) != 0) {
            log.info("客户{}配置自动卖出的价格为{}，不予执行", customerNo, autoConfig.getAutoSellPrice());
            return null;
        }
        // 判断是否已经买入零售商品
        boolean isBuy = dealOrderService.isBuyToday(customerNo);
        if (!isBuy) {
            log.info("客户{}今日未买入零售商品，不予执行", customerNo);
            return null;
        }
        // 判断是否已经配票
        List<BuymatchLog> buymatchLogList = buymatchLogService.findBy(autoConfig.getProductTradeNo(),
                DateUtil.yyyyMMddNoLine.get().format(new Date()), customerNo, new BigDecimal("960"));
        if (CollectionUtils.isEmpty(buymatchLogList)) {
            log.info("客户{}今日未配票，不予执行", customerNo);
            return null;
        }
        String productTradeNo = autoConfig.getProductTradeNo();
        // 本票
        HoldTotal mainHold = holdTotalService.findByCustomerAndProductNo(customerNo, productTradeNo, Constants.HoldType.MAIN);
        BigDecimal mainCount = mainHold == null ? BigDecimal.ZERO : mainHold.getCanSellCount();
        // 配票
        HoldTotal matchHold = holdTotalService.findByCustomerAndProductNo(customerNo, productTradeNo, Constants.HoldType.MATCH);
        BigDecimal matchCount = matchHold == null ? BigDecimal.ZERO : matchHold.getCanSellCount();
        BigDecimal totalCount = mainCount.add(matchCount);
        if (totalCount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("客户{}商品持仓小于等于0", customerNo);
            return null;
        }
        // 已卖出数
        Integer dealSellCount = dealOrderService.sumSellCount(customerNo, productTradeNo, DateUtil.yyyyMMddWithLine.get().format(new Date()));
        // 已委托数
        Integer entrustSellCount = 0;
        List<EntrustOrder> entrustOrderList = entrustOrderService.findNotDealEntrustOrder(customerNo, productTradeNo, Constants.TradeDirection.SELL);
        for (EntrustOrder entrustOrder : entrustOrderList) {
            entrustSellCount = entrustSellCount + (entrustOrder.getEntrustCount() - entrustOrder.getDealCount());
        }
        // 计算剩余可卖数=预约卖出数-成交卖出数-卖出委托数
        Integer restCount = autoConfig.getAutoSellCount() - dealSellCount - entrustSellCount;
        if (restCount > totalCount.intValue()) {
            restCount = totalCount.intValue();
        }
        if (restCount <= 0) {
            log.info("客户{}剩余可卖数小于等于0，持仓总数为{}，已卖出数为{}，已委托数为{}，预约配置卖出数为{}", customerNo, totalCount, dealSellCount, entrustSellCount, autoConfig.getAutoSellCount());
            return null;
        }
        AutoTradeCountVo vo = new AutoTradeCountVo();
        vo.setCustomerNo(customerNo);
        vo.setProductTradeNo(autoConfig.getProductTradeNo());
        vo.setCount(restCount);
        vo.setAutoPrice(autoConfig.getAutoSellPrice());
        vo.setAutoCount(autoConfig.getAutoSellCount());
        log.info("客户{}满足自动卖出的条件，卖出数量为{}", customerNo, restCount);
        return vo;
    }

    /**
     * 自动买入
     *
     * @param list
     */
    public void autoBuy(List<AutoTradeCountVo> list) {
        log.info("开始执行自动买入，本次执行的的list为{}", JSON.toJSONString(list));
        if (CollectionUtils.isEmpty(list)) {
            log.info("本次执行自动买入集合为空");
            return;
        }
        String productTradeNo = list.get(0).getProductTradeNo();
        // 找挂牌卖出的委托单列表
        EntrustAllListDto entrustAllListDto = new EntrustAllListDto();
        entrustAllListDto.setProductTradeNo(productTradeNo);
        entrustAllListDto.setDirection(Constants.TradeDirection.SELL);
        entrustAllListDto.setCurrentPage(1);
        entrustAllListDto.setPageSize(Integer.MAX_VALUE);
        MyPageInfo<EntrustOrderVo> myPageInfo = entrustOrderService.allList(entrustAllListDto);
        if (myPageInfo == null || CollectionUtils.isEmpty(myPageInfo.getList())) {
            log.info("当前挂牌卖出的委托单列表为空");
            return;
        }
        // 委托单未成交的数量按照一手去切分
        List<EntrustOrderVo> entrustOrderVoList = myPageInfo.getList();
        List<String> entrustOrderNoList = new LinkedList<>();
        for (EntrustOrderVo entrustOrder : entrustOrderVoList) {
            // 这里的entrustOrder.getEntrustCount()是当前剩余委托数
            for (int i = 0; i < entrustOrder.getEntrustCount(); i++) {
                entrustOrderNoList.add(entrustOrder.getEntrustNo());
            }
        }
        // 成交单按照每一手去成交
        Integer autoBuyTotalCount = 0;
        for (AutoTradeCountVo autoTradeCountVo : list) {
            autoBuyTotalCount = autoBuyTotalCount + autoTradeCountVo.getCount();
        }
        int count;
        int index = 0;
        String entrustNo;
        TradeDeListDto tradeDeListDto = new TradeDeListDto();
        tradeDeListDto.setCount(1);
        tradeDeListDto.setDirection(Constants.TradeDirection.BUY);
        tradeDeListDto.setResource(DealOrderResourceEnum.AUTO_TRADE.getCode());
        tradeDeListDto.setOneHundred(1);
        for (AutoTradeCountVo autoTradeCountVo : list) {
            count = autoTradeCountVo.getCount();
            for (int i = 0; i < count; i++) {
                if (index >= entrustOrderNoList.size()) {
                    log.info("委托单数量不足，委托单可卖出的总数量为{}，此次自动买入的数量为{}", entrustOrderNoList.size(), autoBuyTotalCount);
                    break;
                }
                entrustNo = entrustOrderNoList.get(index);
                tradeDeListDto.setCustomerNo(autoTradeCountVo.getCustomerNo());
                tradeDeListDto.setEntrustNo(entrustNo);
                log.info("客户{}开始执行自动买入，委托单号为：{}", autoTradeCountVo.getCustomerNo(), entrustNo);
                ApiResult apiResult;
                try {
                    apiResult = tradeBiz.delistBuy(tradeDeListDto);
                    if (apiResult.hasSuccess()) {
                        // 写入自动预约购销记录
                        AutoRecord record = new AutoRecord();
                        record.setCustomerNo(autoTradeCountVo.getCustomerNo());
                        record.setProductTradeNo(autoTradeCountVo.getProductTradeNo());
                        record.setEntrustNo(entrustNo);
                        record.setType("buy");
                        record.setAutoPrice(autoTradeCountVo.getAutoPrice());
                        record.setAutoCount(1);
                        autoRecordService.save(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("客户：{}自动买入异常", autoTradeCountVo.getCustomerNo());
                    index++;
                    continue;
                }
                log.info("客户{}结束执行自动买入，结果为{}", autoTradeCountVo.getCustomerNo(), apiResult.toString());
                index++;
            }
        }
    }


    /**
     * 自动卖出
     *
     * @param list
     */
    public void autoSell(List<AutoTradeCountVo> list) {
        log.info("开始执行自动卖出，本次执行的的list为{}", JSON.toJSONString(list));
        if (CollectionUtils.isEmpty(list)) {
            log.info("本次执行自动卖出集合为空");
            return;
        }
        String productTradeNo = list.get(0).getProductTradeNo();
        // 找挂牌卖出的委托单列表
        EntrustAllListDto entrustAllListDto = new EntrustAllListDto();
        entrustAllListDto.setProductTradeNo(productTradeNo);
        entrustAllListDto.setDirection(Constants.TradeDirection.BUY);
        entrustAllListDto.setCurrentPage(1);
        entrustAllListDto.setPageSize(Integer.MAX_VALUE);
        MyPageInfo<EntrustOrderVo> myPageInfo = entrustOrderService.allList(entrustAllListDto);
        List<String> entrustOrderNoList = new LinkedList<>();
        if (myPageInfo == null || CollectionUtils.isEmpty(myPageInfo.getList())) {
            log.info("当前挂牌买入的委托单列表为空");
        } else {
            // 委托单未成交的数量按照一手去切分
            List<EntrustOrderVo> entrustOrderVoList = myPageInfo.getList();
            for (EntrustOrderVo entrustOrder : entrustOrderVoList) {
                // 这里的entrustOrder.getEntrustCount()是当前剩余委托数
                for (int i = 0; i < entrustOrder.getEntrustCount(); i++) {
                    entrustOrderNoList.add(entrustOrder.getEntrustNo());
                }
            }
        }
        // 成交单按照每一手去成交
        Integer autoBuyTotalCount = 0;
        for (AutoTradeCountVo autoTradeCountVo : list) {
            autoBuyTotalCount = autoBuyTotalCount + autoTradeCountVo.getCount();
        }
        int count;
        int index = 0;
        String entrustNo;
        TradeDeListDto tradeDeListDto = new TradeDeListDto();
        tradeDeListDto.setCount(1);
        tradeDeListDto.setDirection(Constants.TradeDirection.SELL);
        tradeDeListDto.setResource(DealOrderResourceEnum.AUTO_TRADE.getCode());
        tradeDeListDto.setOneHundred(1);

        TradeListDto tradeListDto = new TradeListDto();
        tradeListDto.setPrice("1160");
        tradeListDto.setCount(1);
        tradeListDto.setDirection(Constants.TradeDirection.SELL);
        tradeListDto.setOneHundred(1);
        for (AutoTradeCountVo autoTradeCountVo : list) {
            count = autoTradeCountVo.getCount();
            for (int i = 0; i < count; i++) {
                log.info("当前自动卖出执行客户为{}", autoTradeCountVo.getCustomerNo());
                if (index >= entrustOrderNoList.size()) {
                    log.info("委托单数量不足，委托单可买入的总数量为{}，此次自动卖出的数量为{}，开始挂牌委托单", entrustOrderNoList.size(), autoBuyTotalCount);
                    tradeListDto.setCustomerNo(autoTradeCountVo.getCustomerNo());
                    tradeListDto.setProductTradeNo(autoTradeCountVo.getProductTradeNo());
                    // 挂卖委托单
                    log.info("客户{}开始挂卖委托单", autoTradeCountVo.getCustomerNo());
                    HoldDetails holdDetails = getRightHoldDetails(autoTradeCountVo.getCustomerNo(), autoTradeCountVo.getProductTradeNo());
                    if (holdDetails == null) {
                        log.info("客户{}暂无可卖的持仓明细", autoTradeCountVo.getCustomerNo());
                        continue;
                    }
                    tradeListDto.setId(holdDetails.getId());
                    ApiResult apiResult;
                    try {
                        apiResult = tradeBiz.listSell(tradeListDto);
                    } catch (Exception e) {
                        log.error("客户挂牌卖出异常", e);
                        continue;
                    }
                    log.info("客户{}结束挂卖委托单，结果为{}", autoTradeCountVo.getCustomerNo(), apiResult.toString());
                    continue;
                }
                entrustNo = entrustOrderNoList.get(index);
                log.info("客户{}开始执行自动卖出，委托单号为：{}", autoTradeCountVo.getCustomerNo(), entrustNo);
                tradeDeListDto.setCustomerNo(autoTradeCountVo.getCustomerNo());
                tradeDeListDto.setEntrustNo(entrustNo);
                HoldDetails holdDetails = getRightHoldDetails(autoTradeCountVo.getCustomerNo(), autoTradeCountVo.getProductTradeNo());
                if (holdDetails == null) {
                    log.info("客户{}暂无可卖的持仓明细", autoTradeCountVo.getCustomerNo());
                    continue;
                }
                tradeDeListDto.setId(holdDetails.getId());
                ApiResult apiResult;
                try {
                    apiResult = tradeBiz.delistSell(tradeDeListDto);
                    if (apiResult.hasSuccess()) {
                        // 写入自动预约购销记录
                        AutoRecord record = new AutoRecord();
                        record.setCustomerNo(autoTradeCountVo.getCustomerNo());
                        record.setProductTradeNo(autoTradeCountVo.getProductTradeNo());
                        record.setEntrustNo(entrustNo);
                        record.setType("sell");
                        record.setAutoPrice(autoTradeCountVo.getAutoPrice());
                        record.setAutoCount(1);
                        autoRecordService.save(record);
                    }
                } catch (Exception e) {
                    log.info("客户：{}自动卖出异常", autoTradeCountVo.getCustomerNo());
                    index++;
                    continue;
                }
                log.info("客户{}结束执行自动卖出，结果为{}", autoTradeCountVo.getCustomerNo(), apiResult.toString());
                index++;
            }
        }
    }

    /**
     * 获取客户可卖的持仓明细
     *
     * @param customerNo
     * @return
     */
    private HoldDetails getRightHoldDetails(String customerNo, String productTradeNo) {
        List<HoldDetails> holdDetailsList = holdDetailsService.listByParamsForTrade(customerNo, productTradeNo, null);
        if (CollectionUtils.isEmpty(holdDetailsList)) {
            log.info("客户{}当前没有可卖持仓明细", customerNo);
            return null;
        }
        String sellHoldType;
        for (HoldDetails holdDetails : holdDetailsList) {
            sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
            // 零售&&折扣商品需要校验
            if (Constants.SellHoldType.MAIN.equals(sellHoldType) || Constants.SellHoldType.MATCH.equals(sellHoldType)) {
                // 判断是否是白名单
                boolean whitelistFlag = tradeWhiteListService.isWhiteList(customerNo, Constants.TradeWhitelistType.TODAY_SELL);
                if (whitelistFlag) {
                    return holdDetails;
                }
                String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, customerNo, sellHoldType);
                String res = redisUtil.get(key);
                if (StringUtils.isEmpty(res)) {
                    return holdDetails;
                }
            } else {
                // 兑换仓单直接返回
                return holdDetails;
            }
        }
        return null;
    }

    /**
     * 计算客户余额足够买几手商品
     *
     * @param balance
     * @return
     */
    public int calculationCanBuyCount(BigDecimal balance) {
        return balance.intValue() / Integer.valueOf(defaultBuyPrice);
    }


    @Override
    public List<AutoConfig> getValidList() {
        return tblTraAutoConfigMapper.getValidList();
    }
}
