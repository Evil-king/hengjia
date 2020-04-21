package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.dao.MatchAuthorityMapper;
import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/06 15:24:04
* @description: MatchAuthority服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MatchAuthorityServiceImpl extends AbstractService<MatchAuthority> implements IMatchAuthorityService {

    @Autowired
    private MatchAuthorityMapper matchAuthorityMapper;
    @Value("${match.authority}")
    private int matchAuthority;

    @Override
    public int detuchAuthority(String customerNo, String productTradeNo) {
        return matchAuthorityMapper.updateAuthority(customerNo,productTradeNo);
    }

    @Override
    public void addOrRefleshAuthority(String customerNo, String productTradeNo) {
        if (StringUtils.isEmpty(customerNo)||StringUtils.isEmpty(productTradeNo)) {
            throw new ServiceException("增加或刷新配货权失败，参数异常");
        }
        //查询用户是否有过配货权
        MatchAuthority authority = this.getAuthority(customerNo, productTradeNo);
        if (StringUtils.isEmpty(authority)) {//未曾有过
            MatchAuthority customerAuthority = new MatchAuthority();
            customerAuthority.setCustomerNo(customerNo);
            customerAuthority.setProductTradeNo(productTradeNo);
            customerAuthority.setMatchAuthority(matchAuthority);
            customerAuthority.setStatus(Constants.MatchAuthorityStatus.USABLE);
            matchAuthorityMapper.insertSelective(customerAuthority);
        }else{
            //刷新配货权
            authority.setMatchAuthority(matchAuthority);
            authority.setStatus(Constants.MatchAuthorityStatus.USABLE);
            matchAuthorityMapper.updateByPrimaryKey(authority);
        }
    }

    @Override
    public MatchAuthority getAuthority(String customerNo, String productTradeNo) {
        if (StringUtils.isEmpty(customerNo)||StringUtils.isEmpty(productTradeNo)) {
            throw new ServiceException("获取配货权失败，参数异常");
        }
        Condition condition = new Condition(MatchAuthority.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        List<MatchAuthority> matchAuthorityList = matchAuthorityMapper.selectByCondition(condition);
        if(matchAuthorityList.size()>1){
            throw new ServiceException("should select one but more");
        }
        return matchAuthorityList.size()==0?null:matchAuthorityList.get(0);
    }

    @Override
    public List<String> getCustomerListWithAuthorityZero() {
        return matchAuthorityMapper.selectCustomerListWithAuthorityZero();
    }

    @Override
    public List<MatchAuthority> getListWithAuthority() {
        Condition condition = new Condition(MatchAuthority.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andGreaterThan("matchAuthority",0);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<MatchAuthority> matchAuthorityList = matchAuthorityMapper.selectByCondition(condition);
        return matchAuthorityList;
    }
}
