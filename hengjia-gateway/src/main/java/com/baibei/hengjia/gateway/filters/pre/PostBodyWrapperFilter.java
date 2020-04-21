package com.baibei.hengjia.gateway.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: 会跳舞的机器人
 * @date: 17/12/28 上午10:19
 * @description: 转发请求时只需要传网关接口data以及分页参数过滤器
 */
@Slf4j
public class PostBodyWrapperFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpMethod httpMethod = HttpMethod.valueOf(ctx.getRequest().getMethod());
        boolean isPost = (HttpMethod.POST == httpMethod);
        Boolean shouldFilter = (Boolean) ctx.get("shouldFilter");
        if (shouldFilter != null) {
            shouldFilter = shouldFilter && isPost;
        } else {
            shouldFilter = isPost;
        }
        return shouldFilter;
    }


    @Override
    public Object run() {
        log.info("PostBodyWrapperFilter is running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletRequestWrapper wrapper = new PostBodyRequestWrapper(request);
        ctx.setRequest(wrapper);
        ctx.getZuulRequestHeaders().put(HttpHeaders.CONTENT_TYPE, wrapper.getContentType());
        return null;
    }
}
