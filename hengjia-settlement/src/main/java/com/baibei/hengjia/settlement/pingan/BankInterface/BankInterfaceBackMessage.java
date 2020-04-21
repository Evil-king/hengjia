package com.baibei.hengjia.settlement.pingan.BankInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 返回报文解析
 *
 * @author 林英男
 */
public class BankInterfaceBackMessage {
    /**
     * 解析返回报文
     *
     * @param TranMessage 返回报文
     * @return
     */
    public HashMap parsingTranMessageString(String TranMessage) {
        HashMap<String, String> retKeyDict = new HashMap<String, String>();
        byte[] backMessage;
        try {
            backMessage = TranMessage.getBytes("GBK");
            /*获取返回结果描述*/
            byte[] backHeadMessages = new byte[193];
            for (int i = 0; i < 193; i++) {
                backHeadMessages[i] = backMessage[i];
            }
            String bodyHeadMessage = new String(backHeadMessages, "GBK");
            String rspCode = bodyHeadMessage.substring(87, 93);
            String rspMsg = bodyHeadMessage.substring(93);
            retKeyDict.put("RspCode", rspCode);//应答码
            retKeyDict.put("RspMsg", rspMsg);//应答描述
			/*获取接口交易码*/
            if ("000000".equals(rspCode)) {
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
    public void spiltMessage(HashMap retKeyDict) {
        int tranFunc = Integer.parseInt((String) retKeyDict.get("TranFunc"));
        switch (tranFunc) {
            case 1303:
                spiltMessage_1303(retKeyDict);
                break;
            case 1310:
                spiltMessage_1310(retKeyDict);
                break;
            case 1312:
                spiltMessage_1312(retKeyDict);
                break;
            case 1318:
                spiltMessage_1318(retKeyDict);
                break;
            case 1317:
                spiltMessage_1317(retKeyDict);
                break;
            case 1002:
                spiltMessage_1002(retKeyDict);
                break;
            case 1003:
                spiltMessage_1003(retKeyDict);
                break;
            case 1004:
                spiltMessage_1004(retKeyDict);
                break;
            case 1005:
                spiltMessage_1005(retKeyDict);
                break;
            case 1006:
                spiltMessage_1006(retKeyDict);
                break;
            case 1010:
                spiltMessage_1010(retKeyDict);
                break;
            case 1022:
                spiltMessage_1022(retKeyDict);
                break;
            case 1012:
                spiltMessage_1012(retKeyDict);
                break;
            case 1016:
                spiltMessage_1016(retKeyDict);
                break;
            case 1019:
                spiltMessage_1019(retKeyDict);
                break;
            case 1325:
                spiltMessage_1325(retKeyDict);
                break;
            case 1324:
                spiltMessage_1324(retKeyDict);
                break;
            case 1330:
                spiltMessage_1330(retKeyDict);
                break;
            case 1332:
                spiltMessage_1332(retKeyDict);
                break;
            case 1028:
                spiltMessage_1028(retKeyDict);
                break;
            case 1029:
                spiltMessage_1029(retKeyDict);
                break;
            case 1030:
                spiltMessage_1030(retKeyDict);
                break;
            case 1031:
                spiltMessage_1031(retKeyDict);
                break;
            case 1351:
                spiltMessage_1351(retKeyDict);
                break;
            case 1352:
                spiltMessage_1352(retKeyDict);
                break;
        }
    }

    private void spiltMessage_1303(HashMap retKeyDict) {
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

    private void spiltMessage_1310(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
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

    private void spiltMessage_1312(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("TranWebName", bodyMessages[0]);
        retKeyDict.put("ThirdCustId", bodyMessages[1]);
        retKeyDict.put("CustAcctId", bodyMessages[3]);
        retKeyDict.put("CustName", bodyMessages[4]);
        retKeyDict.put("SupAcctId", bodyMessages[5]);
        retKeyDict.put("OutAcctId", bodyMessages[6]);
        retKeyDict.put("OutAcctIdName", bodyMessages[7]);
        retKeyDict.put("CcyCode", bodyMessages[8]);
        retKeyDict.put("TranAmount", bodyMessages[9]);
        retKeyDict.put("HandFee", bodyMessages[10]);
        retKeyDict.put("FeeOutCustId", bodyMessages[11]);
        retKeyDict.put("Reserve", bodyMessages[12]);

    }

    private void spiltMessage_1318(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("HandFee", bodyMessages[1]);
        retKeyDict.put("FeeOutCustId", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    private void spiltMessage_1317(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("HandFee", bodyMessages[1]);
        retKeyDict.put("FeeOutCustId", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    private void spiltMessage_1002(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FileName", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1003(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1004(HashMap retKeyDict) {
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

    private void spiltMessage_1005(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FuncFlag", bodyMessages[0]);
        retKeyDict.put("SupAcctId", bodyMessages[1]);
        retKeyDict.put("FileName", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    private void spiltMessage_1006(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FileName", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1010(HashMap retKeyDict) {
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

    private void spiltMessage_1022(HashMap retKeyDict) {
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

    private void spiltMessage_1012(HashMap retKeyDict) {
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

    private void spiltMessage_1016(HashMap retKeyDict) {
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

    private void spiltMessage_1019(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("SupAcctId", bodyMessages[0]);
        retKeyDict.put("ThirdCustId", bodyMessages[1]);
        retKeyDict.put("CustAcctId", bodyMessages[3]);
        retKeyDict.put("Reserve", bodyMessages[4]);

    }

    private void spiltMessage_1325(HashMap retKeyDict) {
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

    private void spiltMessage_1324(HashMap retKeyDict) {
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

    private void spiltMessage_1330(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1332(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1028(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("InCustBalance", bodyMessages[1]);
        retKeyDict.put("OutCustBalance", bodyMessages[2]);
        retKeyDict.put("Reserve", bodyMessages[3]);

    }

    private void spiltMessage_1029(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1030(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);

    }

    private void spiltMessage_1031(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("FrontLogNo", bodyMessages[0]);
        retKeyDict.put("Reserve", bodyMessages[1]);
    }

    private void spiltMessage_1351(HashMap retKeyDict) {
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

    private void spiltMessage_1352(HashMap retKeyDict) {
        String backBodyMessage = (String) retKeyDict.get("backBodyMessages");
        String[] bodyMessages = backBodyMessage.split("&", -1);
        retKeyDict.put("ReceptNo", bodyMessages[0]);
        retKeyDict.put("CheckCode", bodyMessages[1]);
        retKeyDict.put("Reserve", bodyMessages[2]);
    }

}

