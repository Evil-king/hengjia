package com.baibei.hengjia.api.modules.product.dao;

import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.trade.bean.dto.IndexProductListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.IndexProductVo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface ProductMarketMapper extends MyMapper<ProductMarket> {
    List<IndexProductVo> selectListForPage(IndexProductListDto productListDto);
}