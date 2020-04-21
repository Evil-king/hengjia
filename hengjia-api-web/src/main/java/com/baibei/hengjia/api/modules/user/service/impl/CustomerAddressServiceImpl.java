package com.baibei.hengjia.api.modules.user.service.impl;

import com.baibei.hengjia.api.modules.user.bean.dto.AddressDto;
import com.baibei.hengjia.api.modules.user.bean.dto.FindAddressDto;
import com.baibei.hengjia.api.modules.user.bean.vo.AddressVo;
import com.baibei.hengjia.api.modules.user.dao.CustomerAddressMapper;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:43:47
* @description: CustomerAddress服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerAddressServiceImpl extends AbstractService<CustomerAddress> implements ICustomerAddressService {

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Override
    public List<CustomerAddress> findByCustomerNo(String customerNo) {
        Condition condition = new Condition(CustomerAddress.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        return findByCondition(condition);
    }
    @Override
    public List<AddressVo> getAllAddress(String customerNo) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerAddress> customerAddresses=customerAddressMapper.selectByCondition(condition);
        List<AddressVo> list=BeanUtil.copyProperties(customerAddresses,AddressVo.class);
        return list;
    }

    @Override
    public ApiResult insertAddress(AddressDto addressDto) {
        CustomerAddress address=BeanUtil.copyProperties(addressDto,CustomerAddress.class);
        address.setId(null);
        address.setDefaultAddress(true);
        address.setCreateTime(new Date());
        address.setModifyTime(new Date());
        address.setFlag(new Byte("1"));
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",addressDto.getCustomerNo());
        List<CustomerAddress> customerAddressList=customerAddressMapper.selectByCondition(condition);
        if(customerAddressList.size()>=1){
            return ApiResult.error("请勿重复保存收货地址");
        }else {
            customerAddressMapper.insertSelective(address);
            return ApiResult.success();
        }

    }

    @Override
    public CustomerAddress findByAddressId(FindAddressDto findAddressDto) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",findAddressDto.getId());
        criteria.andEqualTo("customerNo",findAddressDto.getCustomerNo());
        List<CustomerAddress> customerAddresses=customerAddressMapper.selectByCondition(condition);

        return customerAddresses.size()<1?null:customerAddresses.get(0);
    }

    @Override
    public CustomerAddress findDefaultByNo(String customerNo) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("defaultAddress",true);
        List<CustomerAddress> customerAddresses=customerAddressMapper.selectByCondition(condition);

        return customerAddresses.size()<1?null:customerAddresses.get(0);
    }

    @Override
    public void deleteAddress(Long id) {
        CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setId(id);
        customerAddress.setFlag(new Byte("0"));
        customerAddress.setModifyTime(new Date());
        customerAddressMapper.updateByPrimaryKeySelective(customerAddress);
    }
}
