package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CustDzFailVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.FailResultVo;
import com.baibei.hengjia.admin.modules.settlement.dao.CustDzFailMapper;
import com.baibei.hengjia.admin.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.admin.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.admin.modules.settlement.service.ICustDzFailService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/16 15:12:49
 * @description: CustDzFail服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustDzFailServiceImpl extends AbstractService<CustDzFail> implements ICustDzFailService {

    @Autowired
    private CustDzFailMapper tblSetCustDzFailMapper;

    @Override
    public MyPageInfo<CustDzFailVo> pageList(CleanResultDto cleanResultDto) {
        PageHelper.startPage(cleanResultDto.getCurrentPage(), cleanResultDto.getPageSize());
        List<CustDzFail> list = tblSetCustDzFailMapper.list(cleanResultDto);
        if (CollectionUtils.isEmpty(list)) {
            return new MyPageInfo<>(new ArrayList<>());
        }
        MyPageInfo<CustDzFail> myPageInfo = new MyPageInfo<>(list);
        return PageUtil.transform(myPageInfo, CustDzFailVo.class);
    }

    @Override
    public List<CustDzFailVo> CustDzFailVoList(CleanResultDto cleanResultDto) {
        List<CustDzFail> list = tblSetCustDzFailMapper.list(cleanResultDto);
        List<CustDzFailVo> custDzFailVos=BeanUtil.copyProperties(list,CustDzFailVo.class);
        return custDzFailVos;
    }
}
