package com.baibei.hengjia.api.modules.product.service;

import com.baibei.hengjia.api.modules.product.model.Product;
import com.baibei.hengjia.api.modules.shop.bean.dto.ExchangePointDTO;
import com.baibei.hengjia.api.modules.shop.bean.vo.ShopHomeVO;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 16:16:39
 * @description: Product服务接口
 */
public interface IProductService extends Service<Product> {

    /**
     * 积分商城首页
     *
     * @param pageParam
     * @param customerBaseDto
     * @return
     */
    MyPageInfo<ShopHomeVO> getHomeData(PageParam pageParam, CustomerBaseDto customerBaseDto);


    /**
     * 商品扣减库存
     *
     * @param spuNo
     * @param valueOf
     * @param type
     */
    void cutStock(String spuNo, Integer valueOf, String type);
}
