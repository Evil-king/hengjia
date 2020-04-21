package com.baibei.hengjia.admin.modules.settlement.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CustDzFailVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.FailResultVo;
import com.baibei.hengjia.admin.modules.settlement.service.IBatFailResultService;
import com.baibei.hengjia.admin.modules.settlement.service.ICustDzFailService;
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
 * @author: 会跳舞的机器人
 * @date: 2019/7/8 3:48 PM
 * @description:
 */
@RestController
@RequestMapping("/admin/cleanResult")
public class AdminCleanResultController {
    @Autowired
    private IBatFailResultService batFailResultService;
    @Autowired
    private ICustDzFailService custDzFailService;


    /**
     * 清算失败分页数据
     *
     * @param cleanResultDto
     * @return
     */
    @GetMapping("/failResult/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','FAILRESULT_LIST','FAILRESULT_ALL')")
    public ApiResult<MyPageInfo<FailResultVo>> failResult(CleanResultDto cleanResultDto) {
        return ApiResult.success(batFailResultService.pageList(cleanResultDto));
    }

    /**
     * 对账不平记录分页数据
     *
     * @param cleanResultDto
     * @return
     */
    @GetMapping("/custDzFail/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTDZFAIL_LIST','CUSTDZFAIL_ALL')")
    public ApiResult<MyPageInfo<CustDzFailVo>> pageList(CleanResultDto cleanResultDto) {
        return ApiResult.success(custDzFailService.pageList(cleanResultDto));
    }

    @PostMapping(path = "/failResult/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','FAILRESULT_EXPORT','FAILRESULT_ALL')")
    public void excelFailResultVoExport(CleanResultDto cleanResultDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<FailResultVo> failResultVos = batFailResultService.FailResultVoList(cleanResultDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, FailResultVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(failResultVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PostMapping(path = "/custDzFail/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','CUSTDZFAIL_EXPORT','CUSTDZFAIL_ALL')")
    public void excelCustDzFailVoExport(CleanResultDto cleanResultDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<CustDzFailVo> custDzFailVos = custDzFailService.CustDzFailVoList(cleanResultDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, CustDzFailVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(custDzFailVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
