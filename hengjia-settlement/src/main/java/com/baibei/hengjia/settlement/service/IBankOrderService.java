package com.baibei.hengjia.settlement.service;

import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.settlement.bean.dto.BankFileDto;
import com.baibei.hengjia.settlement.model.BankOrder;

/**
 * @Classname IBankOrderService
 * @Description 银行出入金流水服务
 * @Date 2019/6/25 11:33
 * @Created by Longer
 */
public interface IBankOrderService extends Service<BankOrder> {

    /**
     *  读取银行出入金流水文件，并插入数据库
     * @param bankFileDto
     */
    void bankOrder(BankFileDto bankFileDto);

}
