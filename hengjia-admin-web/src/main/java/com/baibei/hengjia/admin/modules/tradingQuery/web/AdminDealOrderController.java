package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DealOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DealOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDealOrderService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 成交单查询
 */
@RestController
@RequestMapping("/admin/dealOrder")
public class AdminDealOrderController {

    @Autowired
    private IDealOrderService dealOrderService;

    /**
     * 分页列表数据
     *
     * @param dealOrderDto
     * @return
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','DEALORDER_ALL','DEALORDER_LIST')")
    public ApiResult<MyPageInfo<DealOrderVo>> pageList(DealOrderDto dealOrderDto) {
        return ApiResult.success(dealOrderService.pageList(dealOrderDto));
    }

    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','DEALORDER_ALL','DEALORDER_EXPORT')")
    public void excelExport(DealOrderDto dealOrderDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<DealOrderVo> list = dealOrderService.list(dealOrderDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, DealOrderVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(list, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
