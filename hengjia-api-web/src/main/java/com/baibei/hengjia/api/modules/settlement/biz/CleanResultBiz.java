package com.baibei.hengjia.api.modules.settlement.biz;

import com.baibei.hengjia.api.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.api.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.api.modules.settlement.service.IBatFailResultService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.api.modules.settlement.service.ICustDzFailService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/27 3:18 PM
 * @description: 清算文件处理类
 */
@Component
@Slf4j
public class CleanResultBiz {
    @Value("${ftp.path}")
    private String ftpPath;
    @Autowired
    private ICustDzFailService custDzFailService;
    @Autowired
    private IBatFailResultService batFailResultService;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;


    /**
     * 清算失败文件处理
     *
     * @param fileName
     */
    public void batFailResult(String fileName) {
        log.info("清算失败文件处理,fileName={}", fileName);
        // step1.解析清算失败文件
        List<BatFailResult> result = new ArrayList<>();
        String batchNo = DateUtil.yyyyMMddNoLine.get().format(new Date());
        List<String> lineList = readFileByLine(fileName);
        if (CollectionUtils.isEmpty(lineList)) {
            log.info("清算失败文件内容为空");
            return;
        }
        // step2.如果数据库中已经存在数据,则先删除
        List<BatFailResult> existList = batFailResultService.findByBatchNo(batchNo);
        if (!CollectionUtils.isEmpty(existList)) {
            for (BatFailResult batFailResult : existList) {
                batFailResult.setModifyTime(new Date());
                batFailResult.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
                batFailResultService.update(batFailResult);
            }
        }
        // step3.保存入库
        String[] items;
        for (String s : lineList) {
            items = s.split("&");
            BatFailResult item = new BatFailResult();
            item.setQueryId(Integer.valueOf(items[0]));
            item.setTranDateTime(items[1]);
            item.setCounterId(items[2]);
            item.setSupacctId(items[3]);
            item.setFuncCode(items[4]);
            item.setCustAcctId(items[5]);
            item.setCustName(items[6]);
            item.setThirdCustId(items[7]);
            item.setThirdLogNo(items[8]);
            item.setCcyCode(items[9]);
            item.setFreezeAmount(new BigDecimal(items[10]));
            item.setUnfreezeAmount(new BigDecimal(items[11]));
            item.setAddtranAmount(new BigDecimal(items[12]));
            item.setCuttranAmount(new BigDecimal(items[13]));
            item.setProfitAmount(new BigDecimal(items[14]));
            item.setLossAmount(new BigDecimal(items[15]));
            item.setTranHandFee(new BigDecimal(items[16]));
            item.setTranCount(new BigDecimal(items[17]));
            item.setNewBalance(new BigDecimal(items[18]));
            item.setNewFreezeAmount(new BigDecimal(items[19]));
            item.setNote(items[20]);
            item.setReserve(items[21]);
            item.setErrorCode(items[22]);
            item.setFailReason(items[23]);
            item.setBatchNo(batchNo);
            item.setCreateTime(new Date());
            item.setModifyTime(new Date());
            item.setFlag(Byte.valueOf(Constants.Flag.VALID));
            result.add(item);
        }
        batFailResultService.save(result);
        // 更新清算进度状态
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.CLEAN_RESULT, Constants.CleanFlowPathStatus.COMPLETED);
    }


    /**
     * 对账不平记录文件处理
     *
     * @param fileName
     */
    public void batCustDzFail(String fileName) {
        log.info("对账不平记录文件处理,fileName={}", fileName);
        // step1.解析读取对账不平记录文件
        List<CustDzFail> result = new ArrayList<>();
        String batchNo = DateUtil.yyyyMMddNoLine.get().format(new Date());
        List<String> lineList = readFileByLine(fileName);
        if (CollectionUtils.isEmpty(lineList)) {
            log.info("对账不平记录文件内容为空");
            return;
        }
        // step2.如果数据库中已经存在数据,则先删除
        List<CustDzFail> existList = custDzFailService.findByBatchNo(batchNo);
        if (!CollectionUtils.isEmpty(existList)) {
            for (CustDzFail custDzFail : existList) {
                custDzFail.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
                custDzFail.setModifyTime(new Date());
                custDzFailService.update(custDzFail);
            }
        }
        // step3.保存入库
        String[] items;
        for (String s : lineList) {
            items = s.split("&");
            CustDzFail item = new CustDzFail();
            item.setCustAcctId(items[0]);
            item.setCustName(items[1]);
            item.setThirdCustId(items[2]);
            item.setBankBalance(new BigDecimal(items[3]));
            item.setBankFrozenAmount(new BigDecimal(items[4]));
            item.setHengjiaBalance(new BigDecimal(items[5]));
            item.setHengjiaFrozenAmount(new BigDecimal(items[6]));
            item.setBalanceDiff(new BigDecimal(items[7]));
            item.setFrozenDiff(new BigDecimal(items[8]));
            item.setRemark(items[9]);
            item.setBatchNo(batchNo);
            item.setCreateTime(new Date());
            item.setModifyTime(new Date());
            item.setFlag(Byte.valueOf(Constants.Flag.VALID));
            result.add(item);
        }
        // 保存入库
        custDzFailService.save(result);
        // 更新清算进度状态
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.CLEAN_RESULT, Constants.CleanFlowPathStatus.COMPLETED);
    }


    /**
     * 解析文件,按行读取
     *
     * @param filename
     * @return
     */
    private List<String> readFileByLine(String filename) {
        List<String> result = new ArrayList<>();
        File file = new File(ftpPath + "/" + filename);
        InputStream is = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            is = new FileInputStream(file);
            reader = new InputStreamReader(is, "gbk");
            bufferedReader = new BufferedReader(reader);
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                i++;
                if (i > 1) {//第二行开始对数据进行处理
                    result.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader)
                    bufferedReader.close();
                if (null != reader)
                    reader.close();
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
