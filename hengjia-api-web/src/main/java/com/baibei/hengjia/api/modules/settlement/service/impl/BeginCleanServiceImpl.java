package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.settlement.bean.dto.BeginCleanDto;
import com.baibei.hengjia.api.modules.settlement.bean.vo.BeginCleanVo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/27 11:09 AM
 * @description: 交易网触发银行进行清算对账-1003接口
 */
@Service
public class BeginCleanServiceImpl extends AbstractCashFunction<BeginCleanDto, BeginCleanVo> {

    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1003(retKeyDict);
        return retKeyDict;
    }

    @Override
    public String responseResult(Map<String, String> map) {
        return interfaceMessage.getSignMessageBody_1003(map);
    }

    @Override
    public CashFunctionType getType() {
        return CashFunctionType.BEGIN_CLEAN;
    }

    @Override
    public Map<String, String> doRequest(BeginCleanDto request, Map<String, String> parmaKeyDict) {
        parmaKeyDict.put("ThirdLogNo", request.getThirdLogNo());
        parmaKeyDict.put("FuncFlag", request.getFuncFlag());
        parmaKeyDict.put("FileName", request.getFileName());
        parmaKeyDict.put("FileSize", request.getFileSize());
        parmaKeyDict.put("SupAcctId", request.getSupAcctId());
        parmaKeyDict.put("QsZcAmount", request.getQsZcAmount().toPlainString());
        parmaKeyDict.put("FreezeAmount", request.getFreezeAmount().toPlainString());
        parmaKeyDict.put("UnfreezeAmount", request.getUnfreezeAmount().toPlainString());
        parmaKeyDict.put("SyZcAmount", request.getSyZcAmount().toPlainString());
        parmaKeyDict.put("Reserve", request.getReserve());
        logger.info("BeginCleanServiceImpl.doRequest,param={}", JSON.toJSONString(parmaKeyDict));
        return parmaKeyDict;
    }

    @Override
    protected BeginCleanVo toEntityByHashMapResponse(Map<String, String> params) {
        BeginCleanVo vo = new BeginCleanVo();
        vo.setReserve(params.get("FrontLogNo"));
        vo.setThirdLogNo(params.get("Reserve"));
        return vo;
    }
}
