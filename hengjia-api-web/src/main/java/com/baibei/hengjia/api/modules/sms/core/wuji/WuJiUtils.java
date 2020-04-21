package com.baibei.hengjia.api.modules.sms.core.wuji;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 会跳舞的机器人
 * @date: 2018/9/27 14:23
 * @description:
 */
public class WuJiUtils {
    private static final Logger logger = LoggerFactory.getLogger(WuJiUtils.class);

    /**
     * 发送短信
     *
     * @param mobile
     * @param content
     * @param smsUrl
     * @param account
     * @param password
     * @return
     */
    public static boolean send(String mobile, String content, String smsUrl, String account, String password) {
        return send(mobile, content, smsUrl, account, password, null);
    }


    /**
     * @param mobile
     * @param content
     * @param smsUri
     * @param smsAccount
     * @param smsPasswd
     * @param ac
     * @return
     */
    public static boolean send(String mobile, String content, String smsUri, String smsAccount, String smsPasswd,
                               String ac) {
        boolean r = false;
        try {
            logger.info("smsUri={},req={}", smsUri, content);
            //{"result":0,"msgid":"2300529104325397100","suc":1,"fail":0,"ts":"20180529104325"}
            String res = HttpSender.send(smsUri, smsAccount, smsPasswd, mobile, content, false, "", "");

            logger.info("res={}", res);

            JSONObject data = JSON.parseObject(res);
            String result = data.get("result").toString();

            if (res != null && "0".equals(result)) {
                logger.info("{}短信发送成功：{}-{}。", mobile, res, content);
                return true;
            } else {
                logger.info("{}短信发送失败：{}-{}。", mobile, res, content);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}短信发送出现异常：{}", mobile, e.getMessage());
        }
        return false;
    }
}
