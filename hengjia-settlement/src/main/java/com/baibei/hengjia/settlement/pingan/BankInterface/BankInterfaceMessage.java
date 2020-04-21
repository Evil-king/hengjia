package com.baibei.hengjia.settlement.pingan.BankInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * 请求报文拼接
 *
 * @author 林英男
 */
public class BankInterfaceMessage {
    /**
     * @param parmaKeyDict 存放接口字段信息以及报文头字段信息的Map
     * @param bodyLength   报文体长度
     * @author 林英男
     * @see 报文头拼接
     */
    public String getYinShangJieSuanTongHeadMessage(int bodyLength, HashMap<String, String> parmaKeyDict) {
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
                + "PA001"// 操作袁代码
                + "01" // 服务类型
                + df.format(new Date())// 交易日期+交易时间
                + (String) parmaKeyDict.get("ThirdLogNo")// 请求方系统流水号
                + "000000" // 返回码
                + String.format("%1$-100s", " ")// 返回描述
                + "0"// 后续包标志
                + "000"// 请求次数
                + "0"// 签名标识
                + " "// 签名数据包格式
                + String.format("%1$-12s", " ")// 签名算法
                + "0000000000"// 签名数据长度
                + "0"// 附件数目
                + parmaKeyDict.get("TranFunc")
                + "01"
                + "                "
                + df.format(new Date())
                + "000000"
                + "                                          "
                + "0"
                + Length2
                + "PA001"
                + (String) parmaKeyDict.get("ThirdLogNo")
                + (String) parmaKeyDict.get("Qydm");
        return head;
    }

    /**
     * @param parmaKeyDict 存放接口字段信息以及报文头字段信息的Map
     * @return
     * @author 林英男
     * @see 报文体拼接
     */
    public String getSignMessage(HashMap parmaKeyDict) {
        String signMessageBody = "";
        int hTranFunc = Integer.parseInt((String) parmaKeyDict.get("TranFunc"));

        switch (hTranFunc) {
           /* case 1303:
                signMessageBody = getSignMessageBody_1303(parmaKeyDict);
                break;
            case 1310:
                signMessageBody = getSignMessageBody_1310(parmaKeyDict);
                break;
            case 1312:
                signMessageBody = getSignMessageBody_1312(parmaKeyDict);
                break;*/
            case 1318:
                signMessageBody = getSignMessageBody_1318(parmaKeyDict);
                break;
            case 1317:
                signMessageBody = getSignMessageBody_1317(parmaKeyDict);
                break;
            case 1002:
                signMessageBody = getSignMessageBody_1002(parmaKeyDict);
                break;
            case 1003:
                signMessageBody = getSignMessageBody_1003(parmaKeyDict);
                break;
            case 1004:
                signMessageBody = getSignMessageBody_1004(parmaKeyDict);
                break;
            case 1005:
                signMessageBody = getSignMessageBody_1005(parmaKeyDict);
                break;
            case 1006:
                signMessageBody = getSignMessageBody_1006(parmaKeyDict);
                break;
            case 1010:
                signMessageBody = getSignMessageBody_1010(parmaKeyDict);
                break;
            case 1022:
                signMessageBody = getSignMessageBody_1022(parmaKeyDict);
                break;
            case 1012:
                signMessageBody = getSignMessageBody_1012(parmaKeyDict);
                break;
            case 1016:
                signMessageBody = getSignMessageBody_1016(parmaKeyDict);
                break;
            case 1019:
                signMessageBody = getSignMessageBody_1019(parmaKeyDict);
                break;
            case 1325:
                signMessageBody = getSignMessageBody_1325(parmaKeyDict);
                break;
            case 1324:
                signMessageBody = getSignMessageBody_1324(parmaKeyDict);
                break;
            case 1330:
                signMessageBody = getSignMessageBody_1330(parmaKeyDict);
                break;
            case 1332:
                signMessageBody = getSignMessageBody_1332(parmaKeyDict);
                break;
            case 1028:
                signMessageBody = getSignMessageBody_1028(parmaKeyDict);
                break;
            case 1029:
                signMessageBody = getSignMessageBody_1029(parmaKeyDict);
                break;
            case 1030:
                signMessageBody = getSignMessageBody_1030(parmaKeyDict);
                break;
            case 1031:
                signMessageBody = getSignMessageBody_1031(parmaKeyDict);
                break;
            case 1351:
                signMessageBody = getSignMessageBody_1351(parmaKeyDict);
                break;
            case 1352:
                signMessageBody = getSignMessageBody_1352(parmaKeyDict);
                break;

            default:
                signMessageBody = "交易码未配置，请自行根据接口文档及demo封装接口";
        }

        return signMessageBody;
    }

