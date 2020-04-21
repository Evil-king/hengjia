package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.TradeAddressMapper;
import com.baibei.hengjia.api.modules.trade.model.TradeAddress;
import com.baibei.hengjia.api.modules.trade.service.ITradeAddressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


/**
* @author: Longer
* @date: 2019/06/03 15:37:24
* @description: TradeAddress服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeAddressServiceImpl extends AbstractService<TradeAddress> implements ITradeAddressService {

    @Autowired
    private TradeAddressMapper addressMapper;

    @Override
    public void saveOrUpdate(TradeAddress address) {
        if(address.getId()!=null){//更新
            TradeAddress tradeAddress = addressMapper.selectByPrimaryKey(address.getId());
            tradeAddress.setFlag(0);//禁用原来的
            addressMapper.updateByPrimaryKey(tradeAddress);
            //重新插入一条数据
            address.setId(null);
        }
        address.setDefaultflag(1);//设置为默认地址
        Date cuurentDate =  new Date();
        address.setCreatetime(cuurentDate);
        address.setUpdatetime(cuurentDate);
        addressMapper.updateDefault(address);
        addressMapper.insert(address);
    }
}
