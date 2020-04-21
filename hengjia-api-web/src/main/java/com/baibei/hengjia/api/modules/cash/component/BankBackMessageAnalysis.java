package com.baibei.hengjia.api.modules.cash.component;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回报文解析
 *
 * @author 林英男
 */
@Component
public class BankBackMessageAnalysis {
    /**
     * 解析返回报文
     * 解析规则,当报文转换为gbk,通信报文头222位+业务报文头122位+报文体(变体)——其中长度是固定的
     * 如果你要获取特定的字段，只要遍历backMessage字节，获取特定位数的字段 其中backHeadMessages代表报文头222位的193位
     *
     * @param TranMessage 返回报文
     * @return
     */
    public Map<String, String> parsingTranMessageString(String TranMessage) {
        Map<String, String> retKeyDict = new HashMap<>();
        byte[] backMessage;
        try {
            backMessage = TranMessage.getBytes("GBK");
            /*获取返回结果描述*/
            byte[] backHeadMessages = new byte[193];
            for (int i = 0; i < 193; i++) {
                backHeadMessages[i] = backMessage[i];
            }
            String bodyHeadMessage = new String(backHeadMessages, "GBK");
            String externalNo = bodyHeadMessage.substring(67, 87).replaceAll(" ", ""); //取消空格
            String rspCode = bodyHeadMessage.substring(87, 93);
            String rspMsg = bodyHeadMessage.substring(93);
            retKeyDict.put("RspCode", rspCode);//应答码
            retKeyDict.put("RspMsg", rspMsg);//应答描述
            retKeyDict.put("externalNo", externalNo); // 请求方系统流水号
            /*获取接口交易码*/
            if ("000000".equals(rspCode) || "999999".equals(rspCode)) {
                byte[] TranFuncs = new byte[226];
                for (int i = 222; i < 226; i++) {
                    TranFuncs[i] = backMessage[i];
                }
                String TranFunc = new String(TranFuncs, "gbk").trim();
                retKeyDict.put("TranFunc", TranFunc);//接口交易码
                /*获取返回业务报文体（接口返回字段）*/
                byte[] backbodyMessages = new byte[backMessage.length];
                for (int i = 344; i < backMessage.length; i++) {
                    backbodyMessages[i] = backMessage[i];
                }
                String backbodyMessage = new String(backbodyMessages, "gbk").trim();
                retKeyDict.put("backBodyMessages", backbodyMessage);//返回报文业务报文体
                spiltMessage(retKeyDict);//解析报文体
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return retKeyDict;
    }

    /**
     * 解析业务报文体
     *
     * @param retKeyDict
     */
    public void spiltMessage(Map retKeyDict) {
        int tranFunc = Integer.parseInt((String) retKeyDict.get("TranFunc"));
        if(1318 == tranFunc){
            spiltMessage_1318(retKeyDict);
        }
        if(1317 == tranFunc){
            spiltMessage_1317(retKeyDict);
        }
        if (1325 == tranFunc) {
            spiltMessage_1325(retKeyDict);
        }
        if (1312 == tranFunc) {
            spiltMessage_1312(retKeyDict);
        }
        if (1010 == tranFunc) {
            spiltMessage_1010(retKeyDict);
        }
    }


    public void spiltMessage_1303(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FuncFlag", bodyMessages[0]);
        retKeyDict.put("SupAcctId", bodyMessages[1]);
        retKeyDict.put("CustAcctId", bodyMessages[2]);
        retKeyDict.put("CustName", bodyMessages[3]);
        retKeyDict.put("ThirdCustId", bodyMessages[4]);
        retKeyDict.put("IdType", bodyMessages[5]);
        retKeyDict.put("IdCode", bodyMessages[6]);
        retKeyDict.put("RelatedAcctId", bodyMessages[7]);
        retKeyDict.put("AcctFlag", bodyMessages[8]);
        retKeyDict.put("TranType", bodyMessages[9]);
        retKeyDict.put("AcctName", bodyMessages[10]);
        retKeyDict.put("BankCode", bodyMessages[11]);
        retKeyDict.put("BankName", bodyMessages[12]);
        retKeyDict.put("OldRelatedAcctId", bodyMessages[13]);
        retKeyDict.put("Reserve", bodyMessages[14]);

    }

    public void spiltMessage_1310(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("SupAcctId", bodyMessages[0]);
        retKeyDict.put("CustAcctId", bodyMessages[1]);
        retKeyDict.put("TranAmount", bodyMessages[2]);
        retKeyDict.put("InAcctId", bodyMessages[3]);
        retKeyDict.put("InAcctIdName", bodyMessages[4]);
        retKeyDict.put("CcyCode", bodyMessages[5]);
        retKeyDict.put("AcctDate", bodyMessages[6]);
        retKeyDict.put("Reserve", bodyMessages[7]);
    }

    public void spiltMessage_1318(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("HandFee", bodyMessages[1]);
        retKeyDict.put("FeeOutCustId", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    public void spiltMessage_1317(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("HandFee", bodyMessages[1]);
        retKeyDict.put("FeeOutCustId", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    public void spiltMessage_1002(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FileName", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1003(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1004(Map<String, String> retKeyDict) {
        String backBodyMessage = retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("RecordCount", bodyMessages[0]);
        retKeyDict.put("ResultFlag", bodyMessages[1]);
       /* int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 8 + 4];
        retKeyDict.put("Array", ArrayContent);*/
        retKeyDict.put("Reserve", bodyMessages[bodyMessages.length - 1]);

    }

    public void spiltMessage_1005(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FuncFlag", bodyMessages[0]);
        retKeyDict.put("SupAcctId", bodyMessages[1]);
        retKeyDict.put("FileName", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    public void spiltMessage_1006(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FileName", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1010(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TotalCount", bodyMessages[0]);
        retKeyDict.put("BeginNum", bodyMessages[1]);
        retKeyDict.put("LastPage", bodyMessages[2]);
        retKeyDict.put("RecordNum", bodyMessages[3]);
        int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 11 + 4];
        retKeyDict.put("Array", ArrayContent);
        retKeyDict.put("Reserve", reserve);

    }

    public void spiltMessage_1022(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TranWebCode", bodyMessages[0]);
        retKeyDict.put("TranWebName", bodyMessages[1]);
        retKeyDict.put("AcctId", bodyMessages[3]);
        retKeyDict.put("AcctName", bodyMessages[4]);
        retKeyDict.put("IdType", bodyMessages[5]);
        retKeyDict.put("IdCode", bodyMessages[6]);
        retKeyDict.put("WebName", bodyMessages[7]);
        retKeyDict.put("WebCustId", bodyMessages[8]);
        retKeyDict.put("FuncFlag", bodyMessages[9]);
        retKeyDict.put("CcyCode", bodyMessages[10]);
        retKeyDict.put("CurBalance", bodyMessages[11]);
        retKeyDict.put("Reserve", bodyMessages[12]);

    }

    public void spiltMessage_1016(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TotalCount", bodyMessages[0]);
        retKeyDict.put("BeginNum", bodyMessages[1]);
        retKeyDict.put("LastPage", bodyMessages[2]);
        retKeyDict.put("RecordNum", bodyMessages[3]);
        int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 8 + 4];
        retKeyDict.put("Array", ArrayContent);
        retKeyDict.put("Reserve", reserve);

    }

    public void spiltMessage_1019(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("SupAcctId", bodyMessages[0]);
        retKeyDict.put("ThirdCustId", bodyMessages[1]);
        retKeyDict.put("CustAcctId", bodyMessages[3]);
        retKeyDict.put("Reserve", bodyMessages[4]);

    }

    public void spiltMessage_1325(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TotalCount", bodyMessages[0]);
        retKeyDict.put("BeginNum", bodyMessages[1]);
        retKeyDict.put("LastPage", bodyMessages[2]);
        retKeyDict.put("RecordNum", bodyMessages[3]);
        int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 9 + 4];
        retKeyDict.put("Array", ArrayContent);
        retKeyDict.put("Reserve", reserve);

    }

    public void spiltMessage_1324(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TotalCount", bodyMessages[0]);
        retKeyDict.put("BeginNum", bodyMessages[1]);
        retKeyDict.put("LastPage", bodyMessages[2]);
        retKeyDict.put("RecordNum", bodyMessages[3]);
        int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 10 + 4];
        retKeyDict.put("Array", ArrayContent);
        retKeyDict.put("Reserve", reserve);

    }

    public void spiltMessage_1330(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);
    }

    public void spiltMessage_1332(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1028(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("InCustBalance", bodyMessages[1]);
        retKeyDict.put("OutCustBalance", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    public void spiltMessage_1029(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1030(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    public void spiltMessage_1031(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);
    }

    public void spiltMessage_1351(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TotalCount", bodyMessages[0]);
        retKeyDict.put("BeginNum", bodyMessages[1]);
        retKeyDict.put("LastPage", bodyMessages[2]);
        retKeyDict.put("RecordNum", bodyMessages[3]);
        int iCount = Integer.parseInt(bodyMessages[3]);
        int iBegin1 = backBodyMessage.indexOf("&");
        String ArrayContent = backBodyMessage.substring(iBegin1 + 1);
        int iBegin2 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin2 + 1);
        int iBegin3 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin3 + 1);
        int iBegin4 = ArrayContent.indexOf("&");
        ArrayContent = ArrayContent.substring(iBegin4 + 1);
        int iEnd1 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd1);
        int iEnd2 = ArrayContent.lastIndexOf("&");
        ArrayContent = ArrayContent.substring(0, iEnd2);
        String reserve = bodyMessages[iCount * 14 + 4];
        retKeyDict.put("Array", ArrayContent);
        retKeyDict.put("Reserve", reserve);
    }

    public void spiltMessage_1352(Map<String, String> retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("ReceptNo", bodyMessages[0]);
        retKeyDict.put("CheckCode", bodyMessages[1]);
        retKeyDict.put("Reserve", bodyMessages[2]);
    }


    private void spiltMessage_1312(Map retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TranWebName", bodyMessages[0]);
        retKeyDict.put("ThirdCustId", bodyMessages[1]);
        retKeyDict.put("CustAcctId", bodyMessages[2]);
        retKeyDict.put("CustName", bodyMessages[3]);
        retKeyDict.put("SupAcctId", bodyMessages[4]);
        retKeyDict.put("OutAcctId", bodyMessages[5]);
        retKeyDict.put("OutAcctIdName", bodyMessages[6]);
        retKeyDict.put("CcyCode", bodyMessages[7]);
        retKeyDict.put("TranAmount", bodyMessages[8]);
        retKeyDict.put("HandFee", bodyMessages[9]);
        retKeyDict.put("FeeOutCustId", bodyMessages[10]);
        retKeyDict.put("Reserve", bodyMessages[11]);

    }
}
