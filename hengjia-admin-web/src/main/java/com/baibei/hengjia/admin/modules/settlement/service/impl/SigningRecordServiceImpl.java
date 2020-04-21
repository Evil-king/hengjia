package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.settlement.dao.SigningRecordMapper;
import com.baibei.hengjia.admin.modules.settlement.model.SigningRecord;
import com.baibei.hengjia.admin.modules.settlement.service.ISigningRecordService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: uqing
* @date: 2019/07/16 10:29:04
* @description: SigningRecord服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class SigningRecordServiceImpl extends AbstractService<SigningRecord> implements ISigningRecordService {

    @Autowired
    private SigningRecordMapper tblTraSigningRecordMapper;

    @Override
    public SigningRecord selectByCustomerNo(String customerNo) {
        Condition condition=new Condition(SigningRecord.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("thirdCustId",customerNo);
        criteria.andNotEqualTo("funcFlag",Constants.SigningStatus.SIGNING_DELETE);
        List<SigningRecord> signingRecordList=tblTraSigningRecordMapper.selectByCondition(condition);
        if(signingRecordList.size()>0){
            return signingRecordList.get(0);
        }else{
            return null;
        }
    }
}
