package com.baibei.hengjia.api.modules.trade.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.trade.bean.vo.IntegralAndProfit;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.DealHoldRef;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/14 17:00
 * @description:
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class ExchangeBiz {
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private HoldTotalMapper tblTraHoldTotalMapper;
    @Autowired
    private ICouponAccountService couponAccountService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IDealHoldRefService dealHoldRefService;

    private final String newProductTradeNo = "0002";
    private final String oldProductTradeNo = "0001";
    private final BigDecimal base = new BigDecimal("58");
    private final BigDecimal releaseCount = new BigDecimal("2");


    /**
     * 持仓置换
     */
    public void exchange() {
        log.info("开始置换");
        long start = System.currentTimeMillis();
        // 查询所有客户持仓汇总信息
        List<HoldTotal> holdTotalList = tblTraHoldTotalMapper.selectAll();
        BigDecimal totalCount, oldTotalCount;
        String customerNo;
        HoldTotal holdTotal;
        BigDecimal exchangeCoupon;
        for (int i = 0; i < holdTotalList.size(); i++) {
            holdTotal = holdTotalList.get(i);
            customerNo = holdTotal.getCustomerNo();
            log.info("当前执行的条数为{}，客户为{}", i, customerNo);
            if ("1".equals(customerNo) || "2578".equals(customerNo)) {
                log.info("客户编号为{}，忽略", customerNo);
                continue;
            }
            oldTotalCount = holdTotal.getTotalCount();
            // 判断客户持仓汇总是否小于等于0
            if (oldTotalCount.compareTo(BigDecimal.ZERO) <= 0) {
                log.info("客户{}持仓汇总总数为{}，不予执行", customerNo, oldTotalCount);
                continue;
            }
            // step1.客户0001商品持仓汇总数据重置为0
            rest0001HoldTotal(holdTotal);
            // step2.客户0001商品持仓类型的明细数据重置为0
            rest0001HoldDetails(customerNo, holdTotal.getHoldType());
            // step3.客户新增0001商品卖出的成交单
            saveDealOrder("1", customerNo, holdTotal.getProductTradeNo(), Constants.TradeDirection.TRANSFER,
                    oldTotalCount.intValue(), holdTotal.getHoldType(), new BigDecimal("20"));
            // step4.挂牌商1持仓汇总增加客户卖出数量的0001商品，并增加一条持仓明细
            addHold("1", holdTotal.getProductTradeNo(), oldTotalCount.intValue(), UUID.randomUUID().toString().replace("-", ""));
            // case1.处理本票
            if (Constants.HoldType.MAIN.equals(holdTotal.getHoldType())) {
                totalCount = getInteger(oldTotalCount, base);
                // case1.1.持仓总数除以58之后的值，在大于0的情况下才新增0002商品的持仓汇总
                if (totalCount.compareTo(BigDecimal.ZERO) > 0) {
                    // step1.新增0002商品持仓汇总数据
                    addHoldTotal(customerNo, totalCount);
                    // step2.每天解锁2的数量
                    releaseHoldDetailsByDay(customerNo, totalCount);
                    // step3. 新增买入成交单
                    String dealNo = saveDealOrder(customerNo, "1", newProductTradeNo, Constants.TradeDirection.TRANSFER,
                            totalCount.intValue(), Constants.HoldType.MAIN, new BigDecimal("1160"));
                    // step4.从挂牌商扣除0002商品的指定数量
                    deductHoldTotalAndDetails(dealNo, "1", newProductTradeNo, totalCount.intValue(), Constants.HoldType.MAIN);
                }
                exchangeCoupon = oldTotalCount.subtract(totalCount.multiply(base));
                // case2.处理配票
            } else {
                exchangeCoupon = oldTotalCount;
            }
            // step6.剩余部分转换成券
            coupon(customerNo, exchangeCoupon);
        }
        log.info("置换结束，耗时{}", System.currentTimeMillis() - start);
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
        // 1.增加商品持仓明细
        HoldDetails details = new HoldDetails();
        details.setCustomerNo(customerNo);
        details.setProductTradeNo(productTradeNo);
        details.setOriginalCount(new BigDecimal(count));
        details.setRemaindCount(new BigDecimal(count));
        details.setCost(BigDecimal.ZERO);
        details.setTradeTime(new Date());
        details.setHoldNo(entrustNo);
        details.setHoldTime(new Date());
        details.setResource(Constants.HoldResource.TRANSFER);
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
            total.setCost(BigDecimal.ZERO);
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
     * 扣除商品持仓
     *
     * @param dealNo
     * @param customerNo
     * @param productTradeNo
     * @param count
     * @param holdType
     */
    private void deductHoldTotalAndDetails(String dealNo, String customerNo, String productTradeNo, int count, String holdType) {
        BigDecimal sellCount = new BigDecimal(count);
        // step1.扣除客户商品持仓汇总数据
        boolean flag = holdTotalService.deductCustomerProductHold(customerNo, productTradeNo, count, holdType);
        if (!flag) {
            throw new ServiceException("扣除摘牌方商品持仓失败");
        }
        // step2.扣除客户商品持仓明细数量
        List<HoldDetails> detailsList = holdDetailsService.listByParams(customerNo, productTradeNo, holdType, "trade_time", "asc");
        if (CollectionUtils.isEmpty(detailsList)) {
            throw new ServiceException("客户商品持仓详情为空");
        }
        List<HoldDetails> payDetailsList = new LinkedList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (HoldDetails details : detailsList) {
            payDetailsList.add(details);
            total = total.add(details.getRemaindCount());
            if (total.compareTo(sellCount) >= 0) {
                break;
            }
        }
        if (total.compareTo(sellCount) < 0) {
            throw new ServiceException("客户商品持仓详情数量不足");
        }
        int size = payDetailsList.size();
        // 所有明细的剩余数量汇总
        BigDecimal allRemindCount = BigDecimal.ZERO;
        // 每个明细卖出的数
        for (int i = 0; i < size; i++) {
            IntegralAndProfit integralAndProfit = new IntegralAndProfit();
            HoldDetails details = payDetailsList.get(i);
            allRemindCount = allRemindCount.add(details.getRemaindCount());
            // 在最后一条之前的持仓明细全部扣除
            if (i != (size - 1)) {
                details.setRemaindCount(new BigDecimal(0));
                details.setModifyTime(new Date());
                holdDetailsService.update(details);
            } else {
                details.setRemaindCount(allRemindCount.subtract(sellCount));
                details.setModifyTime(new Date());
                holdDetailsService.update(details);
            }
        }
        // 保存成交单与持仓详情关系表
        saveDealHoldRef(dealNo, payDetailsList);
    }

    /**
     * 保存成交单与持仓详情关系表
     *
     * @param dealNo
     * @param list
     */
    private void saveDealHoldRef(String dealNo, List<HoldDetails> list) {
        List<DealHoldRef> dealHoldRefList = new ArrayList<>();
        for (HoldDetails details : list) {
            DealHoldRef ref = new DealHoldRef();
            ref.setDealNo(dealNo);
            ref.setHoldDetailsId(details.getId());
            ref.setCreateTime(new Date());
            ref.setFlag(Byte.valueOf(Constants.Flag.VALID));
            dealHoldRefList.add(ref);
        }
        dealHoldRefService.save(dealHoldRefList);
    }

    /**
     * 保存成交单
     *
     * @param buyCustomerNo
     * @param sellCustomerNo
     * @param productTradeNo
     * @param type
     * @param count
     * @param holdType
     */
    private String saveDealOrder(String buyCustomerNo, String sellCustomerNo, String productTradeNo, String type,
                                 Integer count, String holdType, BigDecimal price) {
        String dealNo = IdWorker.getId() + "";
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(dealNo);
        dealOrder.setEntrustId(0L);
        dealOrder.setProductTradeNo(productTradeNo);
        dealOrder.setBuyCustomerNo(buyCustomerNo);
        dealOrder.setSellCustomerNo(sellCustomerNo);
        dealOrder.setBuyFee(BigDecimal.ZERO);
        dealOrder.setSellFee(BigDecimal.ZERO);
        dealOrder.setProfit(BigDecimal.ZERO);
        dealOrder.setIntegral(BigDecimal.ZERO);
        dealOrder.setType(type);
        dealOrder.setPrice(price);
        dealOrder.setCount(count);
        dealOrder.setHoldType(holdType);
        dealOrderService.save(dealOrder);
        return dealNo;
    }

    /**
     * 每天释放2个持仓
     *
     * @param totalCount
     */
    private void releaseHoldDetailsByDay(String customerNo, BigDecimal totalCount) {
        List<BigDecimal> list = new LinkedList<>();
        BigDecimal result = totalCount.divide(releaseCount, 0, BigDecimal.ROUND_HALF_UP);
        BigDecimal tempTotal = BigDecimal.ZERO;
        for (int i = 0; i < result.intValue(); i++) {
            if (i != (result.intValue() - 1)) {
                list.add(releaseCount);
            } else {
                list.add(totalCount.subtract(tempTotal));
            }
            tempTotal = tempTotal.add(releaseCount);
        }
        BigDecimal count;
        for (int i = 0; i < list.size(); i++) {
            count = list.get(i);
            addHoldDetails(customerNo, count, tradeDayService.getAddNTradeDay(i + 1), Byte.valueOf("0"));
        }
    }

    /**
     * 重置客户持仓明细
     *
     * @param customerNo
     * @param holdType
     */
    private void rest0001HoldDetails(String customerNo, String holdType) {
        List<HoldDetails> holdDetailsList = holdDetailsService.findByCustomerAndProductNo(customerNo, oldProductTradeNo, holdType);
        for (HoldDetails holdDetails : holdDetailsList) {
            holdDetails.setModifyTime(new Date());
            holdDetails.setRemaindCount(BigDecimal.ZERO);
            holdDetails.setScanner(Byte.valueOf("1"));
            holdDetailsService.update(holdDetails);
        }
    }

    /**
     * 重置0001商品持仓汇总数据
     *
     * @param holdTotal
     */
    private void rest0001HoldTotal(HoldTotal holdTotal) {
        holdTotal.setCanSellCount(BigDecimal.ZERO);
        holdTotal.setTotalCount(BigDecimal.ZERO);
        holdTotal.setModifyTime(new Date());
        holdTotalService.update(holdTotal);
    }

    /**
     * 新增持仓汇总信息
     *
     * @param customerNo
     * @param totalCount
     */
    private void addHoldTotal(String customerNo, BigDecimal totalCount) {
        log.info("客户{}新增持仓汇总信息，总数为{}", customerNo, totalCount);
        HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(customerNo, newProductTradeNo, Constants.HoldType.MAIN);
        if (holdTotal != null) {
            log.info("客户{}已存在新商品持仓汇总，请检查！", customerNo);
            throw new RuntimeException("客户已存在商品持仓汇总");
        }
        HoldTotal total = new HoldTotal();
        total.setCustomerNo(customerNo);
        total.setTotalCount(totalCount);
        total.setFrozenCount(BigDecimal.ZERO);
        total.setCanSellCount(BigDecimal.ZERO);
        total.setCost(BigDecimal.ZERO);
        total.setHoldType(Constants.HoldType.MAIN);
        total.setProductTradeNo(newProductTradeNo);
        holdTotalService.save(total);
    }


    /**
     * 新增商品持仓详情
     *
     * @param customerNo
     * @param count
     * @param tradeTime
     * @param scanner
     */
    private void addHoldDetails(String customerNo, BigDecimal count, Date tradeTime, Byte scanner) {
        HoldDetails details = new HoldDetails();
        details.setCustomerNo(customerNo);
        details.setProductTradeNo(newProductTradeNo);
        details.setOriginalCount(count);
        details.setRemaindCount(count);
        details.setCost(new BigDecimal("1160"));
        details.setTradeTime(tradeTime);
        details.setHoldNo(UUID.randomUUID().toString().replace("-", ""));
        details.setHoldTime(new Date());
        details.setResource(Constants.HoldResource.TRANSFER);
        details.setScanner(scanner);
        details.setHoldType(Constants.HoldType.MAIN);
        holdDetailsService.save(details);
    }

    /**
     * 除法，向下取整数
     *
     * @param v1
     * @param v2
     * @return
     */
    private BigDecimal getInteger(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 0, BigDecimal.ROUND_FLOOR);
    }


    /**
     * 券处理
     *
     * @param customerNo 客户编号
     * @param count      持仓数量
     */
    private BigDecimal coupon(String customerNo, BigDecimal count) {
        // 券余额=持仓数量*20
        BigDecimal balance = count.multiply(new BigDecimal("20"));
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCustomerNo(customerNo);
        changeCouponAccountDto.setChangeAmount(balance);
        changeCouponAccountDto.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        changeCouponAccountDto.setTradeType(Byte.valueOf("104"));
        changeCouponAccountDto.setReType(Byte.valueOf("2"));
        changeCouponAccountDto.setProductTradeNo("0002");
        changeCouponAccountDto.setCouponType("vouchers");
        ApiResult apiResult = couponAccountService.changeAmount(changeCouponAccountDto);
        if (!apiResult.hasSuccess()) {
            log.info("券处理异常，customerNo={}，changeCouponAccountDto={}，apiResult={}", customerNo, JSON.toJSONString(changeCouponAccountDto), apiResult.toString());
            throw new RuntimeException("券处理异常");
        }
        return balance;
    }
}