package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.model.CustomerTradeRisk;
import com.baibei.hengjia.api.modules.trade.service.ICustomerTradeRiskService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/3 10:34
 * @description: 客户买入交易风控
 */
@Component
@Slf4j
public class CustomerTradeRiskValidator extends ValidatorTemplate {
    @Autowired
    private ICustomerTradeRiskService customerTradeRiskService;
    // 被风控的提示语
    @Value("${customer.risk.notice}")
    private String customerRiskNotice;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户买入交易风控校验器正在执行...");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编号参数不存在");
        }
        Object type = validatorDataContext.getDataContextMap().get("type");
        if (type == null) {
            throw new ServiceException("挂摘牌类型不能为空");
        }
        CustomerTradeRisk customerTradeRisk = customerTradeRiskService.findByCustomerNo(customerNo.toString());
        // 不存在或者失效则直接通过
        if (customerTradeRisk == null || Constants.ValidStatus.UNVALID.equals(customerTradeRisk.getStatus())) {
            return;
        }
        String tradeParam = customerTradeRisk.getTradeParam();
        if (StringUtils.isEmpty(tradeParam) || tradeParam.length() != 5) {
            throw new ServiceException(customerRiskNotice);
        }
        Calendar calendar = Calendar.getInstance();
        int weekNow = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekNow == 6 || weekNow == 7) {
            log.info("当前时间为周{}，不予校验", weekNow);
            return;
        }
        char[] array = tradeParam.toCharArray();
        char status = array[calendar.get(Calendar.DAY_OF_WEEK) - 1 - 1];
        log.info("当前为周{}，客户{}交易状态为{}", (calendar.get(Calendar.DAY_OF_WEEK) - 1), customerNo.toString(), status);
        if (String.valueOf(status).equals("0")) {
            // 不允许摘牌买入
            if ("delist".equals(type.toString())) {
                throw new ServiceException(customerRiskNotice);
                // 允许挂牌买入，但是前端不可见
            } else {
                validatorDataContext.getDataContextMap().put("canShow", "0");
            }
        }
    }
}