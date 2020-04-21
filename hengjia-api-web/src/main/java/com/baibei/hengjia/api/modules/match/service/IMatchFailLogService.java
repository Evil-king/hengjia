package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.vo.BaseMatchUsersVo;
import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: MatchFailLog服务接口
*/
public interface IMatchFailLogService extends Service<MatchFailLog> {

    /**
     * 根据状态获取失败流水集合
     * @return
     */
    List<MatchFailLog> getFailLogsByStatus(String status);

    /**
     * 获取当天的失败单
     * @param status
     * @return
     */
    List<MatchFailLog> getCurrentDayFailLogs(String status);

    /**
     * 更新
     * @param matchFailLog
     */
    int modifyFailLog(MatchFailLog matchFailLog);

    void destroyFailLog(String status);
}
