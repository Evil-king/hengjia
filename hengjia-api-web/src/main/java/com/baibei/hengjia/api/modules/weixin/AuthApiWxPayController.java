package com.baibei.hengjia.api.modules.weixin;

import com.baibei.hengjia.api.modules.cash.bean.vo.JsSDK;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.api.modules.weixin.bean.JsSdkUrl;
import com.baibei.hengjia.api.modules.weixin.service.WeixinService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/17 4:54 PM
 * @description:
 */
@RestController
@RequestMapping("/auth/api/wx")
public class AuthApiWxPayController {
    @Value("${weixin.appid}")
    private String appId;
    @Value("${weixin.appsecret}")
    private String appSecret;
    @Value("${weixin.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private WeixinService weixinService;

    // 微信授权地址
    private String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

    /**
     * 获取微信授权地址
     *
     * @return
     */
    @PostMapping("/getAuthUrl")
    public ApiResult<String> getAuthUrl(@RequestBody @Validated CustomerBaseDto customerBaseDto) {
        Customer customer = customerService.findByCustomerNo(customerBaseDto.getCustomerNo());
        if (customer == null) {
            return ApiResult.badParam("客户不存在");
        }
        String newRedirectUrl = new StringBuffer(redirectUrl).append("?customerNo=").append(customerBaseDto.getCustomerNo()).
                append("&memberNo=").append(customer.getMemberNo()).toString();
        String result = url.replace("APPID", appId).replace("REDIRECT_URI", URLEncoder.encode(newRedirectUrl));
        return ApiResult.success(result);
    }

    /**
     * 获取前端jssdk签名等相关信息
     *
     * @return
     */
    @PostMapping("/jssdkInfo")
    public ApiResult<JsSDK> jssdkInfo(@Validated @RequestBody JsSdkUrl jsSdkUrl) {
        JsSDK jsSDK = weixinService.jssdkInfo(jsSdkUrl.getUrl());
        if (jsSDK == null) {
            return ApiResult.error("获取失败");
        }
        return ApiResult.success(jsSDK);
    }

}
