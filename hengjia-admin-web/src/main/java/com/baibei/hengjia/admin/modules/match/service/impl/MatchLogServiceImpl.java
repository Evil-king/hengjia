package com.baibei.hengjia.admin.modules.match.service.impl;

import com.baibei.hengjia.admin.modules.match.bean.dto.MatchLogDto;
import com.baibei.hengjia.admin.modules.match.bean.vo.MatchLogVo;
import com.baibei.hengjia.admin.modules.match.dao.MatchLogMapper;
import com.baibei.hengjia.admin.modules.match.model.MatchLog;
import com.baibei.hengjia.admin.modules.match.service.IMatchLogService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/17 15:32:26
* @description: MatchLog服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MatchLogServiceImpl extends AbstractService<MatchLog> implements IMatchLogService {

    @Autowired
    private MatchLogMapper matchLogMapper;

    @Override
    public MyPageInfo<MatchLogVo> pageList(MatchLogDto matchLogDto) {
        PageHelper.startPage(matchLogDto.getCurrentPage(), matchLogDto.getPageSize());
        List<MatchLogVo> list = matchLogMapper.myList(matchLogDto);
        MyPageInfo<MatchLogVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }
}
