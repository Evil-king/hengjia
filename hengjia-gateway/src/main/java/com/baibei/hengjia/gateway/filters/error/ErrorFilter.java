package com.baibei.hengjia.gateway.filters.error;

import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.gateway.utils.FilterUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/28 20:54 PM
 * @description:
 */
@Slf4j
public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("ErrorFilter running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        log.error("ErrorFilter", throwable);
        ctx.remove("throwable");//移除异常
        FilterUtil.terminateFilter(ApiResult.error("网关转发异常"));
        return null;
    }
}
