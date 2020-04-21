package com.baibei.hengjia.common.tool.constants;

import java.text.MessageFormat;

/**
 * @author: 会跳舞的机器人
 * @date: 17/12/29 上午10:34
 * @description: Redis key定义,前缀为服务名,注释写明数据结构类型
 */
public class RedisConstant {


    public static void main(String[] args) {
        String key = MessageFormat.format(USER_CUSTOMERINFO, "20190523");
        System.out.println(key);
    }

    /**
     * 客户信息缓存,{0}为customer_no
     * 数据类型:hash
     */
    public static final String USER_CUSTOMERINFO = "USERSERVICE:CUSTOMERINFO:{0}";
    /**
     * 期初资金缓存，{0}为customer_no
     * 数据类型:hash
     */
    public static final String ACCOUNT_INIT_FUND = "ACCOUNTSERVICE:ACCOUNT_INIT_FUND:{0}";
    /**
     * 用户登录失败次数，{0}为customer_no
     * 数据类型：hash
     */
    public static final String USER_ERROR_COUNT = "USERSERVICE:ERROR_COUNT:{0}";
    /**
     * 用户token存储，{0}为customer_no
     * 数据类型：hash
     */
    public static final String PREFIX_USER_TOKEN = "USERSERVICE:PREFIX_USER_TOKEN:{0}";
    /**
     *
     */
    public static final String PREFIX_TOKEN_CODE = "USERSERVICE:PREFIX_TOKEN_CODE:{0}";
    /**
     * 挂买时的可提资金
     */
    public static final String ACCOUNT_WITHDRAW_AMOUT = "ACCOUNTSERVICE:ACCOUNT_WITHDRAW_AMOUT:{0}";
    /**
     * 商品挂牌买入价格对应的挂牌数量,{0}为商品交易编码
     * 数据类型:hash,field为挂牌报价,value为挂牌总数量
     */
    public static final String TRADE_ENTRUST_PRICEANDCOUNT_BUY = "TRADESERVICE:ENTRUST:PRICEANDCOUNT:BUY:{0}";

    /**
     * 商品挂牌卖出价格对应的挂牌数量,{0}为商品交易编码
     * 数据类型:hash,field为挂牌报价,value为挂牌总数量
     */
    public static final String TRADE_ENTRUST_PRICEANDCOUNT_SELL = "TRADESERVICE:ENTRUST:PRICEANDCOUNT:SELL:{0}";
    /**
     * 商品挂牌买入报价对应的委托单,{0}为商品交易编码
     * 数据类型:sorted set,score为报价,member为委托单单号
     */
    public static final String TRADE_ENTRUST_PRICE_BUY = "TRADESERVICE:ENTRUST:PRICE:BUY:{0}";
    /**
     * 商品挂牌卖出报价对应的委托单,{0}为商品交易编码
     * 数据类型:sorted set,score为报价,member为委托单单号
     */
    public static final String TRADE_ENTRUST_PRICE_SELL = "TRADESERVICE:ENTRUST:PRICE:SELL:{0}";

    /**
     * 商品挂牌买入报价对应的委托单,{0}为商品交易编码,{1}为报价
     * 数据类型:sorted set,score为挂牌委托时间,member为委托单单号
     */
    public static final String TRADE_ENTRUST_TIME_BUY = "TRADESERVICE:ENTRUST:TIME:BUY:{0}:{1}";

    /**
     * 商品挂牌卖出报价对应的委托单,{0}为商品交易编码,{1}为报价
     * 数据类型:sorted set,score为挂牌委托时间,member为委托单单号
     */
    public static final String TRADE_ENTRUST_TIME_SELL = "TRADESERVICE:ENTRUST:TIME:SELL:{0}:{1}";

    /**
     * 委托单信息,{0}为委托单单号
     * 数据类型:hash
     */
    public static final String TRADE_ENTRUSTINFO = "TRADESERVICE:ENTRUSTINFO:{0}";

    /**
     * 交易日,数据类型:String,value=1表示是交易日,value=0表示非交易日
     */
    public static final String TRADE_TRADE_DAY = "TRADESERVICE:TRADEDAY";

    /**
     * 上市商品缓存,{0}为商品交易编码
     * 数据结构:hash
     */
    public static final String PRODUCT_TRADE_NO = "PRODUCTSERVICE:TRADENO:{0}";

    /**
     * 短信验证码时长,{0}为短信码
     */
    public static final String SMS_USER_PHONE = "PUBSMSSERVICE:USERPHONE:{0}";

    /**
     * 微信access_token/jsapi_ticket数据,{0}为appid
     * 数据结构:hash
     */
    public static final String WX_INFO = "WXINFO:{0}";

    /**
     * 处理银行出入金文件，并入库通知渠道
     */
    public static final String SET_BANK_ORDER_TOPIC = "bank_order";
    /**
     * 出入金对账通知渠道
     */
    public static final String SET_WITHDRAW_DEPOSIT_TOPIC = "withdraw_deposit";

    /**
     * 出入金对账通知队列
     */
    public static final String SET_WITHDRAW_DEPOSIT_LIST = "withdraw_deposit_list";

    /**
     * 清算数据准备通知渠道
     */
    public static final String SET_CLEAN_PRE_TOPIC = "clean_pre";

    /**
     * 清算数据准备消息队列
     */
    public static final String SET_CLEAN_PRE_LIST = "clean_pre_list";

    /**
     * 调用银行开始清算通知
     */
    public static final String SET_CLEAN_TOPIC = "send_to_bank";

    /**
     * 调用银行开始清算消息队列
     */
    public static final String SET_CLEAN_LIST = "send_to_bank_list";

    /**
     * 获取银行文件通知
     */
    public static final String SET_BANK_FILE = "bank_file";

    /**
     * 获取银行文件通知消息队列
     */
    public static final String SET_BANK_FILE_LIST = "bank_file_list";

    /**
     * 买入超过100手提示
     */
    public static final String TRADE_BUY_ONEHUNDRED = "TRADESERVICE:ONEHUNDRED:{0}:{1}";

    /**
     * 单一账户当天累计兑换量。{0}=用户编码，{1}=商品交易编码
     */
    public static final String EXCHANGE_DAY_NUM = "EXCHANGE:DAY:{0}:{1}";

    /**
     * 买入配货通知渠道
     */
    public static final String MATCH_BUYMATCH_TOPIC = "match_buymatch";
    /**
     * 买入配货通知队列
     */
    public static final String MATCH_BUYMATCH_LIST = "match_buymatch_list";

    /**
     * 恒价后台登录用户端的token,{0}用户名
     */
    public static final String PREFIX_HENGJIA_ADMIN_USER_TOKEN = "USERSERVICE:PREFIX_HENGJIA_ADMIN_USER_TOKEN:{0}";

    /**
     * 配货失败短信通知key
     */
    public static final String SMS_MATCH_NOTIFY = "PUBSMSSERVICE:MATCHFAIL:NOTIFY:{0}";

    /**
     * 每天只能卖一手零售一手折扣限制
     */
    public static final String TRADE_SELL_LIMIT = "TRADESERVICE:SELL:{0}:{1}";
}
