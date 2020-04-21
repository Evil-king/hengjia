package com.baibei.hengjia.api.modules.trade.web;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MatchVo;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.api.modules.trade.service.IMatchLogService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Classname ApiDeliveryMatchController
 * @Description 提货配票
 * @Date 2019/6/10 11:00
 * @Created by Longer
 */
@Slf4j
@RestController
@RequestMapping("/api/deliveryMatch")
public class ApiDeliveryMatchController {
    @Autowired
    private IMatchLogService matchLogService;
    @Autowired
    private IMatchConfigService matchConfigService;


    /**
     * 提货配票
     * @return
     */
    @PostMapping("/deliveryMatchs")
    public ApiResult deliveryMatch(HttpServletRequest request){
        log.info("开始执行休市提货配票逻辑...");
        /*String ipAddr = getIpAddr(request);
        log.info("请求ip...{}",ipAddr);*/
        ApiResult apiResult;
        try {
            matchLogService.deliveryMatch();
            apiResult = ApiResult.success();
        } catch (ServiceException s){
            log.info("提货配票业务异常，关闭开关...");
            matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.DELIVERYMATCH);
            apiResult = ApiResult.error(s.getMessage());
        }
        catch (Exception e) {
            log.info("提货配票系统异常，关闭开关...");
            matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.DELIVERYMATCH);
            e.printStackTrace();
            apiResult = ApiResult.error(e.getMessage());
        }
        log.info("结束执行休市提货配票逻辑...apiResult={}",JSON.toJSONString(apiResult));
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
            if( ip.indexOf(",")!=-1 ){
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
