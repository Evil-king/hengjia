package com.baibei.hengjia.admin.modules.customer.service;
import com.baibei.hengjia.admin.modules.customer.bean.vo.PickUpDataVo;
import com.baibei.hengjia.admin.modules.customer.model.CustomerAddress;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: hyc
* @date: 2019/07/18 17:33:16
* @description: CustomerAddress服务接口
*/
public interface ICustomerAddressService extends Service<CustomerAddress> {

    List<PickUpDataVo> getPickUpData(String customerNo);
}
