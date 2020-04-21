package com.baibei.hengjia.api.modules.cash.base;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.component.BankMessageSplice;
import com.baibei.hengjia.api.modules.cash.component.GetSentServer;
import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.PABAnswerCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 平安银行报文的发送和拼接的抽象类
 * @param <T>
 * @param <V>
 */
public abstract class AbstractCashFunction<T extends BaseRequest, V extends BaseResponse> implements ICashFunctionService<T, V> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${cash.supAcctId}")
    protected String supAcctId; //资金汇总账号

    @Autowired
    protected GetSentServer getServer;

    @Value("${cash.socketIP}")
    protected String socketIP;

    @Value("${cash.socketPort}")
    protected String socketPort;

    @Value("${cash.Qydm}")
    protected String Qydm;

    // 报文拼接
    @Autowired
    protected BankMessageSplice interfaceMessage;

    // 发送请求
    @Autowired
    protected GetSentServer getGetServer;

    // 报文解析
    @Autowired
    protected BankBackMessageAnalysis bankBackMessageAnalysis;


    /**
     * 请求银行,然后把结果转换对应的值
     * 不是发起请求，可以返回默认值 ApiResult.success();
     *
     * @param request
     * @return
     */
    public Map<String, String> doRequest(T request, Map<String, String> parmaKeyDict) {
        return parmaKeyDict;
    }

    public void afterRequest(V response, Map<String, String> requestResult) {

    }

    /**
     * 进行逻辑处理,如果不是接受请求,可以返回默认值
     *
     * @param request
     * @return
     */
    public Map<String, String> doResponse(T request, Map<String, String> parmaKeyDict) {
        return parmaKeyDict;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResult<V> request(T request) {
        if (logger.isInfoEnabled()) {
            logger.info("execute {} Function by started,request={}", this.getType(), JSONObject.toJSONString(request));
        }
        ApiResult<V> response;
        Map<String, String> parmaKeyDic = new HashMap<>();
        parmaKeyDic.put("TranFunc", Integer.toString(getType().index)); // 交易码
        parmaKeyDic.put("ServiceType", "01");// 01请求,02应答
        parmaKeyDic.put("Qydm", Qydm); // 平台代码
        long start = System.currentTimeMillis();
        byte[] messageBody;
        try {
            Map<String, String> requestResult = doRequest(request, parmaKeyDic);
            String message = this.responseResult(parmaKeyDic);
            messageBody = message.getBytes("gbk");
            String headMessage = interfaceMessage.getYinShangJieSuanTongHeadMessage(messageBody.length, parmaKeyDic);
            String tranMessage = headMessage + message;
            logger.info("当前发送的报文为{}", tranMessage);
            String tranBackMessage = getGetServer.sendAndGetMessage(socketIP, socketPort, tranMessage);
            logger.info("当前银行返回的报文为{}",tranBackMessage);
            Map<String, String> result = bankBackMessageAnalysis.parsingTranMessageString(tranBackMessage);
            logger.info("发送状态为{}", result.get("RspCode"));
            if (result.get("RspCode").equals(PABAnswerCodeEnum.SUCCESS.getCode())) {
                String backBodyMessage = result.get("backBodyMessages");
                logger.info("银行的返回的报文体为:{}", backBodyMessage);
                Map<String, String> resultParam = spiltMessage(result);
                V res = toEntityByHashMapResponse(resultParam);
                afterRequest(res, requestResult);
                response = ApiResult.success(res);
            } else {
                response = ApiResult.error(result.get("RspMsg"));
                afterRequest(null,requestResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("当前{}请求异常:[{}]", getType().getName(), ex.getMessage());
            response = ApiResult.error("当前操作" + getType().getName() + " 失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        long end = System.currentTimeMillis();
        long eliminateTime = end - start;
        if (logger.isInfoEnabled()) {
            logger.info("execute {} Function, used time={},request={},response={}", this.getType(),
                    eliminateTime, JSONObject.toJSONString(request), JSONObject.toJSONString(response));
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String response(Map<String, String> backMessages) {
        String backBodyMessages = backMessages.get("backBodyMessages");
        logger.info("当前的银行的请求消息体为:[{}]", backBodyMessages);
        // stop1 解析消息体
        Map<String, String> parmaKeyDict = new HashMap<>();
        parmaKeyDict.put("TranFunc", Integer.toString(getType().index)); //交易码
        parmaKeyDict.put("ServiceType", "02");//01请求,02应答
        parmaKeyDict.put("Qydm", Qydm);
        parmaKeyDict.put("externalNo", backMessages.get("externalNo")); // 获取外部交易流水号
        // stop 2 根据解析的结果,进行处理
        T responseEntity = toEntityByHashMapRequest(spiltMessage(backMessages));
        logger.info("银行消息解析结果为:[{}]", JSONObject.toJSONString(responseEntity));
        // stop 3 进行验证@Valid
        Map<String, String> validResult = valid(responseEntity, parmaKeyDict);
        if (validResult.get("RspCode").equals(PABAnswerCodeEnum.SUCCESS.getCode())) {
            try {
                doResponse(responseEntity, parmaKeyDict);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("系统异常：当前{}的异常信息为:[{}]", getType().getName(), ex.getMessage());
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "系统错误");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        // stop 4 执行请求
        String tranBackMessage = null;
        byte[] messages;
        String bodyMessage = responseResult(parmaKeyDict);
        try {
            messages = bodyMessage.getBytes("gbk");
            String headMessage = interfaceMessage.getYinShangJieSuanTongHeadMessage(messages.length, parmaKeyDict);
            tranBackMessage = headMessage + bodyMessage;
        } catch (UnsupportedEncodingException e) {
            logger.error("拼接报文体失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return tranBackMessage;
    }

    /**
     * 解析报文装换成map对象
     *
     * @param retKeyDict
     * @return
     */
    public abstract Map<String, String> spiltMessage(Map<String, String> retKeyDict);

    /**
     * 参数验证
     *
     * @param request
     * @return
     */
    public Map<String, String> valid(T request, Map<String, String> parmaKeyDict) {
        parmaKeyDict.put("RspCode", PABAnswerCodeEnum.SUCCESS.getCode());
        return parmaKeyDict;
    }


    /**
     * 拼接返回报文体
     *
     * @param map
     * @return
     */
    public abstract String responseResult(Map<String, String> map);


    /**
     * 根据HashMap 转换成相应的对象
     *
     * @param params
     * @return
     */
    protected T toEntityByHashMapRequest(Map<String, String> params) {
        return null;
    }

    /**
     * 根据HashMao 转换为 返回对象
     *
     * @param params
     * @return
     */
    protected V toEntityByHashMapResponse(Map<String, String> params) {
        return null;
    }
}
