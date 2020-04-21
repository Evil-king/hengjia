package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.base.BaseRequest;
import com.baibei.hengjia.api.modules.cash.base.BaseResponse;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.Map;


public interface ICashFunctionService<T extends BaseRequest, V extends BaseResponse> {


    /**
     * 获取请求类型
     *
     * @return
     */
    CashFunctionType getType();

    /**
     * 获取银行的请求
     *
     * @param backMessages 请求消息体
     * @return
     */
    String response(Map<String, String> backMessages);

    /**
     * 组成请求参数,发送给银行
     *
     * @return
     */
    ApiResult<V> request(T request);


}
