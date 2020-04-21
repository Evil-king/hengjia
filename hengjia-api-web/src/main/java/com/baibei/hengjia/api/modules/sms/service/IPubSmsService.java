package com.baibei.hengjia.api.modules.sms.service;

import com.baibei.hengjia.api.modules.sms.model.PubSms;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;


/**
 * @author: wenqing
 * @date: 2019/06/03 15:50:13
 * @description: PubSms服务接口
 */
public interface IPubSmsService extends Service<PubSms> {

    /**
     * 发送短信
     *
     * @param phone
     * @param message
     * @return
     */
    ApiResult getSms(String phone, String message);

    /**
     * 验证短信码是否存在
     *
     * @param phone
     * @param code
     * @return
     */
    ApiResult validateCode(String phone, String code);

    /**
     * 普通的发送短信
     *
     * @param mobile
     * @param msg
     * @return
     */
    boolean sendNormalSms(String mobile, String msg);
}
