package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.bean.dto.*;
import com.baibei.hengjia.api.modules.cash.bean.vo.*;
import com.baibei.hengjia.api.modules.settlement.bean.dto.BeginCleanDto;
import com.baibei.hengjia.api.modules.settlement.bean.vo.BeginCleanVo;
import com.baibei.hengjia.common.tool.api.ApiResult;

/**
 *
 */
public interface ICashService {

    /**
     * 签约服务
     *
     * @param signingRecordDto
     * @return
     */
    ApiResult<SigningRecordVo> signingService(SigningRecordDto signingRecordDto);

    /**
     * 入金服务
     *
     * @param orderDepositDto
     * @return
     */
    ApiResult<OrderDepositVo> depositDto(OrderDepositDto orderDepositDto);


    /**
     * 签到或签退
     *
     * @param signInBackDto
     * @return
     */
    ApiResult<SignInBackVo> signInBack(SignInBackDto signInBackDto);

    /**
     * 发起出入金流水对账及会员开销户流水匹配
     */
    ApiResult<ReconciliationMatchingVo> reconciliationMatching(ReconciliationMatchingDto reconciliationMatchingDto);

    /**
     * 发起清算请求
     */
    ApiResult<BeginCleanVo> beginClean(BeginCleanDto beginCleanDto);

    /**
     * 查看清算文件进度
     */
    ApiResult<FilePlannedSpeedVo> filePlannedSpeed(FilePlannedSpeedDto filePlannedSpeedDto);
}
