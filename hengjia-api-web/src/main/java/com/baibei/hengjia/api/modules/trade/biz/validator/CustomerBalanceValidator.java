package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
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
 * @date: 2019/5/27 8:52 PM
 * @description: 客户余额校验器
 */
@Component
@Slf4j
public class CustomerBalanceValidator extends ValidatorTemplate {
    @Autowired
    private IAccountService accountService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户余额校验器正在执行...");
        TradeListDto tradeListDto = (TradeListDto) validatorDataContext.getRequestData();
        Account account = accountService.checkAccount(tradeListDto.getCustomerNo());
        if (account == null) {
            throw new ServiceException("账户不存在");
        }
        BigDecimal money = NumberUtil.mul(tradeListDto.getPrice(), tradeListDto.getCount().toString());
        if (account.getBalance().compareTo(money) < 0) {
            log.info("balance={},money={}", account.getBalance(), money);
            throw new ServiceException("账户余额不足");
        }
    }
}
