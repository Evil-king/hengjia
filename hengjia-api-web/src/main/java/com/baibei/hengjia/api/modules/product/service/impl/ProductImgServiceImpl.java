package com.baibei.hengjia.api.modules.product.service.impl;

import com.baibei.hengjia.api.modules.product.dao.ProductImgMapper;
import com.baibei.hengjia.api.modules.product.model.ProductImg;
import com.baibei.hengjia.api.modules.product.service.IProductImgService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 16:16:39
* @description: ProductImg服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductImgServiceImpl extends AbstractService<ProductImg> implements IProductImgService {

    @Autowired
    private ProductImgMapper tblProProductImgMapper;

}
