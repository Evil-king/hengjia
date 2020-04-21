package com.baibei.hengjia.admin.modules.match.service;
import com.baibei.hengjia.admin.modules.match.bean.dto.MatchLogDto;
import com.baibei.hengjia.admin.modules.match.bean.vo.MatchLogVo;
import com.baibei.hengjia.admin.modules.match.model.MatchLog;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
* @author: Longer
* @date: 2019/07/17 15:32:26
* @description: MatchLog服务接口
*/
public interface IMatchLogService extends Service<MatchLog> {

    MyPageInfo<MatchLogVo> pageList(MatchLogDto matchLogDto);
}
