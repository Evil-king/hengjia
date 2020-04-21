package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.FailResultVo;
import com.baibei.hengjia.admin.modules.settlement.dao.BatFailResultMapper;
import com.baibei.hengjia.admin.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.admin.modules.settlement.service.IBatFailResultService;
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
 * @description: BatFailResult服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BatFailResultServiceImpl extends AbstractService<BatFailResult> implements IBatFailResultService {

    @Autowired
    private BatFailResultMapper tblSetBatFailResultMapper;

    @Override
    public MyPageInfo<FailResultVo> pageList(CleanResultDto cleanResultDto) {
        PageHelper.startPage(cleanResultDto.getCurrentPage(), cleanResultDto.getPageSize());
        List<BatFailResult> list = tblSetBatFailResultMapper.list(cleanResultDto);
        if (CollectionUtils.isEmpty(list)) {
            return new MyPageInfo<>(new ArrayList<>());
        }
        MyPageInfo<BatFailResult> myPageInfo = new MyPageInfo<>(list);
        return PageUtil.transform(myPageInfo, FailResultVo.class);
    }

    @Override
    public List<FailResultVo> FailResultVoList(CleanResultDto cleanResultDto) {
        List<BatFailResult> list = tblSetBatFailResultMapper.list(cleanResultDto);
        List<FailResultVo> failResultVos=BeanUtil.copyProperties(list,FailResultVo.class);
        return failResultVos;
    }
}
