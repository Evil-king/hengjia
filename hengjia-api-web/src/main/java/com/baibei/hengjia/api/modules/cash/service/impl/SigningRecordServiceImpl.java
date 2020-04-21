package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.account.dao.AccountMapper;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.cash.bean.vo.TotalBalanceVo;
import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.component.BankMessageSplice;
import com.baibei.hengjia.api.modules.cash.component.GetSentServer;
import com.baibei.hengjia.api.modules.cash.dao.SigningRecordMapper;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;


/**
 * @author: uqing
 * @date: 2019/06/06 10:29:50
 * @description: SigningRecord服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class SigningRecordServiceImpl extends AbstractService<SigningRecord> implements ISigningRecordService {

    @Autowired
    private SigningRecordMapper tblTraSigningRecordMapper;
    @Autowired
    private BankBackMessageAnalysis bankInterfaceBackMessage;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    PropertiesVal propertiesVal;

    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public SigningRecord findByCustAcctId(String custAcctId) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("thirdCustId", custAcctId);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));

        return this.findOneByCondition(condition);
    }

    @Override
    public SigningRecord findByThirdCustId(String thirdCustId) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("thirdCustId", thirdCustId);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        return this.findOneByCondition(condition);
    }

    @Override
    public SigningRecord findByOneCustAcctId(String custAcctId) {
        Condition condition = new Condition(SigningRecord.class);
        condition.createCriteria().andEqualTo("custAcctId", custAcctId);
        return this.findOneByCondition(condition);
    }


    @Override
    public List<SigningRecord> allList() {
        List<String> list = new ArrayList<>();
        list.add(Constants.SigningStatus.SIGNING_CREATE);
        list.add(Constants.SigningStatus.SIGNING_UPDATE);
        list.add(Constants.SigningStatus.SIGNING_DELETE);
        Condition condition = new Condition(SigningRecord.class);
        condition.createCriteria().andIn("funcFlag", list)
                .andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }

    @Override
    public String getAcountBalance(String message) {
        {
            log.info("1019接口入参,message={}", message);
            Map<String, String> result = bankInterfaceBackMessage.parsingTranMessageString(message);
            String tranFunc = result.get("TranFunc");
            if (tranFunc != null && "1019".equals(tranFunc)) {
                Condition condition = new Condition(SigningRecord.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("supAcctId", result.get("SupAcctId"));
                criteria.andEqualTo("custAcctId", result.get("CustAcctId"));
                criteria.andNotEqualTo("funcFlag", 3);
                List<SigningRecord> signingRecords = tblTraSigningRecordMapper.selectByCondition(condition);
                if (signingRecords.size() < 1) {
                    return "";
                }
                SigningRecord signingRecord = signingRecords.get(0);
                Condition condition1 = new Condition(Account.class);
                Example.Criteria criteria1 = buildValidCriteria(condition1);
                criteria1.andEqualTo("customerNo", signingRecord.getThirdCustId());
                List<Account> accounts = accountMapper.selectByCondition(condition1);
                if (accounts.size() < 1) {
                    return "";
                }
                //找到对应的账号
                Account account = accounts.get(0);
                //组装报文发送到渠道
                BankMessageSplice bf = new BankMessageSplice();// 拼接报文类
                HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
                parmaKeyDict.put("ThirdLogNo", "&");//交易网流水号
                parmaKeyDict.put("TranFunc", "1019");//接口交易码
                parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
                parmaKeyDict.put("ServiceType", "02");//服务类型
                parmaKeyDict.put("RspMsg", "交易成功");
                parmaKeyDict.put("ReqMsg", "交易成功");

                parmaKeyDict.put("CustAcctId", signingRecord.getCustAcctId());//子账户
                parmaKeyDict.put("ThirdCustId", signingRecord.getThirdCustId());//交易网会员代码
                parmaKeyDict.put("CustName", signingRecord.getCustName());//子账户名称
                parmaKeyDict.put("TotalAmount", account.getTotalBalance().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//总资产
                parmaKeyDict.put("TotalBalance", account.getBalance().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//可用资金
                parmaKeyDict.put("TotalFreezeAmount", account.getFreezingAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//冻结资金
                parmaKeyDict.put("TranDate", signingRecord.getCreateTime() + "");//开户日期
                String Message = bf.getSignMessage(parmaKeyDict);//拼接报文
                byte[] Messages;
                try {
                    Messages = Message.getBytes("gbk");
                    String HeadMessage = bf.getYinShangJieSuanTongHeadMessage(Messages.length, parmaKeyDict);
                    String tranMessage = HeadMessage + Message;
                    log.info("tranMessage={}", tranMessage);
//                    GetSentServer yinShangJieSuanTongDao = new GetSentServer();// 发送报文并接受返回报文类
//                    String tranBackMessage = yinShangJieSuanTongDao.sendAndGetMessage(propertiesVal.getStockIp(), propertiesVal.getSocketPort(), tranMessage);
//                    log.info("tranBackMessage={}", tranBackMessage);
                    return tranMessage;
                } catch (UnsupportedEncodingException e) {
                    log.info("e={}", e);
                    e.printStackTrace();
                }
            }
            return "";
        }
    }

    /**
     * 1022接口
     *
     * @return
     */
    @Override
    public ApiResult queryTotalBalance() {
        ApiResult apiResult = new ApiResult();
        log.info("进入1022接口");
        List<String> supAcctIds = tblTraSigningRecordMapper.findSupAcctId();
        List<TotalBalanceVo> totalBalanceVos = new ArrayList<>();
        if (supAcctIds.size() < 1) {
            return null;
        }
        for (int i = 0; i < supAcctIds.size(); i++) {
            HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
            Map<String, String> retKeyDict;// 返回报文解析结果
            parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
            parmaKeyDict.put("TranFunc", "1022");// 接口交易码
            parmaKeyDict.put("ThirdLogNo", "&");
            parmaKeyDict.put("ServiceType", "01");//请求

            parmaKeyDict.put("AcctId", supAcctIds.get(i));//资金汇总账号
            parmaKeyDict.put("TranWebCode", propertiesVal.getQydm());
            parmaKeyDict.put("Reserve", "");//保留域

            BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类

            String tranBackMessage = sendMessage(parmaKeyDict);
            retKeyDict = bm.parsingTranMessageString(tranBackMessage);
            if ("000000".equals(retKeyDict.get("RspCode"))) {
                String array = retKeyDict.get("backBodyMessages");
                String[] arr = array.split("&");
                for (int j = 0; j < arr.length; j += 12) {
                    TotalBalanceVo totalBalanceVo = new TotalBalanceVo();
                    totalBalanceVo.setRecordNum(arr[j]);//汇总账号总数
                    totalBalanceVo.setTranWebCode(arr[j + 1]);//交易网代码
                    totalBalanceVo.setTranWebName(arr[j + 2]);//交易网名称
                    totalBalanceVo.setAcctId(arr[j + 3]);//资金汇总账号
                    totalBalanceVo.setAcctName(arr[j + 4]);//户名
                    totalBalanceVo.setIdType(arr[j + 5]);//证件类型
                    totalBalanceVo.setIdCode(arr[j + 6]);//证件号码
                    totalBalanceVo.setWebName(arr[j + 7]);//网银用户名
                    totalBalanceVo.setWebCustId(arr[j + 8]);//网银客户号
                    totalBalanceVo.setFuncFlag(arr[j + 9]);//服务类型
                    totalBalanceVo.setCcyCode(arr[j + 10]);//币种
                    totalBalanceVo.setCurBalance(new BigDecimal(arr[j + 11]).setScale(2, BigDecimal.ROUND_DOWN));//当前账户余额
                    totalBalanceVos.add(totalBalanceVo);
                }
                apiResult.setData(totalBalanceVos);
                return apiResult;
            }
        }
        return null;
    }

    /**
     * 查询签约的用户,是否大于等于下个交易日
     *
     * @param customerNo 会员代码
     * @return
     */
    @Override
    public Boolean isTodaySigning(String customerNo) {
        // 1、查询用户签约信息是否大于下个交易时间
        List<SigningRecord> signing = tblTraSigningRecordMapper.findTodaySigning(customerNo);
        if (signing.size() > 0) {
            // 2、stop 获取签约创建时间的下一个交易日
            Date date = tradeDayService.getAddNTradeDay(signing.get(0).getCreateTime(), 1);
            int result = new Date().compareTo(date);
            if (result >= 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean isOnlyIdCode(String idCode) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("idCode", idCode);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        List<SigningRecord> signingRecordList = this.findByCondition(condition);
        if (signingRecordList.size() > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean isOnlyIdCode(String idCode, String customerNo) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("idCode", idCode);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        criteria.andNotEqualTo("thirdCustId",customerNo);
        List<SigningRecord> signingRecordList = this.findByCondition(condition);
        if (signingRecordList.size() > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public SigningRecord findByReleaseThirdCustId(String thirdCustId) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("thirdCustId", thirdCustId);
        criteria.andEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return this.findOneByCondition(condition);
    }

    protected String sendMessage(HashMap<String, String> parmaKeyDict) {
        BankMessageSplice bf = new BankMessageSplice();// 拼接报文类
        GetSentServer yinShangJieSuanTongDao = new GetSentServer();// 发送报文并接受返回报文类

        String Message = bf.getSignMessage(parmaKeyDict);
        byte[] Messages;
        try {
            Messages = Message.getBytes("gbk");
            String HeadMessage = bf.getYinShangJieSuanTongHeadMessage(Messages.length, parmaKeyDict);//通讯报文头
            String tranMessage = HeadMessage + Message;
            log.info("请求报文：{}" + tranMessage);
            String tranBackMessage = yinShangJieSuanTongDao.sendAndGetMessage(propertiesVal.getStockIp(), propertiesVal.getSocketPort(), tranMessage);
            log.info("返回报文：{}" + tranBackMessage);
            return tranBackMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
