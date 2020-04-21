package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.service.IAsyncService;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.sms.util.SmsUtil;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Classname AsyncServiceImpl
 * @Description 异步类
 * @Date 2019/7/2 11:01
 * @Created by Longer
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@Async
public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    private IPubSmsService pubSmsService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    public PropertiesVal propertiesVal;

    @Override
    public void buyMatchFailSms(String customerNo) {
        //查询用户的手机号码
        CustomerVo customerVo = customerService.findUserByCustomerNo(customerNo);
        //手机号码
        String phone = customerVo.getMobile();
        //发送短信
        String message = smsUtil.operatorSms("2", "3", customerVo.getUsername(), phone);
        ApiResult smsResult = pubSmsService.getSms(phone, message);
        log.info("发送短信通知配货失败结果==》用户手机：{}，结果：{}",phone,smsResult.getCode());

    }
}
