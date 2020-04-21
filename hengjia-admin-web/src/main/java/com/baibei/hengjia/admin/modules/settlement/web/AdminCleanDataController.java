package com.baibei.hengjia.admin.modules.settlement.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanDataService;
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
@RequestMapping("/admin/cleanData")
public class AdminCleanDataController {
    @Autowired
    private ICleanDataService cleanDataService;


    /**
     * 分页列表
     *
     * @param cleanDataPageDto
     * @return
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','CLEANDATA_LIST','CLEANDATA_ALL')")
    public ApiResult<MyPageInfo<CleanDataPageVo>> pageList(CleanDataPageDto cleanDataPageDto) {
        return ApiResult.success(cleanDataService.pageList(cleanDataPageDto));
    }
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','CLEANDATA_EXPORT','CLEANDATA_ALL')")
    public void excelExport(CleanDataPageDto cleanDataPageDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<CleanDataPageVo> cleanDataPageVos = cleanDataService.CleanDataPageVoList(cleanDataPageDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, CleanDataPageVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(cleanDataPageVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
