package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.api.modules.advisory.dao.AdvisoryVideoMapper;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryVedio服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryVideoServiceImpl extends AbstractService<AdvisoryVideo> implements IAdvisoryVideoService {

    @Autowired
    private AdvisoryVideoMapper advisoryVideoMapper;

    @Override
    public AdvisoryVideoVo details(String id) {
        AdvisoryVideoVo advisoryVideoVo = advisoryVideoMapper.queryByParams(id);
        return advisoryVideoVo;
    }
}
