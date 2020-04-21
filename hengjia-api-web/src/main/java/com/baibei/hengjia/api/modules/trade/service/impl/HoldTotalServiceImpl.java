package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.MyHoldDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MyHoldNewDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryHoldVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldNewVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.StatisticsVo;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: HoldTotal服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldTotalServiceImpl extends AbstractService<HoldTotal> implements IHoldTotalService {

    @Autowired
    private HoldTotalMapper tblTraHoldTotalMapper;
    @Autowired
    private IProductMarketService productMarketService;
    @Value("${match.delivery.flag}")
    private String matchDeliveryFlag;

    @Value("${main.delivery.flag}")
    private String mainDeliveryFlag;

    @Value("${newproduct.list}")
    private String newProductList;

    @Override
    public HoldTotal findByCustomerAndProductNo(String customerNo, String productTradeNo, String holdType) {
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        criteria.andEqualTo("holdType", holdType);
        List<HoldTotal> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public boolean frozenCustomerProductHold(String customerNo, String productTradeNo, int frozenCount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        if (holdTotal.getCanSellCount().compareTo(new BigDecimal(frozenCount)) < 0) {
            throw new ServiceException("商品可卖数量不足");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("frozenCount", holdTotal.getFrozenCount());
        criteria.andEqualTo("canSellCount", holdTotal.getCanSellCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setFrozenCount(holdTotal.getFrozenCount().add(new BigDecimal(frozenCount)));
        newHoldTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(new BigDecimal(frozenCount)));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public boolean unfrozenCustomerProductHold(String customerNo, String productTradeNo, int frozenCount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        if (holdTotal.getTotalCount().compareTo(new BigDecimal(frozenCount)) < 0) {
            throw new ServiceException("商品总数数量不足");
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
        newHoldTotal.setFrozenCount(holdTotal.getFrozenCount().subtract(new BigDecimal(frozenCount)));
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().subtract(new BigDecimal(frozenCount)));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public boolean deductCustomerProductHold(String customerNo, String productTradeNo, int changeAmount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        if (holdTotal.getCanSellCount().compareTo(new BigDecimal(changeAmount)) < 0) {
            throw new ServiceException("商品可卖数量不足");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("canSellCount", holdTotal.getCanSellCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().subtract(new BigDecimal(changeAmount)));
        newHoldTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(new BigDecimal(changeAmount)));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public boolean addCustomerProductHold(String customerNo, String productTradeNo, int changeAmount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().add(new BigDecimal(changeAmount)));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public boolean addCustomerProductHoldForMatch(String customerNo, String productTradeNo, BigDecimal changeAmount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(customerNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().add(changeAmount));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public StatisticsVo marketValue(String customerNo) {
        StatisticsVo statisticsVo = new StatisticsVo();
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<HoldTotal> list = findByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            statisticsVo.setMarketValue(BigDecimal.ZERO);
        } else {
            BigDecimal total = BigDecimal.ZERO;
            ProductMarket productMarket;
            for (HoldTotal item : list) {
                productMarket = productMarketService.findByProductTradeNo(item.getProductTradeNo());
                total = total.add(productMarket == null ? BigDecimal.ZERO : item.getTotalCount().multiply(productMarket.getIssuePrice()));
            }
            statisticsVo.setMarketValue(total.setScale(2));
        }
        statisticsVo.setProfitAndLoss(BigDecimal.ZERO);
        return statisticsVo;
    }

    @Override
    public MyPageInfo<MyHoldVo> myHoldList(MyHoldDto myHoldDto) {
        PageHelper.startPage(myHoldDto.getCurrentPage(), myHoldDto.getPageSize());
        List<MyHoldVo> list = tblTraHoldTotalMapper.myHoldList(myHoldDto);
        List<String> productNoList = Arrays.asList(newProductList.split(","));
        for (MyHoldVo vo : list) {
            if (productNoList.contains(vo.getProductTradeNo())) {
                if (Constants.HoldType.MAIN.equals(vo.getHoldType())) {
                    vo.setDeliveryFlag(Boolean.valueOf(mainDeliveryFlag));
                } else {
                    vo.setDeliveryFlag(Boolean.valueOf(matchDeliveryFlag));
                }
            } else {
                vo.setDeliveryFlag(true);
            }
        }
        return new MyPageInfo<>(list);
    }

    @Override
    public MyPageInfo<MyHoldNewVo> myHoldList(MyHoldNewDto myHoldDto) {
        PageHelper.startPage(myHoldDto.getCurrentPage(), myHoldDto.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("customerNo", myHoldDto.getCustomerNo());
        params.put("type", myHoldDto.getType());
        List<MyHoldNewVo> list = tblTraHoldTotalMapper.myHoldListForNew(params);
        if (CollectionUtils.isEmpty(list)) {
            return new MyPageInfo<>(new ArrayList<>());
        }
        for (MyHoldNewVo vo : list) {
            vo.setTotalCount(vo.getOriginalCount().intValue());
            vo.setCanSellCount(vo.getScanner().intValue() == 0 ? 0 : vo.getRemindCount());
            vo.setHoldType(myHoldDto.getType());
            vo.setDeliveryFlag(true);
        }
        return new MyPageInfo<>(list);
    }

    @Override
    public List<HoldTotal> matchHoldList(String customerNo) {
        List<HoldTotal> holdTotals = tblTraHoldTotalMapper.selectMatchHoldList(customerNo);
        return holdTotals;
    }

    @Override
    public void updateTotalCountById(Long id, String deliveryMatchCount) {
        Map map = new HashMap<>();
        map.put("id", id);
        map.put("deliveryMatchCount", deliveryMatchCount);
        tblTraHoldTotalMapper.updateTotalCount(map);
    }

    @Override
    public MyDeliveryHoldVo findMyDeliveryHold(String customerNo, String productTradeNo, String holdType) {
        MyDeliveryHoldVo myDeliveryHoldVo = tblTraHoldTotalMapper.selectMyDeliveryHold(customerNo, productTradeNo, holdType);
        if (StringUtils.isEmpty(myDeliveryHoldVo)) {
            throw new ServiceException("查询不到指定持仓");
        }
        if (Constants.HoldResource.EXCHANGE.equals(myDeliveryHoldVo.getResource())) {
            myDeliveryHoldVo.setHoldType(Constants.HoldResource.EXCHANGE);
        }
        myDeliveryHoldVo.setCanDeliveryCount(new BigDecimal(myDeliveryHoldVo.getTotalCount().subtract(myDeliveryHoldVo.getFrozenCount()).intValue()));//向下取整
        myDeliveryHoldVo.setTotalCount(new BigDecimal(myDeliveryHoldVo.getTotalCount().intValue()));//向下取整
        return myDeliveryHoldVo;
    }

    @Override
    public boolean deductMemberProductHold(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType) {
        HoldTotal holdTotal = findByCustomerAndProductNo(memberNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        if (holdTotal.getCanSellCount().compareTo(changeAmount) < 0) {
            throw new ServiceException("商品可卖数量不足");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", memberNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("canSellCount", holdTotal.getCanSellCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().subtract(changeAmount));
        newHoldTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(changeAmount));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public boolean deductProductHoldByTradeTime(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType, BigDecimal detuchCanSellCount) {
        HoldTotal holdTotal = findByCustomerAndProductNo(memberNo, productTradeNo, holdType);
        if (holdTotal == null) {
            throw new ServiceException("不存在客户商品持仓信息");
        }
        if (holdTotal.getCanSellCount().compareTo(changeAmount) < 0) {
            throw new ServiceException("商品可卖数量不足");
        }
        // 乐观锁实现方式
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", memberNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("totalCount", holdTotal.getTotalCount());
        criteria.andEqualTo("canSellCount", holdTotal.getCanSellCount());
        criteria.andEqualTo("holdType", holdType);

        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setTotalCount(holdTotal.getTotalCount().subtract(changeAmount));
        newHoldTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(detuchCanSellCount));
        newHoldTotal.setModifyTime(new Date());
        return updateByConditionSelective(newHoldTotal, condition);
    }

    @Override
    public List<HoldTotal> findCanSellHoldTotal(String productTradeNo) {
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andGreaterThan("totalCount", BigDecimal.ZERO);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        return findByCondition(condition);
    }
}
