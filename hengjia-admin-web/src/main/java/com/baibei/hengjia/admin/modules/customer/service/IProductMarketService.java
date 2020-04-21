package com.baibei.hengjia.admin.modules.customer.service;
import com.baibei.hengjia.admin.modules.customer.model.ProductMarket;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/09/05 16:57:25
* @description: ProductMarket服务接口
*/
public interface IProductMarketService extends Service<ProductMarket> {

    ProductMarket findByProductTradeNo(String productTradeNo);
}
