package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.service.ISignInBackService;
import com.baibei.hengjia.api.modules.settlement.dao.AmountReturnMapper;
import com.baibei.hengjia.api.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.api.modules.settlement.service.IAmountReturnService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/02 16:49:07
 * @description: AmountReturn服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AmountReturnServiceImpl extends AbstractService<AmountReturn> implements IAmountReturnService {

    @Autowired
    private AmountReturnMapper tblSetAmountReturnMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ISignInBackService signInBackService;

    /**
     * 挂牌商
     */
    @Value("${hengjia.lister}")
    private String hengjiaLister;


    @Override
    public List<AmountReturn> findByBatchNo(String batchNo, String type) {
        if (StringUtils.isEmpty(batchNo)) {
            throw new IllegalArgumentException("batchNo can not be null");
        }
        Condition condition = new Condition(AmountReturn.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("type", type).
                andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }

    @Override
    public ApiResult process(String batchNo, String type) {
        // 是否已经签退
        boolean isSignBack = signInBackService.isToDaySignInBack(Constants.SigningInBack.SIGNING_BACK);
        if (!isSignBack) {
            return ApiResult.badParam("还未签退");
        }
        List<AmountReturn> list = findByBatchNo(batchNo, type);
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.badParam("业务办理集合为空");
        }
        Byte tradeType = Constants.AmountReturnType.FEE.equals(type) ? FundTradeTypeEnum.FEE_SETTLEMENT.getCode()
                : FundTradeTypeEnum.INTEGRARL_SETTLEMENT.getCode();
        for (AmountReturn amountReturn : list) {
            if (Constants.AmountReturnStatus.WAIT.equals(amountReturn.getStatus())) {
                ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                changeAmountDto.setCustomerNo(amountReturn.getCustomerNo());
                changeAmountDto.setOrderNo(amountReturn.getBatchNo());
                changeAmountDto.setChangeAmount(amountReturn.getReturnAmount());
                changeAmountDto.setTradeType(tradeType);
                changeAmountDto.setReType(Byte.valueOf("2"));
                if (amountReturn.getReturnAmount().compareTo(BigDecimal.ZERO) > 0) {
                    // 增加资金流水
                    accountService.changeAccount(changeAmountDto);
                }
                // 修改状态
                amountReturn.setStatus(Constants.AmountReturnStatus.COMPLETED);
                amountReturn.setModifyTime(new Date());
                update(amountReturn);
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult processById(String id) {
        AmountReturn amountReturn = findById(Long.valueOf(id));
        if (amountReturn == null) {
            return ApiResult.badParam("业务办理记录不存在");
        }
        // 是否已经签退
        boolean isSignBack = signInBackService.isToDaySignInBack(Constants.SigningInBack.SIGNING_BACK);
        if (!isSignBack) {
            return ApiResult.badParam("返还失败，还未签退");
        }
        if (!amountReturn.getStatus().equals(Constants.AmountReturnStatus.WAIT)) {
            return ApiResult.badParam("返还失败，业务返还已处理");
        }
        Byte tradeType = null;
        if (Constants.AmountReturnType.FEE.equals(amountReturn.getType())) {
            tradeType = FundTradeTypeEnum.FEE_SETTLEMENT.getCode();
        } else if (Constants.AmountReturnType.INTEGRAL.equals(amountReturn.getType())) {
            tradeType = FundTradeTypeEnum.INTEGRARL_SETTLEMENT.getCode();
        } else if (Constants.AmountReturnType.CREDIT.equals(amountReturn.getType())) {
            tradeType = FundTradeTypeEnum.CREDIT_SETTLEMENT_IN.getCode();
        }
        if (tradeType == null) {
            return ApiResult.badParam("类型错误");
        }
        if (amountReturn.getReturnAmount().compareTo(BigDecimal.ZERO) > 0) {
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(amountReturn.getCustomerNo());
            changeAmountDto.setOrderNo(amountReturn.getBatchNo());
            changeAmountDto.setChangeAmount(amountReturn.getReturnAmount());
            changeAmountDto.setTradeType(tradeType);
            changeAmountDto.setReType(Byte.valueOf("2"));
            // 增加资金流水
            accountService.changeAccount(changeAmountDto);
        }
        // 提货款需要从挂牌商扣除资金
        if (Constants.AmountReturnType.CREDIT.equals(amountReturn.getType())) {
            if (amountReturn.getReturnAmount().compareTo(BigDecimal.ZERO) > 0) {
                ChangeAmountDto changeAmountDto2 = new ChangeAmountDto();
                changeAmountDto2.setCustomerNo(hengjiaLister);
                changeAmountDto2.setOrderNo(amountReturn.getBatchNo());
                changeAmountDto2.setChangeAmount(amountReturn.getReturnAmount());
                changeAmountDto2.setTradeType(FundTradeTypeEnum.CREDIT_SETTLEMENT_OUT.getCode());
                changeAmountDto2.setReType(Byte.valueOf("1"));
                accountService.changeAccount(changeAmountDto2);
            }
        }
        // 修改状态
        amountReturn.setStatus(Constants.AmountReturnStatus.COMPLETED);
        amountReturn.setModifyTime(new Date());
        update(amountReturn);
        return ApiResult.success();
    }
}
