package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.bean.dto.BankFileDto;
import com.baibei.hengjia.api.modules.settlement.model.BankOrder;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;

/**
 * @Classname IBankOrderService
 * @Description 银行出入金流水服务
 * @Date 2019/6/25 11:33
 * @Created by Longer
 */
public interface IBankOrderService extends Service<BankOrder> {

    /**
     * 读取银行出入金流水文件，并插入数据库
     * @param fileName
     * @return 返回批次号
     */
    String bankOrder(String fileName);

    /**
     * 根据批次查询银行出入金流水信息
     * @param batchNo
     * @return
     */
    List<BankOrder> getOrderListByBatchNo(String batchNo);

    BankOrder getOneBankOrder(BankOrder bankOrder);
}
