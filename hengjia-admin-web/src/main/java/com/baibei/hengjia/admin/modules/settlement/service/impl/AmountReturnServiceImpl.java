package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.AmountReturnDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.AmountReturnVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.dao.AmountReturnMapper;
import com.baibei.hengjia.admin.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.admin.modules.settlement.model.CleanData;
import com.baibei.hengjia.admin.modules.settlement.service.IAmountReturnService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/12 10:55:58
 * @description: AmountReturn服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AmountReturnServiceImpl extends AbstractService<AmountReturn> implements IAmountReturnService {

    @Autowired
    private AmountReturnMapper tblSetAmountReturnMapper;

    @Override
    public MyPageInfo<AmountReturnVo> pageList(AmountReturnDto amountReturnDto) {
        Condition condition = new Condition(AmountReturn.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(amountReturnDto.getBatchNo())) {
            criteria.andEqualTo("batchNo", amountReturnDto.getBatchNo());
        }
        if (!StringUtils.isEmpty(amountReturnDto.getCustomerNo())) {
            criteria.andEqualTo("customerNo", amountReturnDto.getCustomerNo());
        }
        if (!StringUtils.isEmpty(amountReturnDto.getStatus())) {
            criteria.andEqualTo("status", amountReturnDto.getStatus());
        }
        if (!StringUtils.isEmpty(amountReturnDto.getType())) {
            criteria.andEqualTo("type", amountReturnDto.getType());
        }
        MyPageInfo<AmountReturn> pageInfo = pageList(condition, PageParam.buildWithDefaultSort(amountReturnDto.getCurrentPage(), amountReturnDto.getPageSize()));
        return PageUtil.transform(pageInfo, AmountReturnVo.class);
    }
}
