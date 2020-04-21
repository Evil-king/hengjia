package com.baibei.hengjia.gateway.filters.post;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.baibei.hengjia.gateway.dto.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/29 10:23 PM
 * @description: 响应过滤器
 */
@Slf4j
public class ResponseWrapperFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("ResponseWrapperFilter is running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
        String responseBody = ctx.getResponseBody();
        JsonResponse jsonResponse = null;
        if (StringUtils.isEmpty(responseBody)) {
            jsonResponse = this.getJsonFromResponse();
        } else {
            try {
                jsonResponse = JSON.parseObject(responseBody, JsonResponse.class);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (jsonRequest != null && jsonResponse != null) {
            jsonResponse.setRequestId(jsonRequest.getRequestId());
            ctx.set("jsonResponse", jsonResponse);
        }
        responseBody = JSON.toJSONString(jsonResponse);
        ctx.setResponseBody(responseBody);
        return null;
    }

    private JsonResponse getJsonFromResponse() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpResponse = ctx.getResponse();
        if (httpResponse.getStatus() < HttpServletResponse.SC_BAD_REQUEST && ctx.getResponseDataStream() != null) {
            try {
                return objectMapper.readValue(ctx.getResponseDataStream(), JsonResponse.class);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
