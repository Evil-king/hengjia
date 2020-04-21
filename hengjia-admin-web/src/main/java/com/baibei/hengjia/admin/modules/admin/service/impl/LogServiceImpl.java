package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.dao.LogMapper;
import com.baibei.hengjia.admin.modules.admin.model.Log;
import com.baibei.hengjia.admin.modules.admin.service.ILogService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* @author: 会跳舞的机器人
* @date: 2019/05/31 16:32:12
* @description: Log服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class LogServiceImpl extends AbstractService<Log> implements ILogService {

    @Autowired
    private LogMapper logMapper;

}
