package com.baibei.hengjia.api.modules.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.trade.bean.dto.EntrustAllListDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.RevokeDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyEntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.dao.EntrustOrderMapper;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.utils.TradeUtil;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.FreezingAmountTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.MapUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: EntrustOrder服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EntrustOrderServiceImpl extends AbstractService<EntrustOrder> implements IEntrustOrderService {

    @Autowired
    private EntrustOrderMapper tblTraEntrustOrderMapper;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IHoldTotalService holdTotalService;

    @Autowired
    private IRevokeOrderService revokeOrderService;

    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IEntrustDetailsRefService entrustDetailsRefService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private ITradeWhiteListService tradeWhiteListService;

    /**
     * 买入手续费
     */
    @Value("${buy.fee.rate}")
    private String buyFeeRate;

    @Value("${lister.replace.customerNos}")
    private String replaceCustomerNos;

    @Override
    public EntrustOrder initEntrustOrder(String customerNo, String productTradeNo, String direction, String price,
                                         String entrustNo, int count, BigDecimal fee, String holdType, Byte canShow) {
        EntrustOrder order = new EntrustOrder();
        Date date = new Date();
        order.setEntrustNo(entrustNo);
        order.setCustomerNo(customerNo);
        order.setProductTradeNo(productTradeNo);
        order.setEntrustTime(date);
        order.setDirection(direction);
        order.setPrice(new BigDecimal(price));
        order.setEntrustCount(count);
        order.setDealCount(0);
        order.setRevokeCount(0);
        order.setResult(Constants.EntrustOrderResult.WAIT_DEAL);
        order.setStatus(Constants.EntrustOrderStatus.OK);
        order.setFlag(Byte.valueOf(Constants.Flag.VALID));
        order.setCreateTime(date);
        order.setModifyTime(date);
        order.setFee(fee);
        order.setHoldType(holdType);
        order.setCanShow(canShow);
        // 转让用户卖出打标识
        boolean isTransferWhiteList = tradeWhiteListService.isWhiteList(customerNo, Constants.TradeWhitelistType.TRANSFER_SELL);
        if (isTransferWhiteList) {
            order.setTransferType("transfer");
        }
        this.save(order);
        return order;
    }

    @Override
    public void updateEntrustOrder(String entrustNo, EntrustOrder entrustOrder) {
        Condition condition = new Condition(EntrustOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("entrustNo", entrustNo);
        this.updateByConditionSelective(entrustOrder, condition);
    }

    @Override
    public EntrustOrder findByOrderNo(String entrustNo) {
        return findBy("entrustNo", entrustNo);
    }

    @Override
    public MyPageInfo<EntrustOrderVo> allList(EntrustAllListDto entrustAllListDto) {
        // 转让用户在即时卖出功能，查询委托买入的委托单列表时，返回空列表
        if (Constants.TradeDirection.BUY.equals(entrustAllListDto.getDirection())) {
            boolean isTransferWhiteList = tradeWhiteListService.isWhiteList(entrustAllListDto.getCustomerNo(), Constants.TradeWhitelistType.TRANSFER_SELL);
            if (isTransferWhiteList) {
                return new MyPageInfo<>(new ArrayList<>());
            }
        }
        entrustAllListDto.setTransferType("normal");
        entrustAllListDto.setCustomerNo(null);
        PageHelper.startPage(entrustAllListDto.getCurrentPage(), entrustAllListDto.getPageSize());
        List<EntrustOrderVo> list = tblTraEntrustOrderMapper.allList(entrustAllListDto);
        MyPageInfo<EntrustOrderVo> myPageInfo = new MyPageInfo<>(list);
        Map<String, String> map = new HashMap<>();
        String[] customerNoArray = replaceCustomerNos.split(",");
        int index = 0;
        for (String s : customerNoArray) {
            map.put(String.valueOf(index), s);
            index++;
        }
        for (EntrustOrderVo vo : list) {
            vo.setMinTrade(1);
            vo.setEntrustCount(vo.getEntrustCount() - vo.getDealCount());
            if ("2578".equals(vo.getCustomerNo())) {
                char endStr = vo.getEntrustId().toString().charAt(vo.getEntrustId().toString().length() - 1);
                String replaceCustomerNo = map.get(String.valueOf(endStr));
                vo.setCustomerNo(replaceCustomerNo);
                vo.setCustomerName(replaceCustomerNo);
            }
        }
        return myPageInfo;
    }

    @Override
    public MyPageInfo<EntrustOrderVo> allListForTransfer(EntrustAllListDto entrustAllListDto) {
        entrustAllListDto.setTransferType("transfer");
        // 2578能看到全部特殊客户的，特殊客户自已只能看到自已的
        entrustAllListDto.setCustomerNo(entrustAllListDto.getCustomerNo().equals("2578") ? null : entrustAllListDto.getCustomerNo());
        PageHelper.startPage(entrustAllListDto.getCurrentPage(), entrustAllListDto.getPageSize());
        List<EntrustOrderVo> list = tblTraEntrustOrderMapper.allList(entrustAllListDto);
        MyPageInfo<EntrustOrderVo> myPageInfo = new MyPageInfo<>(list);
        for (EntrustOrderVo vo : list) {
            vo.setMinTrade(1);
            vo.setEntrustCount(vo.getEntrustCount() - vo.getDealCount());
        }
        return myPageInfo;
    }

    /**
     * 姓名脱敏处理
     *
     * @param fullName
     * @return
     */
    public static String chineseName(final String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        final String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    @Override
    public MyPageInfo<MyEntrustOrderVo> myList(CustomerBaseAndPageDto customerBaseAndPageDto) {
        PageHelper.startPage(customerBaseAndPageDto.getCurrentPage(), customerBaseAndPageDto.getPageSize());
        List<MyEntrustOrderVo> list = tblTraEntrustOrderMapper.myList(customerBaseAndPageDto);
        MyPageInfo<MyEntrustOrderVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public void revoke(RevokeDto revokeDto, String checkTradeDay) {
        log.info("========开始撤单逻辑=========");
        if (checkTradeDay.equals(Constants.CheckTradeDay.CHECK)) {
            boolean tradeDay = tradeDayService.isTradeTime();
            if (!tradeDay)
                throw new ServiceException("休市时间不能撤单");
        }
        //查询委托单信息
        EntrustOrder entrustOrder = findByOrderNo(revokeDto.getEntrustNo());
        if (entrustOrder.getResult().equals(Constants.EntrustOrderResult.REVOKE))
            throw new ServiceException("此委托单已被撤单");
        if (!entrustOrder.getStatus().equals(Constants.EntrustOrderStatus.OK))
            throw new ServiceException("当前委托单不能够撤单");
        String direction = entrustOrder.getDirection();
        //撤销数量=委托数量-成交数量
        Integer revokeCount = entrustOrder.getEntrustCount().intValue() - entrustOrder.getDealCount().intValue();
        //撤销价格=撤销数量*委托单价格*(1+手续费率)
        BigDecimal rate = new BigDecimal("1").add(new BigDecimal(buyFeeRate));
        BigDecimal revokePrice = entrustOrder.getPrice().multiply(new BigDecimal(revokeCount)).multiply(rate);
        revokePrice = revokePrice.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_UP);//保留两位小数，向上取整
        log.info("========撤单，开始更新委托单=========");
        //更新委托单
        entrustOrder.setRevokeCount(revokeCount);//撤销数量
        entrustOrder.setResult(Constants.EntrustOrderResult.REVOKE);//撤单状态
        entrustOrder.setModifyTime(new Date());
        boolean revokeFlag = updateEntrustOrderForRevoke(entrustOrder, revokeDto);
        log.info("========撤单，结束更新委托单=========");
        if (!revokeFlag)
            throw new ServiceException("撤单失败，乐观锁错误，请联系管理员");
        if (Constants.TradeDirection.BUY.equals(direction) && revokeFlag) {//挂牌买入(更新成功，影响行数>0)
            log.info("========撤单，开始解冻客户资金=========");
            //解冻资金
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(revokeDto.getCustomerNo());
            changeAmountDto.setOrderNo(revokeDto.getEntrustNo());
            changeAmountDto.setChangeAmount(revokePrice);
            changeAmountDto.setTradeType(Byte.valueOf(FreezingAmountTradeTypeEnum.REVOKE_BUY_FREEZE.getCode() + ""));
            changeAmountDto.setReType(Byte.valueOf("1"));
            accountService.changeFreezenAmountByType(changeAmountDto);
            log.info("========撤单，结束解冻客户资金=========");
        }
        if (Constants.TradeDirection.SELL.equals(direction) && revokeFlag) {//挂牌卖出(更新成功，影响行数>0)
            log.info("========撤单，开始解冻客户持仓=========");
            //解冻客户持仓
            boolean unFrozenFlag = this.unfrozenCustomerProductHoldForRevoke(revokeDto.getCustomerNo(), entrustOrder.getProductTradeNo(),
                    revokeCount, entrustOrder.getHoldType());
            if (!unFrozenFlag)
                throw new ServiceException("撤单失败，乐观锁错误，请联系管理员");
            // 解冻客户持仓明细 && 删除委托单与持仓明细的关联表
            List<EntrustDetailsRef> refList = entrustDetailsRefService.findByEntrustNo(revokeDto.getEntrustNo());
            for (EntrustDetailsRef ref : refList) {
                HoldDetails holdDetails = holdDetailsService.findById(ref.getHoldDetailsId());
                if (holdDetails == null) {
                    log.info("持仓明细不存在，detailsId={}", ref.getHoldDetailsId());
                    throw new ServiceException("持仓明细不存在");
                }
                boolean flag = holdDetailsService.unfrozen(holdDetails, ref.getDeductCount());
                if (!flag) {
                    throw new ServiceException("撤单失败");
                }
                ref.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
                entrustDetailsRefService.update(ref);
                // 委托时间是当天的才删除标识
                if (DateUtil.isToday(entrustOrder.getEntrustTime())) {
                    String sellHoldType = TradeUtil.getSellType(holdDetails.getHoldType(), holdDetails.getResource());
                    String key = MessageFormat.format(RedisConstant.TRADE_SELL_LIMIT, holdDetails.getCustomerNo(), sellHoldType);
                    redisUtil.delete(key);
                }
            }
            log.info("========撤单，结束解冻客户持仓=========");
        }
        log.info("========开始插入撤单记录=========");
        //插入撤单记录
        RevokeOrder revokeOrder = new RevokeOrder();
        revokeOrder.setEntrustId(entrustOrder.getId());
        revokeOrder.setCount(revokeCount);
        revokeOrder.setCreateTime(new Date());
        revokeOrder.setModifyTime(new Date());
        revokeOrder.setFlag(Byte.valueOf(Constants.Flag.VALID));
        revokeOrderService.addRevokeOrder(revokeOrder);
        log.info("========结束插入撤单记录=========");
        // 更新挂单Redis
        updateRedis(entrustOrder);

        log.info("========结束撤单逻辑=========");
    }

    @Override
    public void batchRevoke(List<RevokeDto> revokeDtoList) {
        for (RevokeDto revokeDto : revokeDtoList) {
            this.revoke(revokeDto, Constants.CheckTradeDay.UNCHECK);
        }
    }

    /**
     * 修改委托单Redis数据
     *
     * @param entrustOrder
     */
    private void updateRedis(EntrustOrder entrustOrder) {
        String productTradeNo = entrustOrder.getProductTradeNo();
        String price = entrustOrder.getPrice().toPlainString();
        if (Constants.TradeDirection.BUY.equals(entrustOrder.getDirection())) {
            // 减少商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_BUY, productTradeNo);
            redisUtil.hincrBy(countKey, price, -entrustOrder.getRevokeCount());
            // 移除挂牌买入委托单
            String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_BUY, productTradeNo, price);
            redisUtil.zremove(timeKey, entrustOrder.getEntrustNo());
        } else {
            // 减少商品挂牌买入价格对应的挂牌数量
            String countKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_SELL, productTradeNo);
            redisUtil.hincrBy(countKey, price, -entrustOrder.getRevokeCount());
            // 移除挂牌买入委托单
            String timeKey = MessageFormat.format(RedisConstant.TRADE_ENTRUST_TIME_SELL, productTradeNo, price);
            redisUtil.zremove(timeKey, entrustOrder.getEntrustNo());
        }
        // 删除委托单信息
        String entrustInfoKey = MessageFormat.format(RedisConstant.TRADE_ENTRUSTINFO, entrustOrder.getEntrustNo());
        redisUtil.hdelete(entrustInfoKey, MapUtil.objectToMap(entrustOrder).keySet());
    }

    /**
     * 更新委托单
     *
     * @param entrustOrder
     * @param revokeDto
     * @return
     */
    public boolean updateEntrustOrderForRevoke(EntrustOrder entrustOrder, RevokeDto revokeDto) {
        //乐观锁实现
        Condition condition = new Condition(EntrustOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("entrustNo", revokeDto.getEntrustNo());
        criteria.andEqualTo("dealCount", entrustOrder.getDealCount());//乐观锁实现
        criteria.andNotEqualTo("result", Constants.EntrustOrderResult.REVOKE);
        boolean updateFlag = this.updateByConditionSelective(entrustOrder, condition);
        return updateFlag;
    }

    /**
     * 解冻客户持仓
     *
     * @param customerNo
     * @param productTradeNo
     * @param frozenCount
     * @param holdType
     * @return
     */
    public boolean unfrozenCustomerProductHoldForRevoke(String customerNo, String productTradeNo, int revokeCount, String holdType) {
        HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("frozenCount", holdTotal.getFrozenCount());
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setFrozenCount(holdTotal.getFrozenCount().subtract(new BigDecimal(revokeCount)));//解冻
        newHoldTotal.setCanSellCount(holdTotal.getCanSellCount().add(new BigDecimal(revokeCount)));//增加可卖数量
        newHoldTotal.setModifyTime(new Date());
        return holdTotalService.updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public BigDecimal entrustCount(String productTradeNo, String direction) {
        Map<String, Object> param = new HashMap<>();
        param.put("direction", direction);
        param.put("productTradeNo", productTradeNo);
        return tblTraEntrustOrderMapper.entrustCount(param);
    }

    @Override
    public void revokeAll(String transferType) {
        log.info("开始撤销当前全部委托单");
        if ("normal".equals(transferType) && "transfer".equals(transferType) && "all".equals(transferType)) {
            throw new ServiceException("参数异常");
        }
        Map map = new HashMap();
        map.put("transferType", "all".equals(transferType) ? "" : transferType);
        List<EntrustOrder> entrustOrderList = tblTraEntrustOrderMapper.allRevokeList(map);
        for (EntrustOrder entrustOrder : entrustOrderList) {
            RevokeDto revokeDto = new RevokeDto();
            revokeDto.setEntrustNo(entrustOrder.getEntrustNo());
            revokeDto.setCustomerNo(entrustOrder.getCustomerNo());
            this.revoke(revokeDto, Constants.CheckTradeDay.UNCHECK);
            log.info("委托单撤销成功，{}", entrustOrder.toString());
        }
    }

    @Override
    public List<EntrustOrder> findNotDealEntrustOrder(String customerNo, String productTradeNo, String direction) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerNo", customerNo);
        params.put("productTradeNo", productTradeNo);
        params.put("direction", direction);
        return tblTraEntrustOrderMapper.findNotDealEntrustOrder(params);
    }
}
