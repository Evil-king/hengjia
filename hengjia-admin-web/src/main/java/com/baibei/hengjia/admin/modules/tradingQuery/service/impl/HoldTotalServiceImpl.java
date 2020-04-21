package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.HoldTotalMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldTotal;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldTotalService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/07/15 15:30:06
 * @description: HoldTotal服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldTotalServiceImpl extends AbstractService<HoldTotal> implements IHoldTotalService {

    @Autowired
    private HoldTotalMapper tblTraHoldTotalMapper;

    @Override
    public MyPageInfo<HoldTotalVo> pageList(HoldTotalDto holdTotalDto) {
        PageHelper.startPage(holdTotalDto.getCurrentPage(), holdTotalDto.getPageSize());
        List<HoldTotalVo> resultList = tblTraHoldTotalMapper.findUserByHoldTotal(holdTotalDto);
        resultList.stream().forEach(x ->dataConversion(x));
        MyPageInfo<HoldTotalVo> pageInfo = new MyPageInfo(resultList);
        return pageInfo;
    }

    @Override
    public List<HoldTotalVo> getTotal(HoldTotalDto holdTotalDto) {
        List<HoldTotalVo> resultList = tblTraHoldTotalMapper.findUserByHoldTotal(holdTotalDto);
        return resultList;
    }

    @Override
    public List<HoldTotalVo> findUserByHoldTotalService(HoldTotalDto holdTotalDto) {
        List<HoldTotalVo> resultList = tblTraHoldTotalMapper.findUserByHoldTotal(holdTotalDto);
        resultList.stream().forEach(result -> dataConversion(result));
        return resultList;
    }

    public void dataConversion(HoldTotalVo result) {
        if (result.getHoldType().equals(Constants.HoldType.MAIN)) {
            result.setHoldType("零售商品");
        } else if (result.getHoldType().equals(Constants.HoldType.MATCH)) {
            result.setHoldType("折扣商品");
        } else if (result.getHoldType().equals(Constants.HoldType.SEND)) {
            result.setHoldType("赠送");
        }
        result.setRealName(MobileUtils.changeName(result.getRealName()));
        result.setPhoneNumber(MobileUtils.changeMobile(result.getPhoneNumber()));
    }
}
