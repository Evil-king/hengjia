package com.baibei.hengjia.admin.modules.customer.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.customer.bean.dto.ChangeStatusDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerNoDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CouponAccountVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.PickUpDataVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.SigningDataVo;
import com.baibei.hengjia.admin.modules.customer.service.ICouponAccountService;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerAddressService;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerService;
import com.baibei.hengjia.admin.modules.withdraw.bean.dto.WithdrawListDto;
import com.baibei.hengjia.admin.modules.withdraw.bean.vo.WithdrawListVo;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/7/15 14:48
 * @description:
 */
@RestController
@RequestMapping("/admin/customer")
public class AdminCustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Autowired
    private ICouponAccountService couponAccountService;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER_ALL','CUSTOMER_LIST')")
    public ApiResult<MyPageInfo<CustomerVo>> pageList(CustomerDto customerDto){
        return ApiResult.success(customerService.pageList(customerDto));
    }

    @RequestMapping("/getSigningData")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER_ALL','SIGNING_DATA')")
    public ApiResult<List<SigningDataVo>> getSigningData(CustomerNoDto customerNoDto){
        return ApiResult.success(customerService.getSigningData(customerNoDto.getCustomerNo()));
    }

    @RequestMapping("/getPickUpData")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER_ALL','PICKUP_DATA')")
    public ApiResult<List<PickUpDataVo>> getPickUpData(CustomerNoDto customerNoDto){
        return ApiResult.success(customerAddressService.getPickUpData(customerNoDto.getCustomerNo()));
    }

    @RequestMapping("/changeStatus")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER_ALL','CHANGE_STATUS')")
    public ApiResult changeStatus(ChangeStatusDto changeStatusDto){
        return customerService.changeStatus(changeStatusDto);
    }
    @RequestMapping("/getCouponAccount")
    public ApiResult<List<CouponAccountVo>> getCouponAccount(CustomerNoDto customerNoDto){
        return ApiResult.success(couponAccountService.findProductByCustomerNo(customerNoDto.getCustomerNo()));
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER_ALL','CUSTOMER_EXPORT')")
    public void excelExport(CustomerDto customerDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<CustomerVo> customerVos = customerService.CustomerVoList(customerDto);
            for (int i = 0; i <customerVos.size() ; i++) {
                if(Constants.CustomerType.CUSTOMER.equals(customerVos.get(i).getCustomerType())){
                    customerVos.get(i).setCustomerType("普通用户");
                }else if(Constants.CustomerType.MEMBER.equals(customerVos.get(i).getCustomerType())){
                    customerVos.get(i).setCustomerType("挂牌商");
                }else if(Constants.CustomerType.DEALERS.equals(customerVos.get(i).getCustomerType())){
                    customerVos.get(i).setCustomerType("经销商");
                }
                if("1".equals(customerVos.get(i).getIsPickUp())){
                    customerVos.get(i).setIsPickUp("是");
                }else if("0".equals(customerVos.get(i).getIsPickUp())){
                    customerVos.get(i).setIsPickUp("否");
                }
            }
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, CustomerVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(customerVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
