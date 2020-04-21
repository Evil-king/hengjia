package com.baibei.hengjia.api.modules.user.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.user.bean.dto.AddressDto;
import com.baibei.hengjia.api.modules.user.bean.dto.FindAddressDto;
import com.baibei.hengjia.api.modules.user.bean.vo.AddressVo;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/6/4 16:38
 * @description:
 */
@Controller
@RequestMapping("/auth/api/customerAddress")
public class AuthApiCutomerAddressControllor {
    @Autowired
    private ICustomerAddressService customerAddressService;

    /**
     * 获取所有地址
     * @param customerNoDto
     * @return
     */
    @PostMapping("/getAllAddress")
    @ResponseBody
    public ApiResult<List<AddressVo>> getAllAddress(@RequestBody @Validated CustomerNoDto customerNoDto){
        List<AddressVo> addressVos=customerAddressService.getAllAddress(customerNoDto.getCustomerNo());
        return ApiResult.success(addressVos);
    }
    @PostMapping("/insertAddress")
    @ResponseBody
    public ApiResult insertAddress(@RequestBody @Validated AddressDto addressDto){

        return customerAddressService.insertAddress(addressDto);
    }
    @PostMapping("/updateAddress")
    @ResponseBody
    public ApiResult updateAddress(@RequestBody @Validated AddressDto addressDto){
        //先将原来的地址删掉
        customerAddressService.deleteAddress(addressDto.getId());
        //再重新插入一条数据
        customerAddressService.insertAddress(addressDto);
        return ApiResult.success();
    }

    @PostMapping("/findById")
    @ResponseBody
    public ApiResult<AddressVo> findById(@RequestBody @Validated FindAddressDto addressDto){
        CustomerAddress customerAddress=customerAddressService.findByAddressId(addressDto);
        return ApiResult.success(BeanUtil.copyProperties(customerAddress,AddressVo.class));
    }
}
