package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.product.dao.ProductImgMapper;
import com.baibei.hengjia.api.modules.product.dao.ProductMapper;
import com.baibei.hengjia.api.modules.product.model.Product;
import com.baibei.hengjia.api.modules.product.model.ProductImg;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.bean.dto.DealOrderDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.DealOrderListVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.DealOrderVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.dao.DealOrderMapper;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.DealOrderTypeEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: DealOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DealOrderServiceImpl extends AbstractService<DealOrder> implements IDealOrderService {
    //图片前缀地址
    @Value("${pictureUrl}")
    private String pictureUrl;
    @Autowired
    private DealOrderMapper tblTraDealOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public MyPageInfo<DealOrderVo> findByCustomerNo(DealOrderDto dealOrderDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = "";
        String endDate = "";
        try {
            if (!StringUtil.isEmpty(dealOrderDto.getStartTime())) {
                startDate = simpleDateFormat.format(Long.valueOf(dealOrderDto.getStartTime()));
            }
            if (!StringUtil.isEmpty(dealOrderDto.getEndTime())) {
                endDate = simpleDateFormat.format(Long.valueOf(dealOrderDto.getEndTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dealOrderDto.setStartTime(String.valueOf(startDate));
        dealOrderDto.setEndTime(String.valueOf(endDate));
        PageHelper.startPage(dealOrderDto.getCurrentPage(), dealOrderDto.getPageSize());
        List<DealOrderListVo> list = tblTraDealOrderMapper.findByCustomerNo(dealOrderDto);
        MyPageInfo<DealOrderListVo> page = new MyPageInfo<>(list);


        for (int i = 0; i < list.size(); i++) {
            //先算总价
            BigDecimal totalPrice = list.get(i).getPrice().multiply(new BigDecimal(list.get(i).getCount()));
            list.get(i).setTotalPrice(totalPrice);
            Condition condition = new Condition(Product.class);
            Example.Criteria criteria = buildValidCriteria(condition);
            criteria.andEqualTo("spuNo", list.get(i).getSpuNo());
            //图片地址
            List<Product> productImgList = productMapper.selectByCondition(condition);
            Product Product = productImgList.size() < 1 ? null : productImgList.get(0);
            if (Product == null) {
                list.get(i).setImageUrl(null);
            } else {
                list.get(i).setImageUrl(Product.getImgUrl());
            }

            if (dealOrderDto.getCustomerNo().equals(list.get(i).getBuyCustomerNo())) {
                //为买方
                if (Constants.TradeDirection.SELL.equals(list.get(i).getType())) {
                    //若此时成交单方向为卖出，则为委托方
                    list.get(i).setRetype(list.get(i).getDirection());
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getEntrustHoldType() + list.get(i).getDirection()));
                } else if ((Constants.TradeDirection.TRANSFER.equals(list.get(i).getType()))) {
                    //此时为非交易过户
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getType()));
                    list.get(i).setRetype(Constants.TradeDirection.BUY);
                } else if (list.get(i).getCustomerNo() == null && Constants.TradeDirection.OFFSET.equals(list.get(i).getType())) {
                    //此时为offset方式
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getType() + Constants.TradeDirection.BUY));
                    list.get(i).setRetype(Constants.TradeDirection.BUY);
                } else {
                    //成交方
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getDealHoldType() + list.get(i).getType()));
                    list.get(i).setRetype(Constants.TradeDirection.EXCHANGE.equals(list.get(i).getType()) ? "buy" : list.get(i).getType());
                }
            } else {
                //卖方
                if (list.get(i).getCustomerNo() == null && (Constants.TradeDirection.EXCHANGE.equals(list.get(i).getType()))) {
                    //此时为兑换方式
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(Constants.HoldType.MAIN + "payment"));
                    list.get(i).setRetype(Constants.TradeDirection.SELL);
                } else if (list.get(i).getCustomerNo() == null && (Constants.TradeDirection.TRANSFER.equals(list.get(i).getType()))) {
                    //此时为非交易过户
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getType()));
                    list.get(i).setRetype(Constants.TradeDirection.SELL);
                } else if (Constants.TradeDirection.BUY.equals(list.get(i).getType())) {
                    //若此时成交单方向为买入，则为委托方
                    list.get(i).setRetype(list.get(i).getDirection());
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getEntrustHoldType() + list.get(i).getDirection()));

                } else if (list.get(i).getCustomerNo() == null && Constants.TradeDirection.OFFSET.equals(list.get(i).getType())) {
                    //此时为offset方式
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getType() + Constants.TradeDirection.SELL));
                    list.get(i).setRetype(Constants.TradeDirection.SELL);

                } else {
                    //成交方
                    list.get(i).setDirection(DealOrderTypeEnum.getMsg(list.get(i).getDealHoldType() + list.get(i).getType()));
                    list.get(i).setRetype(list.get(i).getType());
                }
            }
        }
        return PageUtil.transform(page, DealOrderVo.class);
    }

    @Override
    public List<CustomerCountVo> sumMoneyForBuy(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumMoneyForBuy(param);
    }

    @Override
    public List<CustomerCountVo> sumMoneyForSell(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumMoneyForSell(param);
    }

    @Override
    public List<CustomerCountVo> sumFee(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumFee(param);
    }

    @Override
    public List<CustomerCountVo> tradeCount(Map<String, Object> param) {
        return tblTraDealOrderMapper.tradeCount(param);
    }

    @Override
    public List<CustomerCountVo> sumProfitAmountForSell(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumProfitAmountForSell(param);
    }

    @Override
    public List<CustomerCountVo> sumLossAmountForBuy(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumLossAmountForBuy(param);
    }

    @Override
    public BigDecimal sumAllFee(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumAllFee(param);
    }

    @Override
    public BigDecimal sumAllIntegral(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumAllIntegral(param);
    }

    @Override
    public BigDecimal sumAllCouponAmount(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumAllCouponAmount(param);
    }

    @Override
    public BigDecimal sumAllHongmuFund(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumAllHongmuFund(param);
    }

    @Override
    public Integer sumBuyCount(String buyCustomerNo, String productTradeNo, String date) {
        Map<String, Object> param = new HashMap<>();
        param.put("buyCustomerNo", buyCustomerNo);
        param.put("productTradeNo", productTradeNo);
        param.put("date", date);
        return tblTraDealOrderMapper.sumBuyCount(param);
    }

    @Override
    public Integer sumSellCount(String sellCustomerNo, String productTradeNo, String date) {
        Map<String, Object> param = new HashMap<>();
        param.put("sellCustomerNo", sellCustomerNo);
        param.put("productTradeNo", productTradeNo);
        param.put("date", date);
        return tblTraDealOrderMapper.sumSellCount(param);
    }

    @Override
    public List<String> findTradeProductNo(String customerNo, String time) {
        return tblTraDealOrderMapper.findTradeProductNo(customerNo, time);
    }

    @Override
    public List<DealOrder> sumIntegral(String currentDate) {
        return tblTraDealOrderMapper.sumIntegral(currentDate);
    }

    @Override
    public boolean isBuyToday(String customer) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerNo", customer);
        params.put("date", DateUtil.yyyyMMddWithLine.get().format(new Date()));
        Integer count = tblTraDealOrderMapper.buyCountToday(params);
        return (count != null && count.intValue() > 0);
    }

    @Override
    public List<DealOrder> querySellList(String customerNo, Date date) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerNo", customerNo);
        params.put("date", DateUtil.yyyyMMddWithLine.get().format(date));
        return tblTraDealOrderMapper.querySellList(params);
    }

    @Override
    public List<CustomerCountVo> sumHongmuFund(Map<String, Object> param) {
        return tblTraDealOrderMapper.sumHongmuFund(param);
    }
}
