package com.baibei.hengjia.admin.modules.dataStatistics.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.CustomerIntegralDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.FundDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.FundDataStatisticsVo;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/22 13:43
 * @description:
 */
@RestController
@RequestMapping("/admin/dataStatistics")
public class AdminFundStatisticsController {
    @Autowired
    private ICleanDataService cleanDataService;

    @RequestMapping("/fundPageList")
    @PreAuthorize("hasAnyRole('ADMIN','MONEY_DATASTATISTICS_ALL','MONEY_DATASTATISTICS_LIST')")
    public ApiResult<MyPageInfo<FundDataStatisticsVo>> fundPageList(FundDataStatisticsDto fundDataStatisticsDto){
        return ApiResult.success(cleanDataService.fundPageList(fundDataStatisticsDto));
    }
    @RequestMapping("/fundStatistics/excelExport")
    @PreAuthorize("hasAnyRole('ADMIN','MONEY_DATASTATISTICS_ALL','MONEY_DATASTATISTICS_EXPORT')")
    public void excelExport(FundDataStatisticsDto fundDataStatisticsDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<FundDataStatisticsVo> FundDataStatisticsVos = cleanDataService.fundDataStatisticsVoList(fundDataStatisticsDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, FundDataStatisticsVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(FundDataStatisticsVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
