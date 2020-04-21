package com.baibei.hengjia.admin.modules.customer.service;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CouponAccountVo;
import com.baibei.hengjia.admin.modules.customer.model.CouponAccount;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/09/05 14:07:50
* @description: CouponAccount服务接口
*/
public interface ICouponAccountService extends Service<CouponAccount> {
    /**
     * 获取该用户名下所有的券余额
     * @param customerNo
     * @return
     */
    BigDecimal findByCustomerNo(String customerNo);

    List<CouponAccountVo> findProductByCustomerNo(String customerNo);
}
