package com.baibei.hengjia.admin.modules.settlement.service;
import com.baibei.hengjia.admin.modules.customer.model.CustomerIntegral;
import com.baibei.hengjia.admin.modules.settlement.model.SigningRecord;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: uqing
* @date: 2019/07/16 10:29:04
* @description: SigningRecord服务接口
*/
public interface ISigningRecordService extends Service<SigningRecord> {

    SigningRecord selectByCustomerNo(String customerNo);
}
