package com.baibei.hengjia.api.modules.account.service;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.bean.dto.IntegralDetailDto;
import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.account.model.CustomerIntegral;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;


/**
* @author: hyc
* @date: 2019/06/03 14:40:48
* @description: CustomerIntegral服务接口
*/
public interface ICustomerIntegralService extends Service<CustomerIntegral> {
    /**
     * 修改积分
     * @param changeIntegralDto
     * @throws ServiceException
     */
    void changeIntegral(ChangeIntegralDto changeIntegralDto);


    /**
     * 修改积分（扣减时允许穿仓）
     * @param changeIntegralDto
     * @throws ServiceException
     */
    ApiResult changeIntegralSpecil(ChangeIntegralDto changeIntegralDto);

    IntegralDetailVo findIntegralDetailByCustomer(IntegralDetailDto integralDetailDto) ;
}
