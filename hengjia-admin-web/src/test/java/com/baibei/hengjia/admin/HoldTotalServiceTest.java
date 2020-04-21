package com.baibei.hengjia.admin;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.HoldTotalMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldDetailsService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.CSVUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminWebServiceApplication.class)
public class HoldTotalServiceTest {

    @Autowired
    private IHoldTotalService holdTotalService;

    @Autowired
    private IHoldDetailsService holdDetailsService;

    @Autowired
    private HoldTotalMapper holdTotalMapper;

    @Test
    public void testHoldPageList() {
        HoldTotalDto holdTotalDto = new HoldTotalDto();
        holdTotalDto.setCustomerNo("1359");
        holdTotalDto.setCurrentPage(1);
        holdTotalDto.setPageSize(1);
        MyPageInfo<HoldTotalVo> pageInfo = holdTotalService.pageList(holdTotalDto);
        pageInfo.getList().stream().forEach(x -> {
            System.out.println(JSONObject.toJSONString(x));
        });
        Assert.assertEquals(holdTotalDto.getCurrentPage() * holdTotalDto.getPageSize(), pageInfo.getTotal());
    }

    @Test
    public void testDetailsPageList() {
        HoldDetailsDto holdDetailsDto = new HoldDetailsDto();
        holdDetailsDto.setPageSize(5);
        holdDetailsDto.setCurrentPage(1);
        holdDetailsDto.setCustomerNo("1359");
        MyPageInfo<HoldDetailsVo> pageInfo = holdDetailsService.pageList(holdDetailsDto);
        pageInfo.getList().stream().forEach(x -> {
            System.out.println(JSONObject.toJSONString(x));
        });
        Assert.assertEquals(holdDetailsDto.getCurrentPage() * holdDetailsDto.getPageSize(), pageInfo.getTotal());
    }

    @Test
    public void tesExcelExport() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\持仓明细.xlsx");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        HoldTotalDto dto = new HoldTotalDto();
        List<HoldTotalVo> holdTotalList = holdTotalMapper.findUserByHoldTotal(dto);
        try {
            out = new FileOutputStream(file);
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0, HoldTotalVo.class);
            sheet1.setSheetName("第一个sheet");
            writer.write(holdTotalList, sheet1);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testCSVFileExport() {
        HoldTotalDto dto = new HoldTotalDto();
        List<HoldTotalVo> holdTotalList = holdTotalMapper.findUserByHoldTotal(dto);
        File file = new File("C:\\Users\\Administrator\\Desktop\\持仓明细.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            CSVUtil csvUtil = new CSVUtil(out, HoldTotalVo.class);
            csvUtil.write(holdTotalList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



