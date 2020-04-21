package com.baibei.hengjia.gateway.filters.pre;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.baibei.hengjia.gateway.utils.FilterUtil;
import com.baibei.hengjia.gateway.utils.SignUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/29 9:48 PM
 * @description: 数据签名过滤器
 */
@Slf4j
public class SignatureFilter extends ZuulFilter {

    // 验签秘钥对
    @Value("${device.key.secret}")
    private String deviceKeySecret;

    // 是否启用验签
    @Value("${verifySign}")
    private String verifySign;

    // 默认签名
    @Value("${defaultSign}")
    private String defaultSign;

    // 忽略验签的URL
    @Value("${ignoreSignUrl}")
    private String ignoreSignUrl;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean shouldFilter = (Boolean) ctx.get("shouldFilter");
        if (shouldFilter != null) {
            shouldFilter = shouldFilter && !isMatch(ctx.getRequest().getRequestURI());
        } else {
            shouldFilter = !isMatch(ctx.getRequest().getRequestURI());
        }
        return shouldFilter;
    }


    @Override
    public Object run() {
        log.info("SignatureFilter is running");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest servletRequest = ctx.getRequest();
        String uri = servletRequest.getRequestURI();
        JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
        try {
            String platform = jsonRequest.getPlatform();
            JSONObject jsonObject = JSON.parseObject(deviceKeySecret);
            JSONObject keyAndSecret = jsonObject.getJSONObject(platform);
            if (keyAndSecret == null) {
                FilterUtil.terminateFilter(ApiResult.badParam("设备平台错误"));
                return null;
            }
            String appKey = keyAndSecret.getString("appKey");
            if (StringUtils.isEmpty(appKey) || !appKey.equals(jsonRequest.getAppKey())) {
                FilterUtil.terminateFilter(ApiResult.badParam("appKey错误"));
            }
            String appSecret = keyAndSecret.getString("appSecret");
            String originalSign = jsonRequest.getSign();
            if (StringUtils.isEmpty(appSecret)) {
                FilterUtil.terminateFilter(ApiResult.badParam("appKey错误"));
                return null;
            }
            boolean signMatched = this.verifySign(uri, jsonRequest, appSecret, originalSign);
            if (!signMatched) {
                FilterUtil.terminateFilter(ApiResult.badParam("签名错误"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FilterUtil.terminateFilter(ApiResult.error("验签失败"));
            return null;
        }
        return null;
    }

    /**
     * 是否匹配路由规则
     *
     * @param url
     * @return
     */
    private boolean isMatch(String url) {
        if (ignoreSignUrl != null && !ignoreSignUrl.trim().equals("")) {
            return PatternMatchUtils.simpleMatch(ignoreSignUrl.split(","), url);
        }
        return false;
    }

    /**
     * 验证数据签名
     *
     * @param uri          请求URI
     * @param jsonRequest  请求体
     * @param appSecret    秘钥
     * @param originalSign 请求的签名内容
     */
    private boolean verifySign(String uri, JsonRequest jsonRequest, String appSecret,
                               String originalSign) {
        if (!StringUtils.isEmpty(defaultSign) && jsonRequest.getSign().equals(defaultSign)) {
            log.info("use default sign...");
            return true;
        }
        // jsonRequest转为map
        Map<String, Object> paramMap = SignUtil.bean2Map(jsonRequest);
        paramMap.remove("sign");
        // 参数排序
        Map<String, Object> sortedMap = SignUtil.sort(paramMap);
        // 拼接参数：key1Value1key2Value2
        String urlParams = SignUtil.groupStringParam(sortedMap);
        // 拼接URI和AppSecret：stringURI + stringParams + AppSecret
        StringBuffer sb = new StringBuffer();
        sb.append(uri).append(urlParams).append(appSecret);
        String signContent = sb.toString();
        // 签名
        String mySign = SignUtil.encryptHMAC(appSecret, signContent);
        boolean verifyResult = mySign.equals(originalSign);
        log.info("requestId : {},sign content : {},sign result:{},verify result : {}",
                jsonRequest.getRequestId(), signContent, mySign, verifyResult);
        return verifyResult || isSignSwitchOff(jsonRequest);
    }

    /**
     * 是否启用验签
     *
     * @param jsonRequest
     * @return
     */
    private boolean isSignSwitchOff(JsonRequest jsonRequest) {
        if (!Boolean.parseBoolean(verifySign)) {
            log.warn("requestId : {},sign verify switch off ,so auto let request go!", jsonRequest.getRequestId());
        }
        return !Boolean.parseBoolean(verifySign);
    }
}
