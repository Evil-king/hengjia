package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.EntrustOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IEntrustOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/entrustOrder")
public class AdminEntrustOrderController {

    private final IEntrustOrderService entrustOrderService;

    public AdminEntrustOrderController(IEntrustOrderService entrustOrderService) {
        this.entrustOrderService = entrustOrderService;
    }

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','ENTRUSTORDER_ALL','ENTRUSTORDER_LIST')")
    public ApiResult<MyPageInfo<EntrustOrderVo>> pageList(EntrustOrderDto entrustOrderDto) {
        MyPageInfo<EntrustOrderVo> pageInfo = entrustOrderService.pageList(entrustOrderDto);
        return ApiResult.success(pageInfo);
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','ENTRUSTORDER_ALL','ENTRUSTORDER_EXPORT')")
    public void excelExport(EntrustOrderDto entrustOrderDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<EntrustOrderVo> entrustOrderVos = entrustOrderService.EntrustOrderVoList(entrustOrderDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, EntrustOrderVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(entrustOrderVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
