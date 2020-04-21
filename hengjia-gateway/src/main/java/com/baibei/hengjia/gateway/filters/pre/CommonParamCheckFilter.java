package com.baibei.hengjia.gateway.filters.pre;

import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.baibei.hengjia.gateway.utils.FilterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/22 6:05 PM
 * @description: 公共参数检查过滤器
 */
@Slf4j
public class CommonParamCheckFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("CommonParamCheckFilter is running");
        RequestContext ctx = RequestContext.getCurrentContext();
        //记录请求开始时间
        ctx.set("startTime", System.currentTimeMillis());
        HttpServletRequest servletRequest = ctx.getRequest();
        HttpMethod httpMethod = HttpMethod.valueOf(servletRequest.getMethod());
        // 现网关只支持post请求
        if (!httpMethod.equals(HttpMethod.POST)) {
            FilterUtil.terminateFilter(ApiResult.error("不支持的请求方式"));
            return null;
        }
        try {
            // 转换获取请求参数
            JsonRequest jsonRequest = parseBodyParams(servletRequest);
            // 检查公共参数
            ApiResult<String> apiResult = checkEmptyParam(jsonRequest);
            if (!apiResult.hasSuccess()) {
                FilterUtil.terminateFilter(apiResult);
                return null;
            }
            HttpServletResponse servletResponse = ctx.getResponse();
            servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // 获取并设置请求IP
            String ip = getIpAddr(servletRequest);
            ctx.set("ip",ip);
            // 设置请求上下文
            ctx.set("jsonRequest", jsonRequest);
            ctx.set("shouldFilter", true);
        } catch (Exception e) {
            e.printStackTrace();
            FilterUtil.terminateFilter(ApiResult.error("网关异常"));
            return null;
        }
        return null;
    }


    /**
     * 将Body数据转换为JsonRequest对象
     *
     * @param servletRequest
     * @return
     */
    private JsonRequest parseBodyParams(HttpServletRequest servletRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonRequest jsonRequest = new JsonRequest();
        try {
            if (servletRequest.getContentLengthLong() > 0) {
                ServletInputStream inputStream = servletRequest.getInputStream();
                jsonRequest = objectMapper.readValue(inputStream, JsonRequest.class);
            }
        } catch (IOException e) {
            log.error("IOException : ", e);
        }
        return jsonRequest;
    }

    /**
     * 检查公共必要参数是否为空
     *
     * @param jsonRequest
     * @return
     */
    private ApiResult<String> checkEmptyParam(JsonRequest jsonRequest) {
        ApiResult apiResult = ApiResult.success();
        if (StringUtils.isEmpty(jsonRequest.getRequestId())) {
            return ApiResult.badParam("requestId不能为空");
        }
        if (StringUtils.isEmpty(jsonRequest.getPlatform())) {
            return ApiResult.badParam("platform不能为空");
        }
        if (StringUtils.isEmpty(jsonRequest.getDeviceId())) {
            return ApiResult.badParam("deviceId不能为空");
        }
        if (StringUtils.isEmpty(jsonRequest.getAppKey())) {
            return ApiResult.badParam("appKey不能为空");
        }
        if (StringUtils.isEmpty(jsonRequest.getTimestamp())) {
            return ApiResult.badParam("timestamp不能为空");
        }
        if (StringUtils.isEmpty(jsonRequest.getVersion())) {
            return ApiResult.badParam("version不能为空");
        }
        String platform = jsonRequest.getPlatform();
        if (!Constants.Platform.Android.equals(platform) && !Constants.Platform.IOS.equals(platform) && !Constants.Platform.H5.equals(platform)) {
            return ApiResult.badParam("platform错误");
        }
        return apiResult;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }
}
