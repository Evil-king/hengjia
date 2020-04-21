package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/3 4:08 PM
 * @description: 是否签约校验器
 */
@Component
@Slf4j
public class SignValidator extends ValidatorTemplate {
    @Autowired
    private ISigningRecordService signingRecordService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("签约校验器正在执行...");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编号参数不存在");
        }
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo.toString());
        if (signingRecord == null) {
            throw new ServiceException(ResultEnum.NOT_SIGN);
        }
    }
}
