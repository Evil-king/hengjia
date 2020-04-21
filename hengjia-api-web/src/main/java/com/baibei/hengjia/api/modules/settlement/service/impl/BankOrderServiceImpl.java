package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.bean.dto.BankFileDto;
import com.baibei.hengjia.api.modules.settlement.dao.BankOrderMapper;
import com.baibei.hengjia.api.modules.settlement.model.BankOrder;
import com.baibei.hengjia.api.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.api.modules.utils.SFTPUtils;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author: Longer
* @date: 2019/06/25 11:30:00
* @description: 银行出入金流水服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BankOrderServiceImpl extends AbstractService<BankOrder> implements IBankOrderService {

    @Autowired
    private SFTPUtils sftpUtils;
    @Autowired
    private BankOrderMapper bankOrderMapper;
    @Value("${ftp.path}")
    private String ftpPath;

    @Override
    public String bankOrder(String fileName) {
        log.info("======开始银行出入金流水入库=======");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //批次号
        String batchNo = simpleDateFormat.format(new Date());
        String filePath = ftpPath+"/"+fileName;
        /*String filePath = "F:\\CRJ020120928210009432120120928233452";*/
        log.info("出入金流水文件路径===》{}",filePath);
        List<BankOrder> bankOrderList = dealFileMsg(filePath,batchNo);
        //将银行给过来的流水信息插入数据库
        bankOrderMapper.insertList(bankOrderList);
        log.info("======结束银行出入金流水入库=======");
        return batchNo;
    }

    @Override
    public List<BankOrder> getOrderListByBatchNo(String batchNo) {
        Condition condition = new Condition(BankOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(batchNo)){
            criteria.andEqualTo("batchNo",batchNo);
            return bankOrderMapper.selectByCondition(condition);
        }else{
            return null;
        }
    }

    @Override
    public BankOrder getOneBankOrder(BankOrder bankOrder) {
        Condition condition = new Condition(BankOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        List<BankOrder> bankOrderList = bankOrderMapper.select(bankOrder);
        return bankOrderList.size()==0?null:bankOrderList.get(0);
    }

    /**
     * 处理文件里的信息，并将信息封装到BankOrder实体类里
     * @param filePath 文件路径 如： F:\\CRJ020120928210009432120120928233452
     * @param batchNo 批次号
     * @return
     */
    public List<BankOrder> dealFileMsg(String filePath,String batchNo){
        log.info("======开始读取银行出入金流水文件=======");
        List<BankOrder> bankOrderList = new ArrayList();
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int i=0;
                while((lineTxt = bufferedReader.readLine()) != null){
                    i++;
                    if(i>1){//第二行开始对数据进行处理
                        String[] splitStr = lineTxt.split("&");
                        BankOrder bankOrder = new BankOrder();
                        bankOrder.setBatchNo(batchNo);
                        bankOrder.setSerialNo(Integer.parseInt(splitStr[0]));//序号
                        bankOrder.setType(Byte.valueOf(splitStr[1]));//记账标志(1：出金 2：入金 3：挂账)
                        bankOrder.setDealFlag(splitStr[2]);//处理标志(挂账才有值)
                        bankOrder.setPayer(splitStr[3]);//付款人账号
                        bankOrder.setPayee(splitStr[4]);//收款人账号
                        bankOrder.setMemberNo(splitStr[5]);//交易网会员代码
                        bankOrder.setSubAccount(splitStr[6]);//子账户
                        bankOrder.setSubAccountName(splitStr[7]);//子账户名称
                        bankOrder.setAmount(new BigDecimal(splitStr[8]));;//交易金额
                        bankOrder.setFee(new BigDecimal(splitStr[9]));//手续费
                        bankOrder.setPayFeeAccount(splitStr[10]);//支付手续费子账号
                        bankOrder.setPaySubAccountName(splitStr[11]);//支付子账号名称
                        bankOrder.setPayDate(splitStr[12]);//交易日期
                        bankOrder.setPayTime(splitStr[13]);//交易时间
                        bankOrder.setBankSerialNo(splitStr[14]);//银行前置流水号
                        bankOrder.setCreateTime(new Date());
                        bankOrder.setModifyTime(new Date());
                        bankOrder.setFlag(Byte.valueOf("1"));
                        bankOrderList.add(bankOrder);
                    }
                }
                read.close();
            }else{
                log.info("找不到指定的文件");
                throw new ServiceException("找不到指定文件");
            }
        } catch (Exception e) {
            log.info("读取文件内容出错");
            e.printStackTrace();
            throw new ServiceException("读取文件内容出错");
        }
        log.info("======结束读取银行出入金流水文件=======");
        return bankOrderList;
    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //批次号
        String batchNo = simpleDateFormat.format(new Date());
        System.out.println(batchNo);
    }
}
