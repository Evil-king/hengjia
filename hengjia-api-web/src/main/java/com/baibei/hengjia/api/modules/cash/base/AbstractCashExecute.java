package com.baibei.hengjia.api.modules.cash.base;

import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 */
public abstract class AbstractCashExecute implements ICashService {

    @Autowired
    private List<ICashFunctionService> cashFunctionServices;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    protected ICashFunctionService getFunction(CashFunctionType type) {
        logger.info("当前的请求类型为{}", type.getName());
        return cashFunctionServices.stream().filter(t -> t.getType() == type).findFirst().orElse(null);
    }

    protected <T extends BaseRequest, V extends BaseResponse> ApiResult<V> execute(T request, CashFunctionType type) {
        ICashFunctionService function = this.getFunction(type);
        if (function == null) {
            return ApiResult.error("系统错误,请稍后再试");
        }
        return function.request(request);
    }
}
