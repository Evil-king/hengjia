package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.MapUtil;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 8:46 PM
 * @description: 客户交易状态校验器
 */
@Component
@Slf4j
public class CustomerTradeStatusValidator extends ValidatorTemplate {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("客户交易状态校验器正在执行...");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编号参数不存在");
        }
        Object direction = validatorDataContext.getDataContextMap().get("direction");
        String customerStatus;
        Byte customerType;
        // 先从缓存取
       /* String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customerNo.toString());
        Map<String, Object> map = redisUtil.hgetAll(key);
        if (!CollectionUtils.isEmpty(map)) {
            CustomerVo customerVo = (CustomerVo) MapUtil.mapToObject(map, CustomerVo.class);
            customerStatus = customerVo.getCustomerStatus();
            customerType = customerVo.getCustomerType();
            // 从用户服务取
        } else {
            CustomerVo customerVo = customerService.findUserByCustomerNo(customerNo.toString());
            customerStatus = customerVo.getCustomerStatus();
            customerType = customerVo.getCustomerType();
        }*/
        CustomerVo customerVo = customerService.findUserByCustomerNo(customerNo.toString());
        customerStatus = customerVo.getCustomerStatus();
        customerType = customerVo.getCustomerType();
        if (Constants.TradeDirection.BUY.equals(direction.toString())) {
            if (isInStatus(customerStatus,CustomerStatusEnum.LIMIT_BUY.getCode())) {
                throw new ServiceException(ResultEnum.CUSTOMER_NOT_TRADE);
            }
            // 挂牌商超级用户不允许买入交易
            if (customerType.intValue() == 2) {
                throw new ServiceException(ResultEnum.CUSTOMER_NOT_TRADE);
            }
        } else {
            if (isInStatus(customerStatus,CustomerStatusEnum.LIMIT_SELL.getCode())) {
                throw new ServiceException(ResultEnum.CUSTOMER_NOT_TRADE);
            }
            // 挂牌商超级用户不允许卖出交易
            if (customerType.intValue() == 2) {
                throw new ServiceException(ResultEnum.CUSTOMER_NOT_TRADE);
            }
        }

    }

    public static boolean isInStatus(String status, int val) {
        List<String> list = Arrays.asList(status.split(","));
        return list.contains(val + "");
    }
}
