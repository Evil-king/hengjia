package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.api.modules.advisory.dao.AdvisoryNavigationMapper;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author: wenq
 * @date: 2019/09/12 14:45:44
 * @description: AdvisoryNavigation服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryNavigationServiceImpl extends AbstractService<AdvisoryNavigation> implements IAdvisoryNavigationService {

    @Autowired
    private AdvisoryNavigationMapper advisoryNavigationMapper;


    @Override
    public List<AdvisoryNavigationListVo> navigationList() {

        List<AdvisoryNavigationListVo> list = Lists.newArrayList();

        List<AdvisoryNavigation> advisoryNavigationList = advisoryNavigationMapper.selectByParams();
        if (CollectionUtils.isEmpty(advisoryNavigationList)) {
            return list;
        }
        for (int i = 0; i < advisoryNavigationList.size(); i++) {
            AdvisoryNavigationListVo advisoryNavigationListVo = BeanUtil.copyProperties(advisoryNavigationList.get(i),
                    AdvisoryNavigationListVo.class);
            list.add(advisoryNavigationListVo);
        }
        return list;
    }
}
