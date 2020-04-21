package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordIntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordIntegralVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordIntegralService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
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
 * @date: 2019/7/15 14:23
 * @description:
 */
@RestController
@RequestMapping("/admin/recordIntegral")
public class AdminRecordIntegralController {
    @Autowired
    private IRecordIntegralService recordIntegralService;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','RECORD_INTEGRAL_ALL','RECORD_INTEGRAL_LIST')")
    public ApiResult<MyPageInfo<RecordIntegralVo>> pageList(RecordIntegralDto recordIntegralDto) {
        return ApiResult.success(recordIntegralService.pageList(recordIntegralDto));
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','RECORD_INTEGRAL_ALL','RECORD_INTEGRAL_EXPORT')")
    public void excelExport(RecordIntegralDto recordIntegralDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<RecordIntegralVo> recordIntegralVos = recordIntegralService.RecordIntegralVoList(recordIntegralDto);
            for (int i = 0; i <recordIntegralVos.size() ; i++) {
                if("1".equals(recordIntegralVos.get(i).getRetype())){
                    recordIntegralVos.get(i).setRetype("支出");
                }else if("2".equals(recordIntegralVos.get(i).getRetype())){
                    recordIntegralVos.get(i).setRetype("收入");
                }
            }
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, RecordIntegralVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(recordIntegralVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
