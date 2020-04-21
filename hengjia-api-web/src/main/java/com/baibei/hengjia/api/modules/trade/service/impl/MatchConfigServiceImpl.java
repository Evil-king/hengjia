package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.api.modules.trade.dao.*;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;


/**
* @author: Longer
* @date: 2019/06/05 10:46:05
* @description: MatchConfigServiceImpl
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MatchConfigServiceImpl extends AbstractService<MatchConfig> implements IMatchConfigService {

    @Autowired
    private MatchConfigMapper matchConfigMapper;
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public MatchConfig getSwitch(String switchType) {
        Condition condition = new Condition(MatchConfig.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("switchType",switchType);
        List<MatchConfig> matchConfigs = matchConfigMapper.selectByCondition(condition);
        return matchConfigs.size()==0?null:matchConfigs.get(0);
    }

    @Override
    public int matchSwitch(String swtch,String switchType) {
        Map map= new HashMap();
        map.put("swtch",swtch);
        map.put("switchType",switchType);
        int i = matchConfigMapper.updateSwitch(map);
        return i;
    }

    @Override
    public int matchSwitchByTradeDay(String swtch, String switchType) {
        boolean tradeTime = tradeDayService.isTradeTime();//是否交易时间
        boolean tradeDay = tradeDayService.isTradeDay(new Date());//是否交易日
        if(!tradeDay){
            throw new ServiceException("非交易日，不能操作开关");
        }
        if(tradeTime){
            throw new ServiceException("未休市，不能操作开关");
        }
        Condition condition = new Condition(MatchConfig.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("switchType",switchType);
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setMatchSwitch(swtch);
        matchConfig.setModifyTime(new Date());
        return matchConfigMapper.updateByConditionSelective(matchConfig, condition);
    }

    @Override
    public void test() {
        /*MatchConfig matchConfig = new MatchConfig();
        matchConfig.setMatchSwitch("on");
        matchConfig.setSwitchType("aaaa");
        matchConfig.setCreateTime(new Date());
        matchConfig.setModifyTime(new Date());
        matchConfig.setFlag(new Byte("1"));
        matchConfigMapper.insert(matchConfig);

        MatchConfig matchConfig2 = new MatchConfig();
        matchConfig2.setId(matchConfig.getId());
        matchConfig2.setSwitchType("bbbb");
        matchConfigMapper.updateByPrimaryKeySelective(matchConfig2);*/

        MatchConfig matchConfig3 = new MatchConfig();
        matchConfig3.setId(6L);
        MatchConfig matchConfig1 = this.findById(6L);
        System.out.println(matchConfig1);
        matchConfig1.setMatchSwitch("off");
        int i = matchConfigMapper.updateByPrimaryKey(matchConfig1);
        MatchConfig matchConfig4 = this.findById(6L);
        System.out.println(matchConfig4);
        System.out.println("aaa");

    }
}
