package com.baibei.hengjia.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/28 21:20 PM
 * @description:
 */
@Slf4j
public class FilterUtil {

    /**
     *
     * @param apiResult
     */
    public static void terminateFilter(ApiResult apiResult) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        ctx.getResponse().setCharacterEncoding("UTF-8");
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
        ctx.setResponseBody(JSON.toJSONString(apiResult));
        ctx.set("shouldFilter", false);
    }

}
