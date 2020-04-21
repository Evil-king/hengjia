package com.baibei.hengjia.admin.modules.withdraw.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.withdraw.bean.dto.WithdrawListDto;
import com.baibei.hengjia.admin.modules.withdraw.bean.vo.WithdrawListVo;
import com.baibei.hengjia.admin.modules.withdraw.service.IOrderWithdrawService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/withdraw")
public class AdminWithdrawController {

    @Autowired
    private IOrderWithdrawService withdrawService;


    /**
     * 分页列表数据
     *
     * @param withdrawListDto
     * @return
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','WITHDRAW_ALL','WITHDRAW_LIST')")
    public ApiResult<MyPageInfo<WithdrawListVo>> pageList(WithdrawListDto withdrawListDto) {
        return ApiResult.success(withdrawService.pageList(withdrawListDto));
    }

    /**
     * 后台出金审核接口
     *
     * @param orderNo
     * @param type
     * @return
     */
    @GetMapping("/reviewStatus")
    @PreAuthorize("hasAnyRole('ADMIN','REVEIW')")
    public ApiResult withdraw(String orderNo, String type) {
        return withdrawService.reviewStatus(orderNo, type);
    }


    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','WITHDRAW_EXPORT')")
    public void excelExport(WithdrawListDto withdrawListDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<WithdrawListVo> list = withdrawService.list(withdrawListDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, WithdrawListVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(list, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
