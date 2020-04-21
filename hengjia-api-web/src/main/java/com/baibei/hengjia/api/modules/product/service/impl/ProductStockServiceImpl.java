package com.baibei.hengjia.api.modules.product.service.impl;

import com.baibei.hengjia.api.modules.product.dao.ProductStockMapper;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.model.ProductStock;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.product.service.IProductStockService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 16:16:39
 * @description: ProductStock服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductStockServiceImpl extends AbstractService<ProductStock> implements IProductStockService {

    @Autowired
    private ProductStockMapper tblProProductStockMapper;
    @Autowired
    private IProductMarketService productMarketService;

    @Override
    public ProductStock findBySpuNo(String spuNo) {
        Condition condition = new Condition(ProductStock.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("spuNo", spuNo);
        return findOneByCondition(condition);
    }

    @Override
    public ProductStock findByProductTradeNo(String productTradeNo) {
        ProductMarket productMarket = productMarketService.findByProductTradeNo(productTradeNo);
        if (productMarket == null) {
            return null;
        }
        return findBySpuNo(productMarket.getSpuNo());
    }
}
