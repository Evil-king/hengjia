package com.baibei.hengjia.admin.modules.dataStatistics.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.DealOrderDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.FundDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.DealOrderDataStatisticsVo;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.FundDataStatisticsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDealOrderService;
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
 * @date: 2019/8/27 13:38
 * @description:
 */
@RestController
@RequestMapping("/admin/dataStatistics")
public class AdminDealOrderDataStatisticsController {
    @Autowired
    private IDealOrderService dealOrderService;

    @RequestMapping("/dealOrderPageList")
    @PreAuthorize("hasAnyRole('ADMIN','DEALORDER_DATASTATISTICS_ALL','DEALORDER_DATASTATISTICS_LIST')")
    public ApiResult<MyPageInfo<DealOrderDataStatisticsVo>> dealOrderPageList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto){
        return ApiResult.success(dealOrderService.dealOrderPageList(dealOrderDataStatisticsDto));
    }
    @RequestMapping("/dealOrderDataStatistics/excelExport")
    @PreAuthorize("hasAnyRole('ADMIN','DEALORDER_DATASTATISTICS_ALL','DEALORDER_DATASTATISTICS_EXPORT')")
    public void excelExport(DealOrderDataStatisticsDto dealOrderDataStatisticsDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVos = dealOrderService.dealOrderDataStatisticsVoList(dealOrderDataStatisticsDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, DealOrderDataStatisticsVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(dealOrderDataStatisticsVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
