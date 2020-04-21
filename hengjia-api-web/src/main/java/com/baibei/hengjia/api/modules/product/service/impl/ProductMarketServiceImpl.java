package com.baibei.hengjia.api.modules.product.service.impl;

import com.baibei.hengjia.api.modules.product.bean.dto.ProductMarketDto;
import com.baibei.hengjia.api.modules.product.bean.vo.ProductMarketVo;
import com.baibei.hengjia.api.modules.product.dao.ProductMarketMapper;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.IndexProductListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.IndexProductVo;
import com.baibei.hengjia.api.modules.trade.service.IEntrustOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 16:16:39
 * @description: ProductMarket服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductMarketServiceImpl extends AbstractService<ProductMarket> implements IProductMarketService {
    @Autowired
    private ProductMarketMapper tblProProductMarketMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IEntrustOrderService entrustOrderService;

    @Override
    public ProductMarket findByProductTradeNo(String productTradeNo) {
        Condition condition = new Condition(ProductMarket.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        List<ProductMarket> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public MyPageInfo<IndexProductVo> productList(IndexProductListDto productListDto) {
        /*Condition condition = new Condition(ProductMarket.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(productListDto.getTradeStatusList())) {
            criteria.andIn("tradeStatus",productListDto.getTradeStatusList());
        }
        MyPageInfo<ProductMarket> myPageInfo = pageList(condition, PageParam.buildWithDefaultSort(productListDto.getCurrentPage(), productListDto.getPageSize()));

        return PageUtil.transform(myPageInfo, IndexProductVo.class);*/
        PageHelper.startPage(productListDto.getCurrentPage(), productListDto.getPageSize());
        List<IndexProductVo> list = tblProProductMarketMapper.selectListForPage(productListDto);
        return new MyPageInfo<>(list);
    }

    @Override
    public List<String> getAllProductTradeNo(List<String> statusList) {
        Condition condition = new Condition(ProductMarket.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(statusList)) {
            criteria.andIn("tradeStatus",statusList);
        }
        List<ProductMarket> list=tblProProductMarketMapper.selectByCondition(condition);
        List<String> result=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            result.add(list.get(i).getProductTradeNo());
        }
        return result;
    }

    /**
     * 此方法未设置可卖/可卖数量
     * @param productMarketDto
     * @return
     */
    @Override
    public ProductMarketVo findByProductTradeNo(ProductMarketDto productMarketDto) {
        ProductMarketVo productMarketVo=new ProductMarketVo();
        //查找到商品信息
        ProductMarket productMarket=findByProductTradeNo(productMarketDto.getProductTradeNo());
        if(productMarket==null){
            return null;
        }
        //商品名
        productMarketVo.setProductTradeName(productMarket.getProductTradeName());
        //发行价
        productMarketVo.setIssuePrice(productMarket.getIssuePrice());
        //卖1价格
        productMarketVo.setSellPrice(productMarket.getIssuePrice());
        //买1价格
        productMarketVo.setBuyPrice(productMarket.getIssuePrice());
        //卖1数量
        //String sellKey=MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_SELL, productMarketDto.getProductTradeNo());
        //Integer sellAmount= (Integer) redisUtil.hmget(sellKey, productMarket.getIssuePrice()+"");
        BigDecimal sellAmount = entrustOrderService.entrustCount(productMarketDto.getProductTradeNo(),"sell");
        productMarketVo.setSellAmount(sellAmount==null? 0: sellAmount.intValue());
        //买1数量
        //String buyKey=MessageFormat.format(RedisConstant.TRADE_ENTRUST_PRICEANDCOUNT_BUY,productMarketDto.getProductTradeNo());
        //Integer buyAmount= (Integer) redisUtil.hmget(buyKey,productMarket.getIssuePrice()+"");
        BigDecimal buyAmount = entrustOrderService.entrustCount(productMarketDto.getProductTradeNo(),"buy");
        productMarketVo.setBuyAmount(buyAmount==null? 0: buyAmount.intValue() );
        if("SELL".equals(productMarketDto.getDirection())){
            //如果卖出默认数量则为0
            productMarketVo.setDefaultAmount(0);
        }else {
            //如果买入，则默认数量为配票的数量
            productMarketVo.setDefaultAmount(productMarket.getDeliveryNum());
        }
        return productMarketVo;
    }

    @Override
    public List<ProductMarket> findByTicketRule(String ticketRule) {
        Condition condition=new Condition(ProductMarket.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("ticketRule",ticketRule);
        return tblProProductMarketMapper.selectByCondition(condition);
    }
}
