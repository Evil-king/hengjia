package com.baibei.hengjia.api.modules.cash.web;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.process.AliPayProcess;
import com.baibei.hengjia.api.modules.cash.process.WeiPayProcess;
import com.baibei.hengjia.api.modules.cash.weiPay.WeiAppPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支付回调
 */
@Slf4j
@RestController
@RequestMapping("/payCallBack")
public class ApiPayCallBackController {

    @Autowired
    private WeiPayProcess weiPayProcess;
    @Autowired
    private AliPayProcess aliPayProcess;


    @RequestMapping("/weiPay/notify")
    public void weixinH5(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("######【微信支付回调】######,request={}", JSONObject.toJSONString(request.getParameterMap()));
        if("success".equals(weiPayProcess.callBack(request))){
            response.getWriter().write(WeiAppPayUtil.setXML("SUCCESS", "OK"));   //告诉微信服务器，我收到信息了，不要在通知我了
        }
    }


    @RequestMapping(value = "/alipay/notify", method = RequestMethod.POST)
    public String notify(HttpServletRequest request) {
        log.info("######【支付宝H5回调】######,request={}", JSONObject.toJSONString(request.getParameterMap()));
        return aliPayProcess.callBack(request);
    }
}
