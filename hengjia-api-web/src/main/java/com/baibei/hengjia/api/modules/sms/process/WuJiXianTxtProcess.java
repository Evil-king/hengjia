package com.baibei.hengjia.api.modules.sms.process;

import com.baibei.hengjia.api.modules.sms.SmsTemplate;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.sms.core.wuji.WuJiUtils;
import com.baibei.hengjia.api.modules.sms.dao.PubSmsMapper;
import com.baibei.hengjia.api.modules.sms.model.PubSms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author hwq
 * @date 2019/05/29
 */
@Slf4j
@Component
public class WuJiXianTxtProcess extends SmsTemplate {

    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private PubSmsMapper smsMapper;

    @Override
    protected Boolean doSend(String phone, String message) {
        Boolean flag = Boolean.FALSE;
        try {
            flag = WuJiUtils.send(phone, propertiesVal.getSign() + message, propertiesVal.getWujiUrl(),
                    propertiesVal.getUserName(), propertiesVal.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    protected boolean parseResultToBoolean(Boolean messageResult) {
        return messageResult;
    }

    @Override
    protected int writeToDb(String phone, String message) {
        PubSms sms = new PubSms();
        sms.setCode(message);
        sms.setMobile(phone);
        sms.setFlag((byte) 1);
        sms.setModifyTime(new Date());
        sms.setCreateTime(new Date());
        int num = smsMapper.insert(sms);
        return num;
    }
}
