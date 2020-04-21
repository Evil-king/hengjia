package com.baibei.hengjia.api.modules.sms;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hwq
 * @date 2019/05/29
 */
@Slf4j
public abstract class SmsTemplate {

    /**
     * 短信实体类
     * @return
     */
    public boolean doProcess(String phone,String message) {
        boolean flag = false;
        try {
            // 发送请求到渠道不同的渠道商
            boolean messageResult = doSend(phone,message);
            log.info("短信请求结果=>"+ messageResult);
            // 获取渠道返回的结果
             flag = parseResultToBoolean(messageResult);
            // 写入到DB中
            if(flag){
                if(writeToDb(phone,message) > 0){
                    return flag = true;
                }
            }
        } catch (Exception e) {
            log.error("e{}",e);
        }
        return flag;
    }


    /**
     * 包装数据发送到渠道
     * @param phone
     * @param message
     * @return
     */
    protected abstract Boolean doSend(String phone,String message);

    /**
     * 统一处理结果
     * @param messageResult
     * @return
     */
    protected abstract boolean parseResultToBoolean(Boolean messageResult);

    /**
     * 保存短信相关的信息
     * @param phone
     * @param message
     * @return
     */
    protected abstract int writeToDb(String phone,String message);

}
