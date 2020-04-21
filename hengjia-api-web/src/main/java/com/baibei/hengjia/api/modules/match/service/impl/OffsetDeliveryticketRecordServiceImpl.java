package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.dao.OffsetDeliveryticketRecordMapper;
import com.baibei.hengjia.api.modules.match.model.OffsetDeliveryticketRecord;
import com.baibei.hengjia.api.modules.match.service.IOffsetDeliveryticketRecordService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/10/10 09:47:51
* @description: OffsetDeliveryticketRecord服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class OffsetDeliveryticketRecordServiceImpl extends AbstractService<OffsetDeliveryticketRecord> implements IOffsetDeliveryticketRecordService {

    @Autowired
    private OffsetDeliveryticketRecordMapper tblPhOffsetDeliveryticketRecordMapper;

}
