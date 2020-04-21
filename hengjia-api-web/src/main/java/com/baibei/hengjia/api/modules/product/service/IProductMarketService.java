package com.baibei.hengjia.api.modules.product.service;
import com.baibei.hengjia.api.modules.product.bean.dto.ProductMarketDto;
import com.baibei.hengjia.api.modules.product.bean.vo.ProductMarketVo;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.trade.bean.dto.IndexProductListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.IndexProductVo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 16:16:39
* @description: ProductMarket服务接口
*/
public interface IProductMarketService extends Service<ProductMarket> {

    ProductMarket findByProductTradeNo(String productTradeNo);


    MyPageInfo<IndexProductVo> productList(IndexProductListDto productListDto);

    List<String> getAllProductTradeNo(List<String> statusList);

    ProductMarketVo findByProductTradeNo(ProductMarketDto productMarketDto);

    List<ProductMarket> findByTicketRule(String ticketRule);
}
