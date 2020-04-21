package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.ReconciliationMatchingDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.ReconciliationMatchingVo;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 向银行发起对账出入金流水对账及会员开销户流水匹配
 */
@Service
public class CashFunctionReconciliationMatchingServiceImpl extends AbstractCashFunction<ReconciliationMatchingDto, ReconciliationMatchingVo> {


    @Autowired
    private SerialNumberComponent serialNumberComponent;

    /**
     * 解析平安银行的返回报文
     *
     * @param bodyMessages
     * @return
     */
    @Override
    public Map<String, String> spiltMessage(Map<String,String> bodyMessages) {
        bankBackMessageAnalysis.spiltMessage_1006(bodyMessages);
        return bodyMessages;
    }

    /**
     * 通知银行进行出入金对账，或者会员进行对账
     *
     * @param request      请求对象
     * @param parmaKeyDict 报文头参数
     * @return
     */
    @Override
    public Map<String, String> doRequest(ReconciliationMatchingDto request, Map<String, String> parmaKeyDict) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> result = new HashMap<>();
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateThiredLogNo()); //交易流水号
        parmaKeyDict.put("FuncFlag", request.getFuncFlag()); //交易标识
        parmaKeyDict.put("SupAcctId", supAcctId); //资金汇总账号
        parmaKeyDict.put("BeginDateTime", formatter.format(request.getBeginDateTime())); //开始时间
        parmaKeyDict.put("EndDateTime", formatter.format(DateUtil.getEndDay(request.getEndDateTime()))); //结束时间
        return result;
    }

    /**
     * 拼接请求报文
     *
     * @param map
     * @return
     */
    @Override
    public String responseResult(Map<String, String> map) {
        return interfaceMessage.getSignMessageBody_1006(map);
    }

    /**
     * 1006
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.DEPOSIT_WITHDRAW_MEMBERS_DETAIL;
    }

    /**
     * 装换处理VO对象
     *
     * @param params
     * @return
     */
    @Override
    protected ReconciliationMatchingVo toEntityByHashMapResponse(Map<String, String> params) {
        ReconciliationMatchingVo reconciliationMatchingVo = new ReconciliationMatchingVo();
        reconciliationMatchingVo.setFileName(params.get("FileName"));
        reconciliationMatchingVo.setReserve(params.get("Reserve"));
        return reconciliationMatchingVo;
    }
}
