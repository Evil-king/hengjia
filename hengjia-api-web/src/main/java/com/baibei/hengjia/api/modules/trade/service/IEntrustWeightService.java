package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.EntrustWeight;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.Date;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/07 10:19:25
 * @description: EntrustWeight服务接口
 */
public interface IEntrustWeightService extends Service<EntrustWeight> {

    /**
     * 计算挂单排序权重
     *
     * @param customerNo
     */
    void calculation(String customerNo, String productTradeNo,Date nextTradeDay);

    /**
     * 全部删除
     *
     * @return
     */
    int deleteAll();

}
