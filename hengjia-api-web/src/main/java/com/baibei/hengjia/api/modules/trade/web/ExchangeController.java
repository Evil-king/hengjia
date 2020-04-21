package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.biz.ExchangeBiz;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/14 16:56
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ExchangeController {
    @Autowired
    private ExchangeBiz exchangeBiz;

    /**
     * 0001置换0002
     *
     * @return
     */
    @GetMapping("/exchange")
    public ApiResult exchange() {
        exchangeBiz.exchange();
        return ApiResult.success();
    }
}