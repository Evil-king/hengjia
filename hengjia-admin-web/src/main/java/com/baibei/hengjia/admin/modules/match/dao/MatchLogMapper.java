package com.baibei.hengjia.admin.modules.match.dao;

import com.baibei.hengjia.admin.modules.match.bean.dto.MatchLogDto;
import com.baibei.hengjia.admin.modules.match.bean.vo.MatchLogVo;
import com.baibei.hengjia.admin.modules.match.model.MatchLog;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface MatchLogMapper extends MyMapper<MatchLog> {
    List<MatchLogVo> myList(MatchLogDto matchLogDto);
}