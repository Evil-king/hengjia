package com.baibei.hengjia.gateway.filters.pre;

import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.baibei.hengjia.gateway.utils.FilterUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/28 20:48 PM
 * @description: 用户登录鉴权过滤器
 */
@Slf4j
public class AuthenticationFilter extends ZuulFilter {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean shouldFilter = (Boolean) ctx.get("shouldFilter");
        String servletPath = ctx.getRequest().getServletPath();
        if (shouldFilter != null) {
            shouldFilter = shouldFilter && servletPath.startsWith("/auth");
        } else {
            shouldFilter = servletPath.startsWith("/auth");
        }
        return shouldFilter;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("AuthenticationFilter is running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
        try {
            String accessToken = jsonRequest.getAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                FilterUtil.terminateFilter(ApiResult.badParam("accessToken为空"));
            }
            // 根据token获取客户编号
            String redisKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, accessToken);
            String customerNo = redisUtil.get(redisKey);
            if (StringUtils.isEmpty(customerNo)) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不存在"));
                return null;
            }
            // 获取客户token缓存信息
            String tokenKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
            Map<String, Object> tokenMap = redisUtil.hgetAll(tokenKey);
            if (CollectionUtils.isEmpty(tokenMap)) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不存在"));
                return null;
            }
            Object accessTokenFromRedis = tokenMap.get("accessToken");
            Object accessTokenExpireTime = tokenMap.get("accessTokenExpireTime");
            if (accessTokenFromRedis == null || accessTokenExpireTime == null) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不匹配"));
                return null;
            }
            long expireTime = Long.parseLong(accessTokenExpireTime.toString());
            long now = new Date().getTime();
            if (expireTime < now) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌已过期"));
                return null;
            }
            ctx.put("customerNo", customerNo);
        } catch (Exception e) {
            e.printStackTrace();
            FilterUtil.terminateFilter(ApiResult.error("网关异常"));
            return null;
        }
        return null;
    }
}
