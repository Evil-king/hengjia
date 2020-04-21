package com.baibei.hengjia.admin.modules.customer.service.impl;

import com.baibei.hengjia.admin.modules.customer.dao.ProductMarketMapper;
import com.baibei.hengjia.admin.modules.customer.model.ProductMarket;
import com.baibei.hengjia.admin.modules.customer.service.IProductMarketService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: hyc
* @date: 2019/09/05 16:57:25
* @description: ProductMarket服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductMarketServiceImpl extends AbstractService<ProductMarket> implements IProductMarketService {

    @Autowired
    private ProductMarketMapper tblProProductMarketMapper;

    @Override
    public ProductMarket findByProductTradeNo(String productTradeNo) {
        Condition condition=new Condition(ProductMarket.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        List<ProductMarket> productMarkets=tblProProductMarketMapper.selectByCondition(condition);
        if(productMarkets.size()>0){
            return productMarkets.get(0);
        }else {
            return null;
        }
    }
}
