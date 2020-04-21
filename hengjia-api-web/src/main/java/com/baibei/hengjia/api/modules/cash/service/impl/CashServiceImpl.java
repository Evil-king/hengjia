package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.cash.base.AbstractCashExecute;
import com.baibei.hengjia.api.modules.cash.bean.dto.*;
import com.baibei.hengjia.api.modules.cash.bean.vo.*;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.settlement.bean.dto.BeginCleanDto;
import com.baibei.hengjia.api.modules.settlement.bean.vo.BeginCleanVo;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CashServiceImpl extends AbstractCashExecute {

    private final Logger logger = LoggerFactory.getLogger(CashServiceImpl.class);

    @Autowired
    private ITradeDayService tradeDayService;

    @Value("${verify.tradeDay}")
    private Boolean verifyTradeDay;

    @Override
    public ApiResult<SigningRecordVo> signingService(SigningRecordDto signingRecordDto) {
        return super.execute(signingRecordDto, CashFunctionType.SIGNING);
    }

    @Override
    public ApiResult<OrderDepositVo> depositDto(OrderDepositDto orderDepositDto) {
        return super.execute(orderDepositDto, CashFunctionType.DEPOSIT);
    }


    @Override
    public ApiResult<SignInBackVo> signInBack(SignInBackDto signInBackDto) {
        if (verifyTradeDay) { //开启交易日验证
            if (!tradeDayService.isTradeDay(new Date())) {
                logger.error("今天不是交易日,无法进行签到或者签退");
                return ApiResult.error("今天不是交易日,无法进行签到或者签退");
            }
        }
        return super.execute(signInBackDto, CashFunctionType.SIGN_IN_BACK);
    }

    @Override
    public ApiResult<ReconciliationMatchingVo> reconciliationMatching(ReconciliationMatchingDto reconciliationMatchingDto) {
        return super.execute(reconciliationMatchingDto, CashFunctionType.DEPOSIT_WITHDRAW_MEMBERS_DETAIL);
    }

    @Override
    public ApiResult<BeginCleanVo> beginClean(BeginCleanDto beginCleanDto) {
        return super.execute(beginCleanDto, CashFunctionType.BEGIN_CLEAN);
    }

    @Override
    public ApiResult<FilePlannedSpeedVo> filePlannedSpeed(FilePlannedSpeedDto filePlannedSpeedDto) {
        return super.execute(filePlannedSpeedDto, CashFunctionType.FIND_FILE_PLANNED_SPEED);
    }


}
