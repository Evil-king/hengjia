package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.CSVUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户持有
 */
@RestController
@RequestMapping("/admin/holdTotal")
public class AdminHoldTotalController {

    private final IHoldTotalService holdTotalService;

    public AdminHoldTotalController(IHoldTotalService holdTotalService) {
        this.holdTotalService = holdTotalService;
    }


    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','USER_HOLD','HOLD_DETAILS')")
    public ApiResult<MyPageInfo<HoldTotalVo>> pageList(HoldTotalDto holdTotalDto) {
        return ApiResult.success(holdTotalService.pageList(holdTotalDto));
    }

    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','USER_HOLD','USER_HOLD_EXPORT')")
    public void excelExport(HoldTotalDto holdTotalDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<HoldTotalVo> holdTotalList = holdTotalService.findUserByHoldTotalService(holdTotalDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, HoldTotalVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(holdTotalList, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出csv
     *
     * @param holdTotalDto
     * @param response
     * @throws IOException
     */
    @PostMapping(path = "/csvExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void csvExport(HoldTotalDto holdTotalDto, HttpServletResponse response) throws IOException {
        List<HoldTotalVo> holdTotalList = holdTotalService.findUserByHoldTotalService(holdTotalDto);
        CSVUtil csvUtil = new CSVUtil(response.getOutputStream(), HoldTotalVo.class);
        csvUtil.write(holdTotalList);
    }

}
