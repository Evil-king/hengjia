package com.baibei.hengjia.api.modules.match.service;

import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;

import java.util.List;

/**
 * @Classname IAsyncService
 * @Description 异步类
 * @Date 2019/10/16 15:09
 * @Created by Longer
 */
public interface IAsyncService {
    /**
     * 配货失败，发短信通知用户。
     */
    void buyMatchFailSms(String customerNo);
}
