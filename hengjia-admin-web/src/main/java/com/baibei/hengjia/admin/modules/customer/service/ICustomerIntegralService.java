package com.baibei.hengjia.admin.modules.customer.service;
import com.baibei.hengjia.admin.modules.customer.model.CustomerIntegral;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/07/16 14:39:31
* @description: CustomerIntegral服务接口
*/
public interface ICustomerIntegralService extends Service<CustomerIntegral> {

    CustomerIntegral selectByCustomerNo(String customerNo);
}
