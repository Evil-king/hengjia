package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 7:35 PM
 * @description: 开休市时间校验
 */
@Component
@Slf4j
public class TradeDayValidator extends ValidatorTemplate {
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("开休市时间校验正在执行...");
        boolean tradeDayFlag = tradeDayService.isTradeTime();
        if (!tradeDayFlag) {
            throw new ServiceException(ResultEnum.ISNOT_TRADE_DAY);
        }
    }
}
