package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.HoldDetailsMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldDetails;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldDetailsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/07/15 13:39:41
 * @description: HoldDetails服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldDetailsServiceImpl extends AbstractService<HoldDetails> implements IHoldDetailsService {

    @Autowired
    private HoldDetailsMapper tblTraHoldDetailsMapper;


    public MyPageInfo<HoldDetailsVo> pageList(HoldDetailsDto holdDetailsDto) {
        PageHelper.startPage(holdDetailsDto.getCurrentPage(), holdDetailsDto.getPageSize());
        List<HoldDetailsVo> holdDetailsVoList = tblTraHoldDetailsMapper.findUserHoldDetails(holdDetailsDto);
        MyPageInfo<HoldDetailsVo> result = new MyPageInfo<>(holdDetailsVoList);
        return result;
    }

    @Override
    public List<HoldDetailsVo> HoldDetailsVoList(HoldDetailsDto holdDetailsDto) {
        List<HoldDetailsVo> holdDetailsVoList = tblTraHoldDetailsMapper.findUserHoldDetails(holdDetailsDto);
        return holdDetailsVoList;
    }
}
