package com.baibei.hengjia.admin.modules.customer.service;
import com.baibei.hengjia.admin.modules.customer.bean.dto.ChangeStatusDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.SigningDataVo;
import com.baibei.hengjia.admin.modules.customer.model.Customer;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.CustomerIntegralDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: hyc
* @date: 2019/07/15 10:07:43
* @description: Customer服务接口
*/
public interface ICustomerService extends Service<Customer> {
    /**
     * 用户列表
     * @param customerDto
     * @return
     */
    MyPageInfo<CustomerVo> pageList(CustomerDto customerDto);

    /**
     * 用户列表无分页
     * @param customerDto
     * @return
     */
    List<CustomerVo> CustomerVoList(CustomerDto customerDto);

    /**
     * 查看签约信息
     * @param customerNo
     * @return
     */
    List<SigningDataVo> getSigningData(String customerNo);

    ApiResult changeStatus(ChangeStatusDto changeStatusDto);

    /**
     * 用户积分以及手续费统计
     * @param customerIntegralDto
     * @return
     */
    MyPageInfo<CustomerIntegralVo> integralPageList(CustomerIntegralDto customerIntegralDto);

    /**
     * 查询列表
     * @param customerIntegralDto
     * @return
     */
    List<CustomerIntegralVo> integralVoList(CustomerIntegralDto customerIntegralDto);
}
