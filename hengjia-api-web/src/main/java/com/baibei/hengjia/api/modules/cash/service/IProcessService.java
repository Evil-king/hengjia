package com.baibei.hengjia.api.modules.cash.service;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.common.tool.api.ApiResult;

/**
 * @author hwq
 * @date 2019/06/22
 */
public interface IProcessService {

    /**
     * 校验方法
     * @param o
     * @return
     */
    ApiResult validate(JSONObject o);

    /**
     * 创建订单
     * @param o
     * @return
     */
    TempDeposit createOrder(JSONObject o);

    /**
     * 更新订单
     * @param customerNo
     * @param orderNo
     * @param outOrderNo
     * @param type
     * @return
     */
    int updateOrder(String customerNo, String orderNo, String outOrderNo, String type);

}
