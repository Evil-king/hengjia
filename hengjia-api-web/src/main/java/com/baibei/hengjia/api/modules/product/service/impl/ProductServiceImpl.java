package com.baibei.hengjia.api.modules.product.service.impl;

import com.baibei.hengjia.api.modules.account.service.ICustomerIntegralService;
import com.baibei.hengjia.api.modules.product.dao.ProductMapper;
import com.baibei.hengjia.api.modules.product.model.Product;
import com.baibei.hengjia.api.modules.product.service.IProductService;
import com.baibei.hengjia.api.modules.shop.bean.vo.ShopHomeVO;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 16:16:39
* @description: Product服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends AbstractService<Product> implements IProductService {

    @Autowired
    private ProductMapper tblProProductMapper;
    @Autowired
    private ICustomerIntegralService customerIntegralService;

    @Override
    public MyPageInfo<ShopHomeVO> getHomeData(PageParam pageParam, CustomerBaseDto customerBaseDto) {
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",1);
        criteria.andEqualTo("updown","up");
        criteria.andEqualTo("productType","integral");

        MyPageInfo<Product> myPageInfo = pageList(condition, PageParam.buildWithDefaultSort(pageParam.getCurrentPage(), pageParam.getPageSize()));

        return PageUtil.transform(myPageInfo, ShopHomeVO.class);
    }

    @Override
    public void cutStock(String spuNo, Integer stock, String type) {
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",1);
        criteria.andEqualTo("updown","up");
        criteria.andEqualTo("spuNo",spuNo);
        criteria.andEqualTo("productType",type);
        List<Product> products = tblProProductMapper.selectByCondition(condition);
        if(CollectionUtils.isEmpty(products)){
            throw new ServiceException("该商品不存在");
        }
        if(products.get(0).getStock() < stock){
            throw new ServiceException("该商品库存不足");
        }
        Product product = products.get(0);
        product.setStock(product.getStock() - stock);
        tblProProductMapper.updateByPrimaryKeySelective(product);
    }
}
