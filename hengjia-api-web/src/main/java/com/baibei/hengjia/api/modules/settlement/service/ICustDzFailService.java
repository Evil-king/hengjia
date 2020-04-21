package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/27 16:10:54
 * @description: CustDzFail服务接口
 */
public interface ICustDzFailService extends Service<CustDzFail> {

    List<CustDzFail> findByBatchNo(String batchNo);

}
