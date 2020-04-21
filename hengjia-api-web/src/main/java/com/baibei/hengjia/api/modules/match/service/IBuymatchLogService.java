package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.vo.BaseMatchUsersVo;
import com.baibei.hengjia.api.modules.match.model.BuymatchLog;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: BuymatchLog服务接口
*/
public interface IBuymatchLogService extends Service<BuymatchLog> {

    /**
     * 买入配货
     * @param batchNo 批次号(20190806)
     * @return
     */
    ApiResult buyMatch(String batchNo,BaseMatchUsersVo baseMatchUsersVo);

    /**
     * 根据用户编号批次号以及商品交易编号查询配货记录
     * @param productTradeNo
     * @param batchNo
     * @param customerNo
     * @return
     */
    List<BuymatchLog> findByDateAndCustomerAndProductTradeNo(String productTradeNo, String batchNo, String customerNo);

    List<BuymatchLog> findBy(String productTradeNo, String batchNo, String customerNo, BigDecimal discountPrice);

}
