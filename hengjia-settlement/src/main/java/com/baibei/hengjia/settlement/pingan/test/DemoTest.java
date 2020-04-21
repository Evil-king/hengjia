package com.baibei.hengjia.settlement.pingan.test;

import com.baibei.hengjia.settlement.pingan.BankInterface.BankInterfaceBackMessage;
import com.baibei.hengjia.settlement.pingan.BankInterface.BankInterfaceMessage;
import com.baibei.hengjia.settlement.pingan.server.GetServer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class DemoTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String ip = "10.14.156.220";// 前置机所在机器Ip地址
        String port = "7072";
        BankInterfaceMessage bf = new BankInterfaceMessage();// 拼接报文类
        GetServer yinShangJieSuanTongDao = new GetServer();// 发送报文并接受返回报文类
        BankInterfaceBackMessage bm = new BankInterfaceBackMessage();// 解析返回报文类
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        HashMap<String, String> retKeyDict;// 返回报文解析结果
        parmaKeyDict.put("Qydm", "9199");// 平台代码
        parmaKeyDict.put("TranFunc", "1352");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", "00000000000000000002");// 第三方交易流水号
        parmaKeyDict.put("SupAcctId", "15000090324892");// 1352接口字段
        parmaKeyDict.put("MainAcctId", "888800117507869");// 1352接口字段
        parmaKeyDict.put("ReceptNo", "18071096123593");// 1352接口字段
        parmaKeyDict.put("Reserve", "");// 1352接口字段
        String Message = bf.getSignMessage(parmaKeyDict);
        byte[] Messages;
        try {
            Messages = Message.getBytes("gbk");
            String HeadMessage = bf.getYinShangJieSuanTongHeadMessage(Messages.length, parmaKeyDict);
            String tranMessage = HeadMessage + Message;
            String tranBackMessage = yinShangJieSuanTongDao.sendAndGetMessage(ip, port, tranMessage);
            System.out.println("请求报文：" + tranMessage);
            System.out.println("返回报文：" + tranBackMessage);
            retKeyDict = bm.parsingTranMessageString(tranBackMessage);
            System.out.println("应答码：" + retKeyDict.get("RspCode"));
            System.out.println("应答描述：" + retKeyDict.get("RspMsg"));
            System.out.println("回单号：" + retKeyDict.get("ReceptNo"));
            System.out.println("回单验证码：" + retKeyDict.get("CheckCode"));
            System.out.println("保留域：" + retKeyDict.get("Reserve"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

