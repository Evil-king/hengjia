package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.api.modules.advisory.dao.AdvisoryBannerMapper;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryBannerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author: wenq
 * @date: 2019/09/11 10:09:00
 * @description: AdvisoryBanner服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryBannerServiceImpl extends AbstractService<AdvisoryBanner> implements IAdvisoryBannerService {

    @Autowired
    private AdvisoryBannerMapper advisoryBannerMapper;

    @Override
    public List<AdvisoryBannerVo> index() {
        List<AdvisoryBannerVo> advisoryBannerVoList = advisoryBannerMapper.objIndex();
        return advisoryBannerVoList;
    }

}
