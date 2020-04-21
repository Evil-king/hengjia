package com.baibei.hengjia.api.modules.cash.component;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 请求报文拼接
 * <p>
 * 请求的话，只要返回ReqCode(返回码）字段了就行，如果是接受请求,并返回的话，监管系统主要看应答码和应答码描述，如果是非6个0，判定这个交易是失败的
 *
 * @author 林英男
 */
@Component
public class BankMessageSplice {

    /**
     * `
     *
     * @param parmaKeyDict 存放接口字段信息以及报文头字段信息的Map
     * @param bodyLength   报文体长度
     * @author 林英男
     */
    public String getYinShangJieSuanTongHeadMessage(int bodyLength, Map<String, String> parmaKeyDict) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String Length1 = (bodyLength + 122) + "";
        String Length2 = "" + bodyLength;
        int count1 = Length1.length();
        int count2 = Length2.length();
        for (int i = 0; i < 10 - count1; i++) {
            Length1 = "0" + Length1;
        }
        for (int i = 0; i < 8 - count2; i++) {
            Length2 = "0" + Length2;
        }
        String head = "A001" // 报文版本
                + "03" // 目标系统
                + "01" // 报文编码
                + "01" // 通讯协议
                + String.format("%1$-20s", (String) parmaKeyDict.get("Qydm")) // 平台代码
                + Length1 // 报文长度
                + "000000"// 交易码
                + "PA001"//操作员代码
                + parmaKeyDict.get("ServiceType") // 服务类型
                + df.format(new Date())// 交易日期+交易时间
                + String.format("%1$-20s", parmaKeyDict.get("ThirdLogNo"))// 请求方系统流水号
                + (parmaKeyDict.get("ReqCode") == null ? "000000" : parmaKeyDict.get("ReqCode")) // 返回码
                /*    + String.format("%1$-100s", parmaKeyDict.get("ReqMsg") == null ? " " : parmaKeyDict.get("ReqMsg"))// 返回描述*/
                + returnMsg(parmaKeyDict.get("ReqMsg"), 100)
                + "0"// 后续包标志
                + "000"// 请求次数
                + "0"// 签名标识
                + " "// 签名数据包格式
                + String.format("%1$-12s", " ")// 签名算法
                + "0000000000"// 签名数据长度
                + "0"// 附件数目
                + parmaKeyDict.get("TranFunc") //交易类型
                + parmaKeyDict.get("ServiceType") //服务类型
                + "                " //MAC 码
                + df.format(new Date())
                + (parmaKeyDict.get("RspCode") == null ? "000000" : parmaKeyDict.get("RspCode")) // 应答码
                /* + String.format("%1$-42s", parmaKeyDict.get("RspMsg") == null ? " " : parmaKeyDict.get("RspMsg"))// 应答码描述*/
                + returnMsg(parmaKeyDict.get("RspMsg"), 42)
                + "0" //后续包标识
                + Length2 //报文体长度
                + "PA001" // 操作员号
                + String.format("%1$-20s", parmaKeyDict.get("ThirdLogNo")) //请求方系统流水号
                + parmaKeyDict.get("Qydm"); //交易网代码
        return head;
    }


    /**
     * @param parmaKeyDict 存放接口字段信息以及报文头字段信息的Map
     * @return
     */
    public String getSignMessage(Map parmaKeyDict) {
        String signMessageBody = "";
        int hTranFunc = Integer.parseInt((String) parmaKeyDict.get("TranFunc"));

        switch (hTranFunc) {
            case 1318:
                signMessageBody = getSignMessageBody_1318(parmaKeyDict);
                break;
            case 1312:
                signMessageBody = getSignMessageBody_1312(parmaKeyDict);
                break;
            case 1325:
                signMessageBody = getSignMessageBody_1325(parmaKeyDict);
                break;
            case 1317:
                signMessageBody = getSignMessageBody_1317(parmaKeyDict);
                break;
            case 1010:
                signMessageBody = getSignMessageBody_1010(parmaKeyDict);
                break;
            case 1019:
                signMessageBody = getSignMessageBody_1019(parmaKeyDict);
                break;
            case 1022:
                signMessageBody = getSignMessageBody_1022(parmaKeyDict);
                break;
        }
        return signMessageBody;
    }

    private String getSignMessageBody_1318(HashMap parmaKeyDict) {
        String TranWebName = "";// 交易网名称
        String ThirdCustId = "";// 交易网会员代码
        String IdType = "";// 会员证件类型
        String IdCode = "";// 会员证件号码
        String TranOutType = "";// 出金类型
        String CustAcctId = "";// 子账户账号
        String CustName = "";// 子账户名称
        String SupAcctId = "";// 资金汇总账号
        String TranType = "";// 转账方式
        String OutAcctId = "";// 出金账号
        String OutAcctIdName = "";// 出金账户名称
        String OutAcctIdBankName = "";// 出金账号开户行名
        String OutAcctIdBankCode = "";// 出金账号开户联行号
        String Address = "";// 出金账号开户行地址
        String CcyCode = "";// 币种
        String TranAmount = "";// 申请出金金额
        String FeeOutCustId = "";// 支付转账手续费的子账号
        String Reserve = "";// 保留域
        if (parmaKeyDict.containsKey("TranWebName")) {
            TranWebName = (String) parmaKeyDict.get("TranWebName");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("IdType")) {
            IdType = (String) parmaKeyDict.get("IdType");
        }
        if (parmaKeyDict.containsKey("IdCode")) {
            IdCode = (String) parmaKeyDict.get("IdCode");
        }
        if (parmaKeyDict.containsKey("TranOutType")) {
            TranOutType = (String) parmaKeyDict.get("TranOutType");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("CustName")) {
            CustName = (String) parmaKeyDict.get("CustName");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("TranType")) {
            TranType = (String) parmaKeyDict.get("TranType");
        }
        if (parmaKeyDict.containsKey("OutAcctId")) {
            OutAcctId = (String) parmaKeyDict.get("OutAcctId");
        }
        if (parmaKeyDict.containsKey("OutAcctIdName")) {
            OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
        }
        if (parmaKeyDict.containsKey("OutAcctIdBankName")) {
            OutAcctIdBankName = (String) parmaKeyDict.get("OutAcctIdBankName");
        }
        if (parmaKeyDict.containsKey("OutAcctIdBankCode")) {
            OutAcctIdBankCode = (String) parmaKeyDict.get("OutAcctIdBankCode");
        }
        if (parmaKeyDict.containsKey("Address")) {
            Address = (String) parmaKeyDict.get("Address");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("FeeOutCustId")) {
            FeeOutCustId = (String) parmaKeyDict.get("FeeOutCustId");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return TranWebName + "&" + ThirdCustId + "&" + IdType + "&" + IdCode + "&" + TranOutType + "&" + CustAcctId
                + "&" + CustName + "&" + SupAcctId + "&" + TranType + "&" + OutAcctId + "&" + OutAcctIdName + "&"
                + OutAcctIdBankName + "&" + OutAcctIdBankCode + "&" + Address + "&" + CcyCode + "&" + TranAmount + "&"
                + FeeOutCustId + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1312(HashMap parmaKeyDict) {
        String ThirdLogNo = "";//交易网入金流水号
        String Reserve = "";//保留与

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            ThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1325(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String OrigThirdLogNo = "";// 交易网流水号
        String BeginDate = "";// 开始日期
        String EndDate = "";// 结束日期
        String PageNum = "";// 第几页
        String Reserve = "";// 保留域
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("OrigThirdLogNo")) {
            OrigThirdLogNo = (String) parmaKeyDict.get("OrigThirdLogNo");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + OrigThirdLogNo + "&" + BeginDate + "&" + EndDate + "&" + PageNum + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1317(HashMap parmaKeyDict) {
        String FrontLogNo = "";// 银行前置流水号
        String TranWebName = "";// 交易网名称
        String ThirdCustId = "";// 交易网会员代码
        String IdType = "";// 会员证件类型
        String IdCode = "";// 会员证件号码
        String CustAcctId = "";// 子账户账号
        String CustName = "";// 子账户名称
        String SupAcctId = "";// 资金汇总账号
        String TranStatus = "";// 审核状态
        String TranType = "";// 转账方式
        String OutAcctId = "";// 出金账号
        String OutAcctIdName = "";// 出金账户名称
        String CcyCode = "";// 币种
        String TranAmount = "";// 申请出金金额
        String Reserve = "";// 保留域
        if (parmaKeyDict.containsKey("FrontLogNo")) {
            FrontLogNo = (String) parmaKeyDict.get("FrontLogNo");
        }
        if (parmaKeyDict.containsKey("TranWebName")) {
            TranWebName = (String) parmaKeyDict.get("TranWebName");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("IdType")) {
            IdType = (String) parmaKeyDict.get("IdType");
        }
        if (parmaKeyDict.containsKey("IdCode")) {
            IdCode = (String) parmaKeyDict.get("IdCode");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("CustName")) {
            CustName = (String) parmaKeyDict.get("CustName");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("TranStatus")) {
            TranStatus = (String) parmaKeyDict.get("TranStatus");
        }
        if (parmaKeyDict.containsKey("TranType")) {
            TranType = (String) parmaKeyDict.get("TranType");
        }
        if (parmaKeyDict.containsKey("OutAcctId")) {
            OutAcctId = (String) parmaKeyDict.get("OutAcctId");
        }
        if (parmaKeyDict.containsKey("OutAcctIdName")) {
            OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FrontLogNo + "&" + TranWebName + "&" + ThirdCustId + "&" + IdType + "&" + IdCode + "&" + CustAcctId + "&"
                + CustName + "&" + SupAcctId + "&" + TranStatus + "&" + TranType + "&" + OutAcctId + "&" + OutAcctIdName
                + "&" + CcyCode + "&" + TranAmount + "&" + Reserve + "&";
    }

    /**
     * @param msg
     * @param countLength 左移总位数
     * @return
     */
    public String returnMsg(String msg, Integer countLength) {
        StringBuilder formatStr = new StringBuilder();
        formatStr.append("%1$-");
        if (StringUtils.isEmpty(msg)) {
            formatStr.append(countLength);
            formatStr.append("s");
            return String.format(formatStr.toString(), " ");
        }
        formatStr.append(countLength - msg.length());
        formatStr.append("s");
        return String.format(formatStr.toString(), msg);
    }

    public String getSignMessageBody_1303(Map<String, String> parmaKeyDict) {
        String ThirdLogNo = "";//
        String Reserve = "";//

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            ThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1310(Map<String, String> parmaKeyDict) {
        String ThirdLogNo = "";//
        String Reserve = "";//

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            ThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1312(Map<String, String> parmaKeyDict) {
        String ThirdLogNo = "";//
        String Reserve = "";//

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            ThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1318(Map<String, String> parmaKeyDict) {
        String TranWebName = "";//
        String ThirdCustId = "";//
        String IdType = "";//
        String IdCode = "";//
        String TranOutType = "";//
        String CustAcctId = "";//
        String CustName = "";//
        String SupAcctId = "";//
        String TranType = "";//
        String OutAcctId = "";//
        String OutAcctIdName = "";//
        String OutAcctIdBankName = "";//
        String OutAcctIdBankCode = "";//
        String Address = "";//
        String CcyCode = "";//
        String TranAmount = "";//
        String FeeOutCustId = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("TranWebName")) {
            TranWebName = (String) parmaKeyDict.get("TranWebName");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("IdType")) {
            IdType = (String) parmaKeyDict.get("IdType");
        }
        if (parmaKeyDict.containsKey("IdCode")) {
            IdCode = (String) parmaKeyDict.get("IdCode");
        }
        if (parmaKeyDict.containsKey("TranOutType")) {
            TranOutType = (String) parmaKeyDict.get("TranOutType");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("CustName")) {
            CustName = (String) parmaKeyDict.get("CustName");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("TranType")) {
            TranType = (String) parmaKeyDict.get("TranType");
        }
        if (parmaKeyDict.containsKey("OutAcctId")) {
            OutAcctId = (String) parmaKeyDict.get("OutAcctId");
        }
        if (parmaKeyDict.containsKey("OutAcctIdName")) {
            OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
        }
        if (parmaKeyDict.containsKey("OutAcctIdBankName")) {
            OutAcctIdBankName = (String) parmaKeyDict.get("OutAcctIdBankName");
        }
        if (parmaKeyDict.containsKey("OutAcctIdBankCode")) {
            OutAcctIdBankCode = (String) parmaKeyDict.get("OutAcctIdBankCode");
        }
        if (parmaKeyDict.containsKey("Address")) {
            Address = (String) parmaKeyDict.get("Address");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("FeeOutCustId")) {
            FeeOutCustId = (String) parmaKeyDict.get("FeeOutCustId");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return TranWebName + "&" + ThirdCustId + "&" + IdType + "&" + IdCode + "&" + TranOutType + "&" + CustAcctId
                + "&" + CustName + "&" + SupAcctId + "&" + TranType + "&" + OutAcctId + "&" + OutAcctIdName + "&"
                + OutAcctIdBankName + "&" + OutAcctIdBankCode + "&" + Address + "&" + CcyCode + "&" + TranAmount + "&"
                + FeeOutCustId + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1317(Map<String, String> parmaKeyDict) {
        String FrontLogNo = "";//
        String TranWebName = "";//
        String ThirdCustId = "";//
        String IdType = "";//
        String IdCode = "";//
        String CustAcctId = "";//
        String CustName = "";//
        String SupAcctId = "";//
        String TranStatus = "";//
        String TranType = "";//
        String OutAcctId = "";//
        String OutAcctIdName = "";//
        String CcyCode = "";//
        String TranAmount = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("FrontLogNo")) {
            FrontLogNo = (String) parmaKeyDict.get("FrontLogNo");
        }
        if (parmaKeyDict.containsKey("TranWebName")) {
            TranWebName = (String) parmaKeyDict.get("TranWebName");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("IdType")) {
            IdType = (String) parmaKeyDict.get("IdType");
        }
        if (parmaKeyDict.containsKey("IdCode")) {
            IdCode = (String) parmaKeyDict.get("IdCode");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("CustName")) {
            CustName = (String) parmaKeyDict.get("CustName");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("TranStatus")) {
            TranStatus = (String) parmaKeyDict.get("TranStatus");
        }
        if (parmaKeyDict.containsKey("TranType")) {
            TranType = (String) parmaKeyDict.get("TranType");
        }
        if (parmaKeyDict.containsKey("OutAcctId")) {
            OutAcctId = (String) parmaKeyDict.get("OutAcctId");
        }
        if (parmaKeyDict.containsKey("OutAcctIdName")) {
            OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FrontLogNo + "&" + TranWebName + "&" + ThirdCustId + "&" + IdType + "&" + IdCode + "&" + CustAcctId + "&"
                + CustName + "&" + SupAcctId + "&" + TranStatus + "&" + TranType + "&" + OutAcctId + "&" + OutAcctIdName
                + "&" + CcyCode + "&" + TranAmount + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1002(Map<String, String> parmaKeyDict) {
        String FuncFlag = "";
        String SupAcctId = "";
        String TranDate = "";
        String Reserve = "";
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("TranDate")) {
            TranDate = (String) parmaKeyDict.get("TranDate");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + SupAcctId + "&" + TranDate + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1003(Map<String, String> parmaKeyDict) {
        String FuncFlag = "";//
        String FileName = "";//
        String FileSize = "";//
        String SupAcctId = "";//
        String QsZcAmount = "";//
        String FreezeAmount = "";//
        String UnfreezeAmount = "";
        String SyZcAmount = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("FileName")) {
            FileName = (String) parmaKeyDict.get("FileName");
        }
        if (parmaKeyDict.containsKey("FileSize")) {
            FileSize = (String) parmaKeyDict.get("FileSize");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("QsZcAmount")) {
            QsZcAmount = (String) parmaKeyDict.get("QsZcAmount");
        }
        if (parmaKeyDict.containsKey("FreezeAmount")) {
            FreezeAmount = (String) parmaKeyDict.get("FreezeAmount");
        }
        if (parmaKeyDict.containsKey("UnfreezeAmount")) {
            UnfreezeAmount = (String) parmaKeyDict.get("UnfreezeAmount");
        }
        if (parmaKeyDict.containsKey("SyZcAmount")) {
            SyZcAmount = (String) parmaKeyDict.get("SyZcAmount");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + FileName + "&" + FileSize + "&" + SupAcctId + "&" + QsZcAmount + "&" + FreezeAmount
                + "&" + UnfreezeAmount + "&" + SyZcAmount + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1004(Map<String, String> parmaKeyDict) {
        String FuncFlag = "";//
        String BeginDate = "";//
        String EndDate = "";//
        String SupAcctId = "";//
        String Reserve = "";
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + BeginDate + "&" + BeginDate + "&" + SupAcctId + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1005(Map<String, String> parmaKeyDict) {
        String Reserve = "";//

        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = parmaKeyDict.get("Reserve");
        }
        return Reserve + "&";
    }

    public String getSignMessageBody_1006(Map<String, String> parmaKeyDict) {
        String FuncFlag = "";//
        String SupAcctId = "";//
        String BeginDateTime = "";//
        String EndDateTime = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("BeginDateTime")) {
            BeginDateTime = (String) parmaKeyDict.get("BeginDateTime");
        }
        if (parmaKeyDict.containsKey("EndDateTime")) {
            EndDateTime = (String) parmaKeyDict.get("EndDateTime");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + SupAcctId + "&" + BeginDateTime + "&" + EndDateTime + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1010(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String ThirdCustId = "";//
        String CustAcctId = "";//
        String SelectFlag = "";//
        String PageNum = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("SelectFlag")) {
            SelectFlag = (String) parmaKeyDict.get("SelectFlag");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + ThirdCustId + "&" + CustAcctId + "&" + SelectFlag + "&" + PageNum + "&" + Reserve
                + "&";
    }

    public String getSignMessageBody_1022(Map<String, String> parmaKeyDict) {
        String AcctId = "";
        String TranWebCode = "";
        String Reserve = "";
        if (parmaKeyDict.containsKey("AcctId")) {
            AcctId = (String) parmaKeyDict.get("AcctId");
        }
        if (parmaKeyDict.containsKey("TranWebCode")) {
            TranWebCode = (String) parmaKeyDict.get("TranWebCode");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return AcctId + "&" + TranWebCode + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1012(Map<String, String> parmaKeyDict) {
        String CustAcctId = "";//
        String SelectFlag = "";//
        String PageNum = "";
        String Reserve = "";
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("SelectFlag")) {
            SelectFlag = (String) parmaKeyDict.get("SelectFlag");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return CustAcctId + "&" + SelectFlag + "&" + PageNum + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1016(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String BeginDate = "";//
        String EndDate = "";//
        String PageNum = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + BeginDate + "&" + EndDate + "&" + PageNum + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1019(Map<String, String> parmaKeyDict) {
        String CustAcctId = "";//
        String ThirdCustId = "";//
        String CustName = "";//
        String TotalAmount = "";//
        String TotalBalance = "";//
        String TotalFreezeAmount = "";//
        String TranDate = "";//
        String Reserve = "";//

        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("CustName")) {
            CustName = (String) parmaKeyDict.get("CustName");
        }
        if (parmaKeyDict.containsKey("TotalAmount")) {
            TotalAmount = (String) parmaKeyDict.get("TotalAmount");
        }
        if (parmaKeyDict.containsKey("TotalBalance")) {
            TotalBalance = (String) parmaKeyDict.get("TotalBalance");
        }
        if (parmaKeyDict.containsKey("TotalFreezeAmount")) {
            TotalFreezeAmount = (String) parmaKeyDict.get("TotalFreezeAmount");
        }
        if (parmaKeyDict.containsKey("TranDate")) {
            TranDate = (String) parmaKeyDict.get("TranDate");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return CustAcctId + "&" + ThirdCustId + "&" + CustName + "&" + TotalAmount + "&" + TotalBalance + "&" + TotalFreezeAmount + "&" + TranDate + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1325(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String OrigThirdLogNo = "";//
        String BeginDate = "";//
        String EndDate = "";//
        String PageNum = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            OrigThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + OrigThirdLogNo + "&" + BeginDate + "&" + EndDate + "&" + PageNum + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1324(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String OrigThirdLogNo = "";//
        String BeginDate = "";//
        String EndDate = "";//
        String PageNum = "";
        String Reserve = "";
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("OrigThirdLogNo")) {
            OrigThirdLogNo = (String) parmaKeyDict.get("OrigThirdLogNo");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + OrigThirdLogNo + "&" + BeginDate + "&" + EndDate + "&" + PageNum + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1330(Map<String, String> parmaKeyDict) {
        String FuncFlag = "";//
        String TxDate = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("TxDate")) {
            TxDate = (String) parmaKeyDict.get("TxDate");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + TxDate + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1332(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String FuncFlag = "";//
        String OutCustAcctId = "";//
        String OutThirdCustId = "";//
        String InCustAcctId = "";//
        String InThirdCustId = "";//
        String TranAmount = "";// ֧
        String HandFee = "";//
        String CcyCode = "";//
        String PaySerialNo = "";// ֧
        String ThirdHtId = "";// ֧
        String ThirdHtCont = "";// ֧
        String Note = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("OutCustAcctId")) {
            OutCustAcctId = (String) parmaKeyDict.get("OutCustAcctId");
        }
        if (parmaKeyDict.containsKey("OutThirdCustId")) {
            OutThirdCustId = (String) parmaKeyDict.get("OutThirdCustId");
        }
        if (parmaKeyDict.containsKey("InCustAcctId")) {
            InCustAcctId = (String) parmaKeyDict.get("InCustAcctId");
        }
        if (parmaKeyDict.containsKey("InThirdCustId")) {
            InThirdCustId = (String) parmaKeyDict.get("InThirdCustId");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("HandFee")) {
            HandFee = (String) parmaKeyDict.get("HandFee");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("PaySerialNo")) {
            PaySerialNo = (String) parmaKeyDict.get("PaySerialNo");
        }
        if (parmaKeyDict.containsKey("ThirdHtId")) {
            ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
        }
        if (parmaKeyDict.containsKey("ThirdHtCont")) {
            ThirdHtCont = (String) parmaKeyDict.get("ThirdHtCont");
        }
        if (parmaKeyDict.containsKey("Note")) {
            Note = (String) parmaKeyDict.get("Note");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + FuncFlag + "&" + OutCustAcctId + "&" + OutThirdCustId + "&" + InCustAcctId + "&"
                + InThirdCustId + "&" + TranAmount + "&" + HandFee + "&" + CcyCode + "&" + PaySerialNo + "&" + ThirdHtId
                + "&" + ThirdHtCont + "&" + Note + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1028(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String FuncFlag = "";//
        String OutCustAcctId = "";//
        String OutThirdCustId = "";//
        String InCustAcctId = "";//
        String InThirdCustId = "";//
        String TranAmount = "";//
        String CcyCode = "";//
        String ThirdHtId = "";//
        String Note = "";//
        String Reserve = "";
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("OutCustAcctId")) {
            OutCustAcctId = (String) parmaKeyDict.get("OutCustAcctId");
        }
        if (parmaKeyDict.containsKey("OutThirdCustId")) {
            OutThirdCustId = (String) parmaKeyDict.get("OutThirdCustId");
        }
        if (parmaKeyDict.containsKey("InCustAcctId")) {
            InCustAcctId = (String) parmaKeyDict.get("InCustAcctId");
        }
        if (parmaKeyDict.containsKey("InThirdCustId")) {
            InThirdCustId = (String) parmaKeyDict.get("InThirdCustId");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("ThirdHtId")) {
            ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
        }
        if (parmaKeyDict.containsKey("Note")) {
            Note = (String) parmaKeyDict.get("Note");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }

        return SupAcctId + "&" + FuncFlag + "&" + OutCustAcctId + "&" + OutThirdCustId + "&" + InCustAcctId + "&"
                + InThirdCustId + "&" + TranAmount + "&" + CcyCode + "&" + ThirdHtId + "&" + Note + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1029(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String FuncFlag = "";//
        String CustAcctId = "";//
        String ThirdCustId = "";//
        String TranAmount = "";//
        String CcyCode = "";//
        String ThirdHtId = "";//
        String Note = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("ThirdHtId")) {
            ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
        }
        if (parmaKeyDict.containsKey("Note")) {
            Note = (String) parmaKeyDict.get("Note");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + FuncFlag + "&" + CustAcctId + "&" + ThirdCustId + "&" + TranAmount + "&" + CcyCode
                + "&" + ThirdHtId + "&" + Note + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1030(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String FuncFlag = "";//
        String CustAcctId = "";//
        String ThirdCustId = "";//
        String TranAmount = "";//
        String CcyCode = "";//
        String ThirdHtId = "";//
        String Note = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("ThirdHtId")) {
            ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
        }
        if (parmaKeyDict.containsKey("Note")) {
            Note = (String) parmaKeyDict.get("Note");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + FuncFlag + "&" + CustAcctId + "&" + ThirdCustId + "&" + TranAmount + "&" + CcyCode
                + "&" + ThirdHtId + "&" + Note + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1031(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String FuncFlag = "";//
        String CustAcctId = "";//
        String ThirdCustId = "";//
        String TranAmount = "";//
        String CcyCode = "";//
        String ThirdHtId = "";//
        String Note = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("ThirdCustId")) {
            ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
        }
        if (parmaKeyDict.containsKey("TranAmount")) {
            TranAmount = (String) parmaKeyDict.get("TranAmount");
        }
        if (parmaKeyDict.containsKey("CcyCode")) {
            CcyCode = (String) parmaKeyDict.get("CcyCode");
        }
        if (parmaKeyDict.containsKey("ThirdHtId")) {
            ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
        }
        if (parmaKeyDict.containsKey("Note")) {
            Note = (String) parmaKeyDict.get("Note");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }

        return SupAcctId + "&" + FuncFlag + "&" + CustAcctId + "&" + ThirdCustId + "&" + TranAmount + "&" + CcyCode
                + "&" + ThirdHtId + "&" + Note + "&" + Reserve + "&";
    }

    public String getSignMessageBody_1351(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";//
        String CustAcctId = "";//
        String SelectFlag = "";//
        String BeginDate = "";//
        String EndDate = "";//
        String PageNum = "";//
        String Reserve = "";//
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("CustAcctId")) {
            CustAcctId = (String) parmaKeyDict.get("CustAcctId");
        }
        if (parmaKeyDict.containsKey("SelectFlag")) {
            SelectFlag = (String) parmaKeyDict.get("SelectFlag");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("PageNum")) {
            PageNum = (String) parmaKeyDict.get("PageNum");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + "&" + CustAcctId + "&" + SelectFlag + "&" + BeginDate + "&" + EndDate + "&" + PageNum + "&"
                + Reserve + "&";
    }

    public String getSignMessageBody_1352(Map<String, String> parmaKeyDict) {
        String SupAcctId = "";
        String MainAcctId = "";
        String ReceptNo = "";
        String Reserve = "";
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("MainAcctId")) {
            MainAcctId = (String) parmaKeyDict.get("MainAcctId");
        }
        if (parmaKeyDict.containsKey("ReceptNo")) {
            ReceptNo = (String) parmaKeyDict.get("ReceptNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return SupAcctId + '&' + MainAcctId + "&" + ReceptNo + "&" + Reserve + "&";
    }

}
