package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.bean.dto.WeiPayDto;
import com.baibei.hengjia.common.tool.api.ApiResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IWeiXinPayService {

    /**
     * 微信H5支付
     * @param weiPayDto
     * @return
     */
    ApiResult weiH5Pay(WeiPayDto weiPayDto);

    /**
     * 微信H5支付回调
     * @param request
     * @param response
     * @return
     */
    void weiH5PayCallBack(HttpServletRequest request, HttpServletResponse response);
}
