package com.baibei.hengjia.api.modules.user.service;
import com.baibei.hengjia.api.modules.user.bean.dto.AddressDto;
import com.baibei.hengjia.api.modules.user.bean.dto.FindAddressDto;
import com.baibei.hengjia.api.modules.user.bean.vo.AddressVo;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;

import java.util.List;

import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:43:47
* @description: CustomerAddress服务接口
*/
public interface ICustomerAddressService extends Service<CustomerAddress> {

    List<CustomerAddress> findByCustomerNo(String customerNo);

    List<AddressVo> getAllAddress(String customerNo) ;

    /**
     * 插入一条地址记录
     * @param addressDto
     */
    ApiResult insertAddress(AddressDto addressDto);

    /**
     * 通过地址ID以及用户编号找到地址
     * @param findAddressDto
     * @return
     */
    CustomerAddress findByAddressId(FindAddressDto findAddressDto);

    /**
     * 通过用户编码获取默认地址
     * @param customerNo
     * @return
     */
    CustomerAddress findDefaultByNo(String customerNo);

    void deleteAddress(Long id);
}
