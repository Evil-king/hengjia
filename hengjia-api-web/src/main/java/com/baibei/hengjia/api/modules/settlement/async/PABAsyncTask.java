package com.baibei.hengjia.api.modules.settlement.async;

import com.baibei.hengjia.api.modules.cash.bean.dto.ReconciliationMatchingDto;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 对账异步任务
 */
@Component
@Async
public class PABAsyncTask {

    private final Logger logger = LoggerFactory.getLogger(PABAsyncTask.class);


    /**
     * 发起出入金对账
     */
    public void withDrawDeposit(ICashService cashService) {
        logger.info("开启异步任务:出入金流水对账开始........................................");
        if (cashService != null) {
            ReconciliationMatchingDto reconciliationMatchingDto = new ReconciliationMatchingDto();
            reconciliationMatchingDto.setFuncFlag(Constants.PAB1006.WITHDRAW_DEPOSIT);
            reconciliationMatchingDto.setBeginDateTime(DateUtil.getBeginDay());
            reconciliationMatchingDto.setEndDateTime(DateUtil.getEndDay());
            cashService.reconciliationMatching(reconciliationMatchingDto);
            return;
        }
        logger.error("cashService 不能为空");
    }
}
