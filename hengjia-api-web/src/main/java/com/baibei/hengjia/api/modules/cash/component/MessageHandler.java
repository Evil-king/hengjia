package com.baibei.hengjia.api.modules.cash.component;

import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * 监听消息处理
 */
public class MessageHandler implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    Socket mSocket = null;
    boolean mStopped = false;
    BufferedWriter mWriter = null;
    BufferedReader mReader = null;

    private final BankBackMessageAnalysis bankBackMessageAnalysis;

    private final List<ICashFunctionService> cashFunctionServiceList;

    private final IOrderWithdrawService orderWithdrawService;

    private final ISigningRecordService signingRecordService;

    public MessageHandler(Socket pSocket, BankBackMessageAnalysis bankBackMessageAnalysis, List<ICashFunctionService> cashFunctionServiceList,
                          IOrderWithdrawService orderWithdrawService, ISigningRecordService signingRecordService) {
        this.signingRecordService = signingRecordService;
        logger.info("创建新的消息处理器");
        mSocket = pSocket;
        this.cashFunctionServiceList = cashFunctionServiceList;
        this.bankBackMessageAnalysis = bankBackMessageAnalysis;
        this.orderWithdrawService = orderWithdrawService;
    }

    @Override
    public void run() {
        char tagChar[];
        tagChar = new char[1024];
        int len;
        String temp;
        String rev = "";
        String message;
        try {
            while (!mStopped && !mSocket.isClosed()) {
                logger.info("处理消息...");
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "GBk"));
                mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "GBK"));

                if ((len = mReader.read(tagChar)) != -1) {
                    temp = new String(tagChar, 0, len);
                    rev += temp;
                    temp = null;
                }
                message = handleMessage1(rev, mWriter);
                if (StringUtils.isEmpty(message)) {
                    message = handleMessage(rev, mWriter);
                }
                mWriter.write(message + "\r\n");
                mWriter.flush();
                closeSocket();
            }
        } catch (Exception e) {
            logger.error("error in MessageHandler -- closing down.");
            e.printStackTrace();
        } finally {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取报文体的内容
     * 进行逻辑处理
     *
     * @param pMessage
     * @param bwriter
     */
    public String handleMessage(String pMessage, BufferedWriter bwriter) throws IOException {
        //todo:此处填写消息处理代码
        logger.info("--------------- 开始处理监管系统的传入的消息为:{} ---------------", pMessage);

        // stop 1  解析报文体
        Map<String, String> retKeyDict = bankBackMessageAnalysis.parsingTranMessageString(pMessage);

        // stop 2 获取交易码
        String tranFunc = retKeyDict.get("TranFunc");
        logger.info("当前的交易码为{}", tranFunc);

        // stop 3 根据交易码,进行逻辑处理
        ICashFunctionService cashFunctionService = cashFunctionServiceList.stream().
                filter(function -> function.getType().getIndex() == Integer.valueOf(tranFunc)).findFirst().orElse(null);

        // stop 4 把报文回写银行
        if (cashFunctionService != null) {
            String responseMessage = cashFunctionService.response(retKeyDict);
            logger.info("--------------- 拼接监管系统返回的消息{}---------------", responseMessage);
            // stop 5 回写报文
            return responseMessage;
        }
        return "";
    }

    /**
     * 获取报文体的内容
     * 进行逻辑处理（重写）
     *
     * @param pMessage
     * @param bwriter
     */
    public String handleMessage1(String pMessage, BufferedWriter bwriter) throws IOException {
        String tranBackMessage = orderWithdrawService.withdrawForBank(pMessage);
        if (StringUtil.isEmpty(tranBackMessage)) {
            tranBackMessage = signingRecordService.getAcountBalance(pMessage);
            return tranBackMessage;
        }
        return tranBackMessage;
    }

    /**
     * 关闭通讯
     *
     * @throws java.io.IOException
     */
    public void closeSocket() throws java.io.IOException {
        mSocket.close();
    }
}



