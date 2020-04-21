package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.CleanDataMapper;
import com.baibei.hengjia.api.modules.settlement.model.CleanData;
import com.baibei.hengjia.api.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/26 18:54:38
 * @description: CleanData服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CleanDataServiceImpl extends AbstractService<CleanData> implements ICleanDataService {

    @Autowired
    private CleanDataMapper tblSetCleanDataMapper;

    @Override
    public List<CleanData> findByBatchNo(String batchNo) {
        Condition condition = new Condition(CleanData.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }

    @Override
    public List<String> findCleanSuccessCustomerNo(String batchNo) {
        Condition condition = new Condition(CleanData.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("cleanStatus", Constants.CleanStatus.SUCCESS).
                andEqualTo("flag", Constants.Flag.VALID);
        List<CleanData> list = findByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            log.info("batchNo={}不存在清算成功的用户");
            return null;
        }
        List<String> result = new ArrayList<>();
        for (CleanData item : list) {
            result.add(item.getThirdCustId());
        }
        return result;
    }

    @Override
    public boolean isCleanSuccess(String batchNo, String customerNo) {
        Condition condition = new Condition(CleanData.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("cleanStatus", Constants.CleanStatus.SUCCESS).
                andEqualTo("thirdCustId", customerNo).
                andEqualTo("flag", Constants.Flag.VALID);
        List<CleanData> list = findByCondition(condition);
        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public Boolean isCleanSuccess(String customerNo) {
        Condition condition = new Condition(CleanData.class);
        condition.createCriteria().andEqualTo("cleanStatus", Constants.CleanStatus.SUCCESS).
                andEqualTo("thirdCustId", customerNo).
                andEqualTo("flag", Constants.Flag.VALID);
        List<CleanData> list = findByCondition(condition);
        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public void updateCleanFail(String batchNo, List<String> failCustomerNoList) {
        if (CollectionUtils.isEmpty(failCustomerNoList)) {
            return;
        }
        Condition condition = new Condition(CleanData.class);
        condition.createCriteria().andIn("thirdCustId", failCustomerNoList)
                .andEqualTo("flag", Constants.Flag.VALID);
        CleanData cleanData = new CleanData();
        cleanData.setModifyTime(new Date());
        cleanData.setCleanStatus(Constants.CleanStatus.FAIL);
        this.updateByConditionSelective(cleanData, condition);
    }

    @Override
    public void updateCleanSuccess(String batchNo, List<String> failCustomerNoList) {
        Condition condition = new Condition(CleanData.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        if (!CollectionUtils.isEmpty(failCustomerNoList)) {
            criteria.andNotIn("thirdCustId", failCustomerNoList);
        }
        CleanData cleanData = new CleanData();
        cleanData.setModifyTime(new Date());
        cleanData.setCleanStatus(Constants.CleanStatus.SUCCESS);
        this.updateByConditionSelective(cleanData, condition);
    }

    @Override
    public void softDelete(List<Long> idList) {
        Condition condition = new Condition(CleanData.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("id", idList);
        CleanData cleanData = new CleanData();
        cleanData.setModifyTime(new Date());
        cleanData.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
        this.updateByConditionSelective(cleanData, condition);
    }
}
