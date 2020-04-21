package com.baibei.hengjia.api.modules.user.service;
import com.baibei.hengjia.api.modules.user.model.CustomerDetail;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/06/03 16:01:58
* @description: CustomerDetail服务接口
*/
public interface ICustomerDetailService extends Service<CustomerDetail> {

    CustomerDetail findByCustomerNo(String customerNo);
}
