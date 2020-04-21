package com.baibei.hengjia.admin.modules.settlement.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.WithDrawDepositDiffDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.WithDrawDepositDiffVo;
import com.baibei.hengjia.admin.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.admin.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Longer
 * @date: 2019/7/15 11:38 AM
 * @description:出入金差异表
 */
@RestController
@RequestMapping("/admin/withDrawDepositDiff")
public class AdminWithDrawDepositDiffController {
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;


    /**
     * 分页列表
     * @param withDrawDepositDiffDto
     * @return
     */
    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','DIFFLIST_ALL','DIFFLIST')")
    public ApiResult<MyPageInfo<WithDrawDepositDiffVo>> pageList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
        return ApiResult.success(withDrawDepositDiffService.pageList(withDrawDepositDiffDto));
    }

    @PostMapping("/dealDiff")
    @PreAuthorize("hasAnyRole('ADMIN','DIFFLIST_ALL','DEALDIFF')")
    public ApiResult dealDiff(Long id){
        return  withDrawDepositDiffService.dealDiff(id);
    }

    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','DIFFLIST_ALL','DIFFLIST_EXPORT')")
    public void excelExport(WithDrawDepositDiffDto withDrawDepositDiffDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<WithDrawDepositDiffVo> withDrawDepositDiffVos = withDrawDepositDiffService.WithDrawDepositDiffVoList(withDrawDepositDiffDto);
            for (int i = 0; i <withDrawDepositDiffVos.size() ; i++) {
                if("withdraw".equals(withDrawDepositDiffVos.get(i).getType())){
                    withDrawDepositDiffVos.get(i).setType("出金");
                }else if("deposit".equals(withDrawDepositDiffVos.get(i).getType())){
                    withDrawDepositDiffVos.get(i).setType("入金");
                }

                if("1".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金申请中");
                }else if("2".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金审核通过");
                }else if("3".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金处理中");
                }else if("4".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金成功");
                }else if("5".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金失败");
                }else if("6".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("出金审核失败");
                }else if("success".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("入金成功");
                }else if("fail".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                    withDrawDepositDiffVos.get(i).setHengjiaStatus("入金失败");
                }
                if("success".equals(withDrawDepositDiffVos.get(i).getBankStatus())){
                    withDrawDepositDiffVos.get(i).setBankStatus("成功");
                }

                if("long_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                    withDrawDepositDiffVos.get(i).setDiffType("长款差错");
                }else if("short_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                    withDrawDepositDiffVos.get(i).setDiffType("短款差错");
                }else if("amount_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                    withDrawDepositDiffVos.get(i).setDiffType("金额不一致");
                }else if("status_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                    withDrawDepositDiffVos.get(i).setDiffType("状态不一致");
                }else if("amount_status_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                    withDrawDepositDiffVos.get(i).setDiffType("金额和状态不一致");
                }

                if("wait".equals(withDrawDepositDiffVos.get(i).getStatus())){
                    withDrawDepositDiffVos.get(i).setStatus("待处理");
                }else if("deal".equals(withDrawDepositDiffVos.get(i).getStatus())){
                    withDrawDepositDiffVos.get(i).setStatus("已处理");
                }
            }
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, WithDrawDepositDiffVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(withDrawDepositDiffVos, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
