package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordMoneyService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/7/15 10:34
 * @description:
 */
@RestController
@RequestMapping("/admin/recordMoney")
public class AdminRecordMoneyController {
    @Autowired
    private IRecordMoneyService recordMoneyService;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','RECORD_MONEY_ALL','RECORD_MONEY_LIST')")
    public ApiResult<MyPageInfo<RecordMoneyVo>> pageList(RecordMoneyDto recordMoneyDto) {
        return ApiResult.success(recordMoneyService.pageList(recordMoneyDto));
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','RECORD_MONEY_ALL','RECORD_MONEY_EXPORT')")
    public void excelExport(RecordMoneyDto recordMoneyDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<RecordMoneyVo> recordMoneyVos = recordMoneyService.RecordMoneyVoList(recordMoneyDto);
            for (int i = 0; i <recordMoneyVos.size() ; i++) {
                if("1".equals(recordMoneyVos.get(i).getRetype())){
                    recordMoneyVos.get(i).setRetype("支出");
                }else if("2".equals(recordMoneyVos.get(i).getRetype())){
                    recordMoneyVos.get(i).setRetype("收入");
                }
            }
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, RecordMoneyVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(recordMoneyVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
