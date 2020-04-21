package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 2:44 PM
 * @description: 客户地址校验器
 */
@Component
@Slf4j
public class DeListCustomerAddressValidator extends ValidatorTemplate {
    @Autowired
    private ICustomerAddressService customerAddressService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户地址校验器正在执行...");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编码不存在");
        }
        List<CustomerAddress> list = customerAddressService.findByCustomerNo(customerNo.toString());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.NO_ADDRESS);
        }
    }
}
