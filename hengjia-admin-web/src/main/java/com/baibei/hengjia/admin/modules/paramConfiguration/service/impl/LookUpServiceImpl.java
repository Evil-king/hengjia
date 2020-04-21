package com.baibei.hengjia.admin.modules.paramConfiguration.service.impl;

import com.baibei.hengjia.admin.modules.paramConfiguration.dao.LookUpMapper;
import com.baibei.hengjia.admin.modules.paramConfiguration.model.LookUp;
import com.baibei.hengjia.admin.modules.paramConfiguration.service.ILookUpService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: wenqing
* @date: 2019/09/04 15:17:57
* @description: LookUp服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class LookUpServiceImpl extends AbstractService<LookUp> implements ILookUpService {

    @Autowired
    private LookUpMapper lookUpMapper;

    @Override
    public int addLogistics(String name) {
        LookUp lookUp = new LookUp();
        lookUp.setClasscode("logistics");
        lookUp.setDescription(name);
        lookUp.setCode(name);
        lookUp.setName(name);
        return lookUpMapper.insert(lookUp);
    }

    @Override
    public int deleteToId(long id) {
        return lookUpMapper.deleteById(id);
    }

    @Override
    public int selectByName(String name) {
        return lookUpMapper.selectByName(name);
    }
}
