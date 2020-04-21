package com.baibei.hengjia.admin.modules.customer.service.impl;

import com.baibei.hengjia.admin.modules.customer.bean.vo.PickUpDataVo;
import com.baibei.hengjia.admin.modules.customer.dao.CustomerAddressMapper;
import com.baibei.hengjia.admin.modules.customer.model.CustomerAddress;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerAddressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/18 17:33:16
* @description: CustomerAddress服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerAddressServiceImpl extends AbstractService<CustomerAddress> implements ICustomerAddressService {

    @Autowired
    private CustomerAddressMapper tblCustomerAddressMapper;

    @Override
    public List<PickUpDataVo> getPickUpData(String customerNo) {
        List<PickUpDataVo> pickUpDataVos=new ArrayList<PickUpDataVo>();
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerAddress> customerAddresses=tblCustomerAddressMapper.selectByCondition(condition);
        if(customerAddresses.size()>0){
            PickUpDataVo pickUpDataVo=new PickUpDataVo();
            pickUpDataVo.setReceivingName(customerAddresses.get(0).getReceivingName());
            pickUpDataVo.setMobile(customerAddresses.get(0).getReceivingMobile());
            String address=customerAddresses.get(0).getProvince()+customerAddresses.get(0).getCity()+customerAddresses.get(0).getArea()+customerAddresses.get(0).getReceivingAddress();
            pickUpDataVo.setReceivingAddress(address);
            pickUpDataVos.add(pickUpDataVo);
        }
        return pickUpDataVos;
    }
}
