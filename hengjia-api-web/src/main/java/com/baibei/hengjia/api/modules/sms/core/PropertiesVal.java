package com.baibei.hengjia.api.modules.sms.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/05/29
 */
@Data
@Component
public class PropertiesVal {
    //start--短信相关验证
    @Value("${sms.temple}")
    private String smsTemple;
    @Value("${sms.default}")
    private String smsDefaultTmple;
    @Value("${sms.time.out}")
    private long smsTimeOut;
    @Value("${sms.max.count}")
    private int smsMaxCount;
    @Value("${sms.max.time}")
    private int smsMaxTime;
    //end

    //start--微网通联
//    @Value("${wwtl.sms.url_submit}")
//    private String url_submit;
//
//    @Value("${wwtl.sms.username}")
//    private String username;
//
//    @Value("${wwtl.sms.password}")
//    private String password;
//
//    @Value("${wwtl.sms.prudoct}")
//    private String productNum;
//
//    @Value("${wwtl.sms.sign}")
//    private String sign;
    //end

    //start--无极限短信
    @Value("${wuji.sms.url}")
    private String wujiUrl;
    @Value("${wuji.sms.username}")
    private String userName;
    @Value("${wuji.sms.password}")
    private String password;
    @Value("${wuji.sms.sign}")
    private String sign;
    //end

    //strt--出金手续费费率、出金最小手续费
    @Value("${withdraw.fee.rate}")
    private String rate;
    @Value("${withdraw.fee}")
    private BigDecimal fee;
    //end

    //start--入金时间、出金时间
    @Value("${withdraw.time}")
    private String withdrawTime;
    @Value("${deposit.time}")
    private String depositTime;
    //end



    //start--平安出金相关信息
    @Value("${cash.socketIP}")
    private String stockIp;
    @Value("${cash.socketPort}")
    private String socketPort;
    @Value("${cash.Qydm}")
    private String Qydm;
    @Value("${cash.tranWebName}")
    private String tranWebName;
    //end

    //start--入金金额上下限
    @Value("${min.deposit}")
    private BigDecimal minDeposit;
    @Value("${max.deposit}")
    private BigDecimal maxDeposit;
    //end

    //start--微信H5支付
    @Value("${weixin.appid}")
    private String appid; //appid
    @Value("${weixin.mch_id}")
    private String mch_id; //商户号
    @Value("${weixin.notify_url}")
    private String notify_url; //回调通知地址
    @Value("${weixin.mch_key}")
    private String mch_key; //微信交易支付密钥
    @Value("${weixin.body}")
    private String body;
    @Value("${weixin.url}")
    private String urlStr;
    //end

    //start--每个用户当天出金额度累计限度(含当前申请的金额在内)
    @Value("${withdrawal.amount}")
    private BigDecimal withdrawalAmount;
    //end

    //start--银行端发起出金提示语
    @Value("抱歉，出金申请不成功，请前往交易网端进行操作。如有疑问，可联系在线客服。")
    private String withdrawStr;
    //end
}
