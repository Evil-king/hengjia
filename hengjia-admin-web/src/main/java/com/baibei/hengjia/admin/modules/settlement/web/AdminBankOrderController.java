package com.baibei.hengjia.admin.modules.settlement.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
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
 * @author: Longer
 * @date: 2019/7/15 11:38 AM
 * @description:银行出入金流水
 */
@RestController
@RequestMapping("/admin/bankOrder")
public class AdminBankOrderController {
    @Autowired
    private IBankOrderService bankOrderService;


    /**
     * 分页列表
     * @param bankOrderDto
     * @return
     */
    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','BANKORDER')")
    public ApiResult<MyPageInfo<BankOrderVo>> pageList(BankOrderDto bankOrderDto) {
        return ApiResult.success(bankOrderService.pageList(bankOrderDto));
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','BANKORDER','BANKORDER_EXPORT')")
    public void excelExport(BankOrderDto bankOrderDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<BankOrderVo> bankOrderVos = bankOrderService.BankOrderVoList(bankOrderDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, BankOrderVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(bankOrderVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
