package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.cash.dao.SignInBackMapper;
import com.baibei.hengjia.api.modules.cash.model.SignInBack;
import com.baibei.hengjia.api.modules.cash.service.ISignInBackService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/06/18 14:50:10
 * @description: SignInBack服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SignInBackServiceImpl extends AbstractService<SignInBack> implements ISignInBackService {

    @Autowired
    private SignInBackMapper tblCashSignInBackMapper;

    @Override
    public Boolean isToDaySignInBack(String status) {
        log.info("status={}",status);
        List<SignInBack> result = tblCashSignInBackMapper.findSignInBackByToday(status, LocalDate.now());
        log.info("result={}",result.size());
        if (result.size() > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
