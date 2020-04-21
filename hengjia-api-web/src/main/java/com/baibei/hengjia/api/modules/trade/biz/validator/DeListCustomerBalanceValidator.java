package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeDeListDto;
import com.baibei.hengjia.api.modules.trade.model.EntrustOrder;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/5 5:15 PM
 * @description: 摘牌客户余额校验器
 */
@Component
@Slf4j
public class DeListCustomerBalanceValidator extends ValidatorTemplate {
    @Autowired
    private IAccountService accountService;


    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户余额校验器正在执行...");
        TradeDeListDto tradeDeListDto = (TradeDeListDto) validatorDataContext.getRequestData();
        Account account = accountService.checkAccount(tradeDeListDto.getCustomerNo());
        if (account == null) {
            throw new ServiceException("账户不存在");
        }
        EntrustOrder entrustOrder = (EntrustOrder) validatorDataContext.getDataContextMap().get("entrustOrder");
        if (entrustOrder == null) {
            throw new ServiceException("委托单不存在");
        }
        BigDecimal money = NumberUtil.mul(entrustOrder.getPrice().toPlainString(), tradeDeListDto.getCount().toString());
        if (account.getBalance().compareTo(money) < 0) {
            log.info("balance={},money={}", account.getBalance(), money);
            throw new ServiceException("账户余额不足");
        }
    }
}
