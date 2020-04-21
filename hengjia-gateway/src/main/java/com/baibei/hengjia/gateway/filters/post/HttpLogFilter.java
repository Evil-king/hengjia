package com.baibei.hengjia.gateway.filters.post;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/29 11:09 PM
 * @description: 请求日志记录过滤器
 */
@Slf4j
public class HttpLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("HttpLogFilter is running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest servletRequest = ctx.getRequest();
        HttpMethod httpMethod = HttpMethod.valueOf(servletRequest.getMethod());
        if (!(httpMethod.equals(HttpMethod.GET) || httpMethod.equals(HttpMethod.POST)
                || httpMethod.equals(HttpMethod.PUT) || httpMethod.equals(HttpMethod.DELETE))) {
            log.warn("Illegal requests received :  {} - {}", httpMethod.name(), servletRequest.getRequestURL());
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(400);
            ctx.set("shouldFilter", false);
            return null;
        }
        String responseBody = ctx.getResponseBody();
        if (responseBody != null && responseBody.length() > 200) {
            // 响应的日志截取前200个字符
            responseBody = responseBody.substring(0, 200) + "...";
        }
        // 当请求没有按接口协议发送的时候，存在会NULL的情况
        JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
        if (jsonRequest != null) {
            long startTime = (long) ctx.get("startTime");
            log.info("requestId={} take={} ms - {} - {} - Params= {} - Response= {}", jsonRequest.getRequestId(), (System.currentTimeMillis() - startTime), httpMethod.name(), servletRequest.getRequestURL(), JSON.toJSONString(jsonRequest), responseBody);
            return null;
        }
        return null;
    }

}
