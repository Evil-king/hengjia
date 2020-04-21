package com.baibei.hengjia.admin.modules.tradingQuery.web;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.hengjia.admin.modules.paramConfiguration.service.ILookUpService;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DeliveryDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.IntegralDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryExportVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDeliveryService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.ExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 成交单查询
 */
@Slf4j
@RestController
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {

    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private ILookUpService lookUpService;

    /**
     * 分页列表数据
     *
     * @param deliveryDto
     * @return
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_ALL','DELIVERY_LIST')")
    public ApiResult<MyPageInfo<DeliveryVo>> pageList(DeliveryDto deliveryDto) {
        return ApiResult.success(deliveryService.pageList(deliveryDto));
    }


    /**
     * 添加物流信息
     *
     * @param deliveryDto
     * @return
     */
    @PostMapping(path = "/addLogisticsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_ALL','DELIVERY_LOGISTICS')")
    public ApiResult addLogisticsInfo(@RequestBody DeliveryDto deliveryDto) {

        if (deliveryDto.getDeliveryNo() == null) {
            return ApiResult.error("添加物流信息");
        }
        if (deliveryDto.getDeliveryStatus().equals(Constants.DeliveryStatus.RECEIVED)) { //如果已经收货无法编辑了
            return ApiResult.error("已经收货，无需添加物流信息");
        }
        deliveryService.addLogisticsInfo(deliveryDto);
        return ApiResult.success();
    }


    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_EXPORT')")
    public void excelExport(DeliveryDto deliveryDto, HttpServletResponse response) {
        ServletOutputStream out;
        try {
            List<DeliveryExportVo> list = deliveryService.list(deliveryDto);
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, DeliveryExportVo.class);
            sheet1.setSheetName("sheet1");
            writer.write(list, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @PostMapping(path = "/excelImport")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_IMPORT')")
    public ApiResult excelImport(@RequestParam("file") MultipartFile file) {
        InputStream inputStream = null;
        String confirmSend = "";
        try {
            inputStream = file.getInputStream();
            //实例化实现了AnalysisEventListener接口的类
            ExcelListener listener = new ExcelListener();
            //传入参数
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener);
            //读取信息
            try {
                excelReader.read(new Sheet(1, 0, IntegralDto.class));
            } catch (ExcelAnalysisException e) {
                return ApiResult.error("文件类型只支持.XLSX");
            }
            //获取数据
            List<Object> list = listener.getDatas();
            if (list.size() > 10000) {
                return ApiResult.error("导入数据量过大");
            }

            //转换数据类型,并插入到数据库
            for (int i = 1; i < list.size(); i++) {
                IntegralDto integralDto = (IntegralDto) list.get(i);
                if (StringUtils.isEmpty(integralDto.getLogisticsNo())
                        || StringUtils.isEmpty(integralDto.getLogisticsCompany())
                        || StringUtils.isEmpty(integralDto.getOrderNo())) {
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
                if(deliveryService.selectByOrderNoExist(integralDto.getOrderNo()) == 0){
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
                if (integralDto.getLogisticsNo().indexOf(" ") != -1) {
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
                if (integralDto.getLogisticsCompany().indexOf(" ") != -1) {
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
                if (lookUpService.selectByName(integralDto.getLogisticsCompany()) == 0) {
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
                confirmSend = deliveryService.send(integralDto.getOrderNo(), integralDto.getLogisticsCompany(), integralDto.getLogisticsNo());
                if ("fail".equals(confirmSend)) {
                    log.info("导入订单有误,orderNo={}", integralDto.getOrderNo());
                    return ApiResult.error("第"+i+"行数据错误，上传失败，请检查后重新上传");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.success();
    }

}
