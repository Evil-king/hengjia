package com.baibei.hengjia.api.modules.product.service.impl;

import com.baibei.hengjia.api.modules.product.dao.ShopProductMapper;
import com.baibei.hengjia.api.modules.product.model.ShopProduct;
import com.baibei.hengjia.api.modules.product.service.IShopProductService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 16:16:39
* @description: ShopProduct服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ShopProductServiceImpl extends AbstractService<ShopProduct> implements IShopProductService {

    @Autowired
    private ShopProductMapper tblProShopProductMapper;

}
