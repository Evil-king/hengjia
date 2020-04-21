package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.trade.service.IAutoWhiteListService;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/9 19:05
 * @description:
 */
@Component
@Slf4j
public class AutoWhiteListValidator extends ValidatorTemplate {
    @Autowired
    private IAutoWhiteListService autoWhiteListService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("智能购销白名单校验器正在执行...");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编号参数不存在");
        }
        boolean whiteFlag = autoWhiteListService.isWhiteList(customerNo.toString());
        // 非白名单用户不允许卖出
        if (!whiteFlag) {
            throw new ServiceException("不允许卖出");
        }
    }
}