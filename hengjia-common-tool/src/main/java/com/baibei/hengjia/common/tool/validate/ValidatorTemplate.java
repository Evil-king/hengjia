package com.baibei.hengjia.common.tool.validate;

import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 7:30 PM
 * @description: 校验器模板
 */
@Slf4j
public abstract class ValidatorTemplate {

    public void validate(ValidatorDataContext validatorDataContext) {
        try {
            validateInner(validatorDataContext);
        } catch (Exception e) {
            ServiceException serviceException;
            if (e instanceof ServiceException) {
                log.error("校验业务异常", e);
                serviceException = (ServiceException) e;
            } else {
                log.error("交易参数校验异常", e);
                serviceException = new ServiceException(ResultEnum.VALIDATE_ERROR);
            }
            throw serviceException;
        }

    }

    protected abstract void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException;
}
