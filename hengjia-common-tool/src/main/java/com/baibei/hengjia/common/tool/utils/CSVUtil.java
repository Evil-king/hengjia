package com.baibei.hengjia.common.tool.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.util.TypeUtil;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import org.apache.commons.csv.CSVFormat;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

/**
 * 根据阿里云的excel导出进行附加csv文件导出
 *
 * @author uqing
 */
public class CSVUtil {

    private CSVPrinter csvPrinter;

    private ExcelHeadProperty excelHead;

    private final Logger logger = LoggerFactory.getLogger(CSVUtil.class);

    public CSVUtil(OutputStream out, Class<? extends BaseRowModel> head) {
        this(out, head, null);
    }

    public CSVUtil(OutputStream out, Class<? extends BaseRowModel> head, CSVFormat csvFormat) {
        excelHead = new ExcelHeadProperty(head, null);
        if (csvFormat == null) {
            csvFormat = CSVFormat.DEFAULT;
        }
        if (excelHead.getHead().size() == 0) {
            logger.info("表头不能为空");
            throw new ServiceException("表头不能为空");
        }
        initHead(csvFormat, out);
    }

    /**
     * 初始化头部
     *
     * @param csvFormat
     * @param out
     */
    public void initHead(CSVFormat csvFormat, OutputStream out) {
        List<String> headList = new ArrayList<>();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(out, "GBK");
            for (List<String> result : excelHead.getHead()) {
                if (result.size() > 1) {
                    logger.info("表头不支持,只支持单一表头");
                    throw new ServiceException("表头不支持,只支持单一表头");
                }
                headList.add(result.get(0));
            }
            String[] headArray = headList.toArray(new String[headList.size()]);
            csvPrinter = csvFormat.withHeader(headArray).print(osw);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 写入文件
     * @param data
     * @return
     */
    public void write(List<? extends BaseRowModel> data) {
        for (int i = 0; i < data.size(); i++) {
            this.addOneRowOfDataTo(data.get(i));
        }
    }

    /**
     * 添加一行数据
     *
     * @param oneRowData
     */
    public void addOneRowOfDataTo(Object oneRowData) {
        BeanMap beanMap = BeanMap.create(oneRowData);
        List<String> filedList = new ArrayList<>();
        for (ExcelColumnProperty excelHeadProperty : excelHead.getColumnPropertyList()) {
            String value = getFieldStringValue(beanMap, excelHeadProperty.getField().getName(), excelHeadProperty.getFormat());
            filedList.add(value);
        }
        String[] valueArray = filedList.toArray(new String[filedList.size()]);
        try {
            csvPrinter.printRecord(valueArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据字段获取对应的值
     *
     * @param beanMap
     * @param fieldName
     * @param format
     * @return
     */
    public String getFieldStringValue(BeanMap beanMap, String fieldName, String format) {
        String cellValue = null;
        Object value = beanMap.get(fieldName);
        if (value != null) {
            if (value instanceof Date) {
                cellValue = TypeUtil.formatDate((Date) value, format);
            } else {
                cellValue = value.toString();
            }
        }
        return cellValue;
    }


}
