package com.baibei.hengjia.api.modules.product.service;

import com.baibei.hengjia.api.modules.product.model.ProductStock;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 16:16:39
 * @description: ProductStock服务接口
 */
public interface IProductStockService extends Service<ProductStock> {

    ProductStock findBySpuNo(String spuNo);

    ProductStock findByProductTradeNo(String productTradeNo);

}
