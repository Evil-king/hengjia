package com.baibei.hengjia.api.modules.cash.template;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.common.tool.api.ApiResult;

import javax.servlet.http.HttpServletRequest;

public abstract class PayTemplate<O> {

    public ApiResult doPosscess(JSONObject object) {
        //1、进行校验方法
        ApiResult apiResult = validate(object);
        //2、创建订单
        if (apiResult.hasSuccess()) {
            TempDeposit order = createOrder(object);
            //3、发送各自渠道
            apiResult = sendToChannel(order, object.getString("templeStr"));
        }
        return apiResult;
    }

    public String callBack(HttpServletRequest request){
        return payCallback(request);
    }

    /**
     * 校验方法
     *
     * @param o
     * @return
     */
    protected abstract ApiResult validate(JSONObject o);

    /**
     * 创建订单
     *
     * @param o
     * @return
     */
    protected abstract TempDeposit createOrder(JSONObject o);

    /**
     * 发送到各自的渠道
     *
     * @param order
     * @param templeStr 额外字段
     */
    protected abstract ApiResult sendToChannel(TempDeposit order, String templeStr);

    /**
     * 回调
     * @param request
     * @return
     */
    protected abstract String payCallback(HttpServletRequest request);

}
