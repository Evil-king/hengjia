package com.baibei.hengjia.admin.modules.product.service.impl;

import com.baibei.hengjia.admin.modules.product.bean.dto.TradeProductPageListDto;
import com.baibei.hengjia.admin.modules.product.bean.vo.TradeProductVo;
import com.baibei.hengjia.admin.modules.product.dao.TradeProductMapper;
import com.baibei.hengjia.admin.modules.product.model.TradeProduct;
import com.baibei.hengjia.admin.modules.product.service.ITradeProductService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/02 18:48:18
 * @description: TradeProduct服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeProductServiceImpl extends AbstractService<TradeProduct> implements ITradeProductService {

    @Autowired
    private TradeProductMapper tblTradeProductMapper;

    @Override
    public MyPageInfo<TradeProductVo> pageList(TradeProductPageListDto tradeProductPageListDto) {
        Condition condition = new Condition(TradeProduct.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(tradeProductPageListDto.getSpuNo())) {
            criteria.andEqualTo("spuNo", tradeProductPageListDto.getSpuNo());
        }
        if (!StringUtils.isEmpty(tradeProductPageListDto.getCustomerNo())) {
            criteria.andEqualTo("customerNo", tradeProductPageListDto.getCustomerNo());
        }
        MyPageInfo<TradeProduct> pageInfo = pageList(condition, PageParam.build(tradeProductPageListDto.getCurrentPage(), tradeProductPageListDto.getPageSize()));
        return PageUtil.transform(pageInfo, TradeProductVo.class);
    }
}
