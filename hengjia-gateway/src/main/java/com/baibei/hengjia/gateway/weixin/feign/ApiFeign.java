package com.baibei.hengjia.gateway.weixin.feign;

import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/10 8:17 PM
 * @description: API接口调用
 */
@FeignClient(name = "apiFeign", url = "${api.url}")
public interface ApiFeign {


    /**
     * 更新客户openid
     *
     * @return
     */
    @GetMapping("/auth/api/customer/updateOpenid")
    ApiResult updateOpenid(@RequestParam(name = "customerNo") String customerNo, @RequestParam(name = "openId") String openId);
}
