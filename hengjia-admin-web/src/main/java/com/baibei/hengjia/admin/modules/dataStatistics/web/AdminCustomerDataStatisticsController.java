package com.baibei.hengjia.admin.modules.dataStatistics.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.customer.bean.dto.ChangeStatusDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerNoDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.PickUpDataVo;
import com.baibei.hengjia.admin.modules.customer.bean.vo.SigningDataVo;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerService;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.CustomerIntegralDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/admin/dataStatistics")
public class AdminCustomerDataStatisticsController {
    @Autowired
    private ICustomerService customerService;

    @RequestMapping("/integralPageList")
    @PreAuthorize("hasAnyRole('ADMIN','INTEGRAL_DATASTATISTICS_ALL','INTEGRAL_DATASTATISTICS_LIST')")
    public ApiResult<MyPageInfo<CustomerIntegralVo>> integralPageList(CustomerIntegralDto customerIntegralDto){
        return ApiResult.success(customerService.integralPageList(customerIntegralDto));
    }

    @RequestMapping("/integralPageList/excelExport")
    @PreAuthorize("hasAnyRole('ADMIN','INTEGRAL_DATASTATISTICS_ALL','INTEGRAL_DATASTATISTICS_EXPORT')")
    public void excelExport(CustomerIntegralDto customerIntegralDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<CustomerIntegralVo> customerIntegralVos = customerService.integralVoList(customerIntegralDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, CustomerIntegralVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(customerIntegralVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
