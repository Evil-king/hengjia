package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.SignInBackDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.SignInBackVo;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.model.SignInBack;
import com.baibei.hengjia.api.modules.cash.service.ISignInBackService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签到|签退  发送请求
 */
@Service
public class CashFunctionSignInBackServiceImpl extends AbstractCashFunction<SignInBackDto, SignInBackVo> {

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    @Autowired
    private ISignInBackService signInBackService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> doRequest(SignInBackDto request, Map<String, String> parmaKeyDict) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = new Date();
        Map<String, String> requestResult = new HashMap<>();
        SignInBack signInBack = new SignInBack();
        signInBack.setCreateTime(new Date());
        signInBack.setFlag(new Byte(Constants.Flag.VALID));
        signInBack.setSignStatus(request.getFuncFlag());
        signInBack.setTxDate(nowDate);
        signInBack.setSignNo(serialNumberComponent.generateOrderNo(SignInBack.class, signInBackService, "G", "signNo"));
        signInBack.setReserve(request.getReserve());
        signInBack.setExternalNo(parmaKeyDict.get("externalNo"));
        signInBackService.save(signInBack);
        parmaKeyDict.put("ThirdLogNo", signInBack.getSignNo());
        parmaKeyDict.put("FuncFlag", request.getFuncFlag()); // 提交标识
        parmaKeyDict.put("TxDate", formatter.format(nowDate)); //时间格式化
        requestResult.put("signNo", signInBack.getSignNo()); //返回业务处理
        return requestResult;
    }

    @Override
    public String responseResult(Map<String, String> parmaKeyDict) {
        return interfaceMessage.getSignMessageBody_1330(parmaKeyDict);
    }


    /**
     * 1330 交易码
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.SIGN_IN_BACK;
    }


    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1330(retKeyDict);
        return retKeyDict;
    }

    /**
     * 接受传入参数,返回给前端使用
     *
     * @param params
     * @return
     */
    @Override
    protected SignInBackVo toEntityByHashMapResponse(Map<String, String> params) {
        SignInBackVo signInBackVo = new SignInBackVo();
        signInBackVo.setFrontLogNo(params.get("FrontLogNo"));
        signInBackVo.setReserve(params.get("Reserve"));
        return signInBackVo;
    }

    @Override
    public void afterRequest(SignInBackVo response, Map<String, String> requestResult) {
        if (requestResult.get("signNo") == null) {
            logger.info("保存当前签约信息外部编号失败");
        }
        Condition condition = new Condition(SignInBack.class);
        condition.createCriteria().andEqualTo("signNo", requestResult.get("signNo"));
        List<SignInBack> signInBackList = signInBackService.findByCondition(condition);
        if (signInBackList.size() != 1) {
            logger.info("当前签约流水号{}错误", requestResult.get("signNo"));
            return;
        }
        SignInBack signInBack = signInBackList.get(0);
        if (response != null) {
            signInBack.setExternalNo(response.getFrontLogNo()); //前置流水号
            signInBack.setStatus(Constants.Status.SUCCESS);
            this.signInBackService.update(signInBack);
        } else {
            signInBack.setStatus(Constants.Status.FAIL); //修改当前签到状态
            this.signInBackService.update(signInBack);
        }
        super.afterRequest(response, requestResult);
    }

}
