package com.baibei.hengjia.gateway.weixin;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.gateway.weixin.bean.WeixinOauth2Token;
import com.baibei.hengjia.gateway.weixin.feign.ApiFeign;
import com.baibei.hengjia.gateway.weixin.util.WeixinUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/17 3:37 PM
 * @description: 微信授权重定向URL
 */
@Slf4j
@RestController
@RequestMapping("/wx")
public class WxAuthController {
    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.appsecret}")
    private String appSecret;

    @Value("${hengjia.h5.url}")
    private String hengjiaH5Url;

    @Autowired
    private ApiFeign apiFeign;


    /**
     * 微信授权
     *
     * @param request
     * @return
     */
    @GetMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String memberNo = request.getParameter("memberNo");
        String customerNo = request.getParameter("customerNo");
        log.info("code={},memberNo={},customerNo={}", code, memberNo, customerNo);
        WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(appId, appSecret, code);
        log.info("weixinOauth2Token={}", JSON.toJSONString(weixinOauth2Token));
        if (weixinOauth2Token != null && !StringUtils.isEmpty(weixinOauth2Token.getOpenId())) {
            ApiResult apiResult = apiFeign.updateOpenid(customerNo, weixinOauth2Token.getOpenId());
            if (!apiResult.hasSuccess()) {
                log.error("apiResult={}", JSON.toJSONString(apiResult));
            }
        }
        response.sendRedirect(new StringBuffer(hengjiaH5Url).append("?memberNo=").append(memberNo).append("&indexRouter=wechatPay").toString());
    }
}
