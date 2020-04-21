package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.product.dao.ProductMapper;
import com.baibei.hengjia.api.modules.product.model.Product;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradeRecordVo;
import com.baibei.hengjia.api.modules.trade.dao.AutoRecordMapper;
import com.baibei.hengjia.api.modules.trade.model.AutoRecord;
import com.baibei.hengjia.api.modules.trade.service.IAutoRecordService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoRecord服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AutoRecordServiceImpl extends AbstractService<AutoRecord> implements IAutoRecordService {

    @Autowired
    private AutoRecordMapper tblTradeAutoRecordMapper;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public MyPageInfo<AutoTradeRecordVo> pageList(CustomerBaseAndPageDto dto) {
        Date tradeDay = tradeDayService.getAddNTradeDay(-10);
        Map<String, Object> params = new HashMap<>();
        params.put("customerNo", dto.getCustomerNo());
        params.put("tradeDay", tradeDay);
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<AutoTradeRecordVo> list = tblTradeAutoRecordMapper.pageList(params);
        MyPageInfo<AutoTradeRecordVo> myPageInfo = new MyPageInfo<>(list);
        if (!CollectionUtils.isEmpty(myPageInfo.getList())) {
            for (AutoTradeRecordVo vo : myPageInfo.getList()) {
                Condition condition = new Condition(Product.class);
                Example.Criteria criteria = buildValidCriteria(condition);
                criteria.andEqualTo("spuNo", vo.getSpuNo());
                //图片地址
                List<Product> productImgList = productMapper.selectByCondition(condition);
                vo.setImageUrl(CollectionUtils.isEmpty(productImgList) ? null : productImgList.get(0).getImgUrl());
            }
        }
        return new MyPageInfo<>(list);
    }
}
