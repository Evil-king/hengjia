package com.baibei.hengjia.api.modules.weixin;

import com.baibei.hengjia.api.modules.weixin.service.WeixinService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/17 6:27 PM
 * @description:
 */
@RestController
@RequestMapping("/api/weixin")
public class WeixinInfoController {
    @Autowired
    private WeixinService weixinService;

    /**
     * 更新微信授权信息
     *
     * @return
     */
    @GetMapping("/updateWxInfo")
    public ApiResult updateWxInfo() {
        weixinService.updateWxInfo();
        return ApiResult.success();
    }
}