    /*private String getSignMessageBody_1303(HashMap parmaKeyDict) {
        String ThirdLogNo = "";//交易网入金流水号
        String Reserve = "";//保留与

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            FuncFlag = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1310(HashMap parmaKeyDict) {
        String ThirdLogNo = "";//交易网入金流水号
        String Reserve = "";//保留与

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            FuncFlag = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1312(HashMap parmaKeyDict) {
        String ThirdLogNo = "";//交易网入金流水号
        String Reserve = "";//保留与

        if (parmaKeyDict.containsKey("ThirdLogNo")) {
            FuncFlag = (String) parmaKeyDict.get("ThirdLogNo");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return ThirdLogNo + "&" + Reserve + "&";
    }
*/
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

    private String getSignMessageBody_1002(HashMap parmaKeyDict) {
        String FuncFlag = "";// 功能标志
        String SupAcctId = "";// 资金汇总账号
        String TranDate = "";// 日期
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1003(HashMap parmaKeyDict) {
        String FuncFlag = "";// 批量标识
        String FileName = "";// 批量文件名
        String FileSize = "";// 文件大小
        String SupAcctId = "";// 资金汇总账号
        String QsZcAmount = "";// 清收买卖货款扎差金额
        String FreezeAmount = "";// 冻结总金额
        String UnfreezeAmount = "";// 解冻总金额
        String SyZcAmount = "";// 损益扎差数
        String Reserve = "";// 文件密码
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
                + "&" + UnfreezeAmount + "&" + FuncFlag + "&" + SyZcAmount + "&";
    }

    private String getSignMessageBody_1004(HashMap parmaKeyDict) {
        String FuncFlag = "";// 批量任务标识
        String BeginDate = "";// 起始日期
        String EndDate = "";// 结束日期
        String SupAcctId = "";// 资金汇总账号
        String Reserve = "";// 保留域
        if (parmaKeyDict.containsKey("FuncFlag")) {
            FuncFlag = (String) parmaKeyDict.get("FuncFlag");
        }
        if (parmaKeyDict.containsKey("BeginDate")) {
            BeginDate = (String) parmaKeyDict.get("BeginDate");
        }
        if (parmaKeyDict.containsKey("EndDate")) {
            EndDate = (String) parmaKeyDict.get("EndDate");
        }
        if (parmaKeyDict.containsKey("SupAcctId")) {
            SupAcctId = (String) parmaKeyDict.get("SupAcctId");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return FuncFlag + "&" + BeginDate + "&" + BeginDate + "&" + SupAcctId + "&" + Reserve + "&";
    }

    private String getSignMessageBody_1005(HashMap parmaKeyDict) {
        String Reserve = "";//保留与

        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return Reserve + "&";
    }

    private String getSignMessageBody_1006(HashMap parmaKeyDict) {
        String FuncFlag = "";// 功能标志
        String SupAcctId = "";// 资金汇总账号
        String BeginDateTime = "";// 开始时间
        String EndDateTime = "";// 结束时间
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1010(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String ThirdCustId = "";// 交易网会员代码
        String CustAcctId = "";// 子账号
        String SelectFlag = "";// 查询标志
        String PageNum = "";// 第几页
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1022(HashMap parmaKeyDict) {
        String AcctId = "";// 资金汇总账号
        String TranWebCode = "";// 交易网代码
        String Reserve = "";// 保留域
        if (parmaKeyDict.containsKey("AcctId")) {
            AcctId = (String) parmaKeyDict.get("AcctId");
        }
        if (parmaKeyDict.containsKey("TranWebCode")) {
            TranWebCode = (String) parmaKeyDict.get("TranWebCode");
        }
        if (parmaKeyDict.containsKey("Reserve")) {
            Reserve = (String) parmaKeyDict.get("Reserve");
        }
        return null;
    }

    private String getSignMessageBody_1012(HashMap parmaKeyDict) {
        String CustAcctId = "";// 子账号
        String SelectFlag = "";// 查询标志
        String PageNum = "";// 第几页
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1016(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String BeginDate = "";// 开始日期
        String EndDate = "";// 结束日期
        String PageNum = "";// 第几页
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1019(HashMap parmaKeyDict) {
        String CustAcctId = "";// 子账户
        String ThirdCustId = "";// 交易网会员代码
        String CustName = "";// 子账户名称
        String TotalAmount = "";// 账户总余额
        String TotalBalance = "";// 账户可用余额
        String TotalFreezeAmount = "";// 账户总冻结金额
        String TranDate = "";// 维护日期
        String Reserve = "";// 保留域

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

    private String getSignMessageBody_1324(HashMap parmaKeyDict) {
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

    private String getSignMessageBody_1330(HashMap parmaKeyDict) {
        String FuncFlag = "";// 请求功能
        String TxDate = "";// 交易日期
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1332(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String FuncFlag = "";// 功能标志
        String OutCustAcctId = "";// 转出子账户
        String OutThirdCustId = "";// 转出会员代码
        String InCustAcctId = "";// 转入子账户
        String InThirdCustId = "";// 转入会员代码
        String TranAmount = "";// 支付金额
        String HandFee = "";// 手续费金额
        String CcyCode = "";// 币种
        String PaySerialNo = "";// 支付指令号
        String ThirdHtId = "";// 支付订单号
        String ThirdHtCont = "";// 支付订单内容
        String Note = "";// 备注
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1028(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String FuncFlag = "";// 功能标志
        String OutCustAcctId = "";// 转出子账户
        String OutThirdCustId = "";// 转出会员代码
        String InCustAcctId = "";// 转入子账户
        String InThirdCustId = "";// 转入会员代码
        String TranAmount = "";// 支付金额
        String CcyCode = "";// 币种
        String ThirdHtId = "";// 支付订单号
        String Note = "";// 备注
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1029(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String FuncFlag = "";// 功能标志
        String CustAcctId = "";// 子账户
        String ThirdCustId = "";// 会员代码
        String TranAmount = "";// 交易金额
        String CcyCode = "";// 币种
        String ThirdHtId = "";// 订单号
        String Note = "";// 备注
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1030(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String FuncFlag = "";// 功能标志
        String CustAcctId = "";// 子账户
        String ThirdCustId = "";// 会员代码
        String TranAmount = "";// 交易金额
        String CcyCode = "";// 币种
        String ThirdHtId = "";// 订单号
        String Note = "";// 备注
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1031(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String FuncFlag = "";// 功能标志
        String CustAcctId = "";// 子账户
        String ThirdCustId = "";// 会员代码
        String TranAmount = "";// 交易金额
        String CcyCode = "";// 币种
        String ThirdHtId = "";// 订单号
        String Note = "";// 备注
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1351(HashMap parmaKeyDict) {
        String SupAcctId = "";// 资金汇总账号
        String CustAcctId = "";// 查询账户
        String SelectFlag = "";// 查询类型
        String BeginDate = "";// 开始日期
        String EndDate = "";// 结束日期
        String PageNum = "";// 第几页
        String Reserve = "";// 保留域
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

    private String getSignMessageBody_1352(HashMap parmaKeyDict) {
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
