package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CustDzFailVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldDetailsService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 持仓明细
 */
@RestController
@RequestMapping("/admin/holdDetails")
public class AdminHoldDetailsController {

    private final IHoldDetailsService holdDetailsService;

    public AdminHoldDetailsController(IHoldDetailsService holdDetailsService) {
        this.holdDetailsService = holdDetailsService;
    }

    /**
     * 用户持仓汇总
     *
     * @return
     */
    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','HOLD_DETAILS')")
    public ApiResult<MyPageInfo<HoldDetailsVo>> pageList(HoldDetailsDto holdDetailsDto) {
        return ApiResult.success(holdDetailsService.pageList(holdDetailsDto));
    }

    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','USER_HOLD','HOLD_DETAILS_EXPORT')")
    public void excelExport(HoldDetailsDto holdDetailsDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<HoldDetailsVo> holdDetailsVos = holdDetailsService.HoldDetailsVoList(holdDetailsDto);
            for (int i = 0; i <holdDetailsVos.size() ; i++) {
                HoldDetailsVo holdDetailsVo=holdDetailsVos.get(i);
                if("1".equals(holdDetailsVo.getScanner())){
                    holdDetailsVo.setScanner("锁定");
                }else if("0".equals(holdDetailsVo.getScanner())){
                    holdDetailsVo.setScanner("解锁");
                }
                if("main".equals(holdDetailsVo.getHoldType())){
                    holdDetailsVo.setHoldType("零售商品");
                }else if("match".equals(holdDetailsVo.getHoldType())){
                    holdDetailsVo.setHoldType("折扣商品");
                }
                if("deal".equals(holdDetailsVo.getResource())){
                    holdDetailsVo.setResource("摘牌成交");
                }else if("plan".equals(holdDetailsVo.getResource())){
                    holdDetailsVo.setResource("配售");
                }else if("transfer".equals(holdDetailsVo.getResource())){
                    holdDetailsVo.setResource("非交易过户");
                }
            }
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, HoldDetailsVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(holdDetailsVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
