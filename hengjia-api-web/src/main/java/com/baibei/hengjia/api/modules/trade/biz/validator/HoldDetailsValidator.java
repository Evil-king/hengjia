package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.utils.TradeUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/16 13:57
 * @description:
 */
@Component
@Slf4j
public class HoldDetailsValidator extends ValidatorTemplate {
    @Autowired
    private IHoldDetailsService holdDetailsService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("持有明细校验器正在执行");
        Object detailsId = validatorDataContext.getDataContextMap().get("detailsId");
        if (detailsId == null) {
            throw new ServiceException("持有明细ID不存在");
        }
        HoldDetails holdDetails = holdDetailsService.findById(Long.valueOf(detailsId.toString()));
        if (holdDetails == null) {
            log.info("持有记录不存在，detailsId={}", detailsId);
            throw new ServiceException("持有不存在");
        }
        if (holdDetails.getRemaindCount() == null || holdDetails.getRemaindCount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("持有剩余数量小于等于0，detailsId={}，");
            throw new ServiceException("持有剩余数量小于等于0");
        }
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编码不存在");
        }
        if (!holdDetails.getCustomerNo().equals(customerNo.toString())) {
            log.info("当前客户编号为{}，持仓归属客户编号为{}", customerNo.toString(), holdDetails.getCustomerNo());
            throw new ServiceException("持仓归属客户错误");
        }
        if (new Date().getTime() < holdDetails.getTradeTime().getTime() || holdDetails.getScanner().intValue() == 0) {
            log.info("持仓未解锁，detailsId为{}，解锁时间为{}", detailsId, holdDetails.getTradeTime());
            throw new ServiceException("持仓未解锁");
        }
        validatorDataContext.getDataContextMap().put("holdDetails", holdDetails);
    }
}