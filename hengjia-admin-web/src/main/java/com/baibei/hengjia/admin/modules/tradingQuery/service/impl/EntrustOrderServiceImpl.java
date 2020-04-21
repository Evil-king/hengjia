package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.EntrustOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.EntrustOrderMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.EntrustOrder;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IEntrustOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/07/18 11:01:43
 * @description: EntrustOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EntrustOrderServiceImpl extends AbstractService<EntrustOrder> implements IEntrustOrderService {

    @Autowired
    private EntrustOrderMapper tblTraEntrustOrderMapper;

    @Override
    public MyPageInfo<EntrustOrderVo> pageList(EntrustOrderDto entrustOrderDto) {
        PageHelper.startPage(entrustOrderDto.getCurrentPage(), entrustOrderDto.getPageSize());
        List<EntrustOrderVo> entrustOrderVoList = tblTraEntrustOrderMapper.findByEntrustOrder(entrustOrderDto);
        entrustOrderVoList.stream().forEach(result->result.setRealName(MobileUtils.changeName(result.getRealName())));
        MyPageInfo<EntrustOrderVo> pageInfo = new MyPageInfo<>(entrustOrderVoList);
        return pageInfo;
    }

    @Override
    public List<EntrustOrderVo> EntrustOrderVoList(EntrustOrderDto entrustOrderDto) {
        List<EntrustOrderVo> entrustOrderVoList = tblTraEntrustOrderMapper.findByEntrustOrder(entrustOrderDto);
        entrustOrderVoList.stream().forEach(result -> dataConversion(result));
        return entrustOrderVoList;
    }

    /**
     * 字段转换
     * @param entrustOrderVo
     */
    public void dataConversion(EntrustOrderVo entrustOrderVo) {
        if ("main".equals(entrustOrderVo.getHoldType())) {
            entrustOrderVo.setHoldType("零售商品");
        } else if ("match".equals(entrustOrderVo.getHoldType())) {
            entrustOrderVo.setHoldType("折扣商品");
        }
        if ("buy".equals(entrustOrderVo.getDirection())) {
            entrustOrderVo.setDirection("买入");
        } else if ("sell".equals(entrustOrderVo.getDirection())) {
            entrustOrderVo.setDirection("卖出");
        }
        if ("all_deal".equals(entrustOrderVo.getResult())) {
            entrustOrderVo.setResult("全部成交");
        } else if ("some_deal".equals(entrustOrderVo.getResult())) {
            entrustOrderVo.setResult("部分成交");
        } else if ("wait_deal".equals(entrustOrderVo.getResult())) {
            entrustOrderVo.setResult("待成交");
        } else if ("revoke".equals(entrustOrderVo.getResult())) {
            entrustOrderVo.setResult("撤单");
        }
        entrustOrderVo.setRealName(MobileUtils.changeName(entrustOrderVo.getRealName()));
    }
}
