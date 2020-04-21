package com.baibei.hengjia.api.modules.cash.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/6/28 14:01
 * @description:
 */
@Data
public class TotalBalanceVo {
    private String RecordNum;//汇总账号总数
    private String TranWebCode;//交易网代码
    private String TranWebName;//交易网名称
    private String AcctId;//资金汇总账号
    private String AcctName;//户名
    private String IdType;//证件类型
    private String IdCode;//证件号码
    private String WebName;//网银用户名
    private String WebCustId;//网银客户号
    private String FuncFlag;//服务类型
    private String CcyCode;//币种
    private BigDecimal CurBalance;//当前账户余额
}
