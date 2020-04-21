package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/14 9:45 AM
 * @description: 设置开休市状态至Redis
 */
@Slf4j
@RestController
@RequestMapping("/api/tradeDay")
public class ApiTradeDayController {
    @Autowired
    private ITradeDayService tradeDayService;



    /**
     * 设置开休市状态至Redis
     *
     * @return
     */
    @GetMapping("/setTradeDay")
    public ApiResult setTradeDay(HttpServletRequest request) {
        log.info("设置开休市状态，来自IP：{}", getIpAddress(request));
        log.info("setTradeDay is running...");
        boolean flag = tradeDayService.setTradeDayToRedis();
        if (flag) {
            return ApiResult.success("开市中...");
        } else {
            return ApiResult.success("休市中...");
        }
    }

    public final static String getIpAddress(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (null == ip || "127.0.0.1".equals(ip)) {
            // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = (String) ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        }
        return ip;
    }
}
