package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.FilePlannedSpeedDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.FilePlannedSpeedVo;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.settlement.biz.CleanBiz;
import com.baibei.hengjia.api.modules.settlement.model.CleanProgress;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanProgressService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 交易网查询银行清算与对账文件的进度1004
 */
@Service
public class CashFunctionFilePlannedSpeedServiceImpl extends AbstractCashFunction<FilePlannedSpeedDto, FilePlannedSpeedVo> {


    @Autowired
    private ICleanProgressService cleanProgressService;

    @Autowired
    private SerialNumberComponent serialNumberComponent;
    @Autowired
    private CleanBiz cleanBiz;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;


    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1004(retKeyDict);
        return retKeyDict;
    }

    @Override
    public String responseResult(Map<String, String> map) {
        return interfaceMessage.getSignMessageBody_1004(map);
    }

    /**
     * 当开始时日期和结束日期为今天时，代表查看今天的进度
     * @param request
     * @param parmaKeyDict
     * @return
     */
    @Override
    public Map<String, String> doRequest(FilePlannedSpeedDto request, Map<String, String> parmaKeyDict) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String startData = dateFormat.format(request.getBeginDate());
        String endData = dateFormat.format(request.getEndDate());
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateThiredLogNo());// 请求方流水号
        parmaKeyDict.put("FuncFlag", request.getFuncFlag()); //1：清算 2：余额对账 4：出入金流水 5：开销户流水对账
        parmaKeyDict.put("BeginDate", startData);
        parmaKeyDict.put("EndDate", endData);
        parmaKeyDict.put("SupAcctId", supAcctId);
        return parmaKeyDict;
    }


    @Override
    protected FilePlannedSpeedVo toEntityByHashMapResponse(Map<String, String> params) {
        FilePlannedSpeedVo filePlannedSpeedVo = new FilePlannedSpeedVo();
        filePlannedSpeedVo.setRecordCount(Integer.valueOf(params.get("RecordCount")));
        filePlannedSpeedVo.setResultFlag(params.get("ResultFlag"));
        return filePlannedSpeedVo;
    }

    /**
     * 获取结算结果
     *
     * @param response
     * @param requestResult
     */
    @Override
    public void afterRequest(FilePlannedSpeedVo response, Map<String, String> requestResult) {
        CleanProgress cleanProgress = new CleanProgress();
        cleanProgress.setBatchNo(requestResult.get("BeginDate"));
        cleanProgress.setFuncflag(requestResult.get("FuncFlag"));
        cleanProgress.setCreateTime(new Date());
        cleanProgress.setRecordCount(response.getRecordCount());
        // 返回标识 1:正取批量文件; 2:取批量文件失败  3:正在读取文件; 4:读取文件失败 5：正在处理中; 6：处理完成,但部分成功 7：处理全部失败, 8:处理完全成功 9:处理完成,但生成处理结果文件失败
        String resultFlag = response.getResultFlag();
        cleanProgress.setResultFlag(resultFlag);
        cleanProgress.setFlag(new Byte(Constants.Flag.VALID));
        cleanProgressService.save(cleanProgress);
        if (Constants.CleanProcessResult.SOME_SUCCESS.equals(resultFlag)
                || Constants.CleanProcessResult.ALL_SUCCESS.equals(resultFlag)
                || Constants.CleanProcessResult.ALL_FAIL.equals(resultFlag)) {
            String batchNo = DateUtil.yyyyMMddNoLine.get().format(new Date());
            // 更新清算状态
            cleanBiz.updateCleanStatus(batchNo);
            // 更新清算进度状态
            cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.CLEAN_PROCESS, Constants.CleanFlowPathStatus.COMPLETED);
        }
        logger.info("保存批量清算文件进度{}成功", JSONObject.toJSONString(cleanProgress));
    }

    /**
     * 交易码1004
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.FIND_FILE_PLANNED_SPEED;
    }
}
