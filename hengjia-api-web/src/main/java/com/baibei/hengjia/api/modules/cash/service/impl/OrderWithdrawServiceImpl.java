package com.baibei.hengjia.api.modules.cash.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.MemberBalanceVo;
import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.component.BankMessageSplice;
import com.baibei.hengjia.api.modules.cash.component.GetSentServer;
import com.baibei.hengjia.api.modules.cash.dao.AccountBookMapper;
import com.baibei.hengjia.api.modules.cash.dao.OrderWithdrawMapper;
import com.baibei.hengjia.api.modules.cash.model.AccountBook;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.cash.service.IValidateService;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.PinganProcess;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.Utils;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: uqing
 * @date: 2019/06/03 20:37:57
 * @description: OrderWithdraw服务实现
 */
@Slf4j
@Service
public class OrderWithdrawServiceImpl extends AbstractService<OrderWithdraw> implements IOrderWithdrawService {

    @Autowired
    private PinganProcess pinganProsscess;
    @Autowired
    private OrderWithdrawMapper orderWithdrawMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private BankBackMessageAnalysis bankInterfaceBackMessage;
    @Autowired
    PropertiesVal propertiesVal;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private AccountBookMapper accountBookMapper;
    @Autowired
    private IValidateService validateService;


    /**
     * 创建订单
     *
     * @param orderWithdrawDto
     * @return
     */
    @Override
    public ApiResult withdrawApplicationApplication(OrderWithdrawDto orderWithdrawDto) {

        return pinganProsscess.doProcess(orderWithdrawDto);
    }


    /**
     * 1318接口
     *
     * @return
     */
    public void queryWithdrawTask(OrderWithdraw orderWithdraw) {
        log.info("银行1318接口---------------------");
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(orderWithdraw.getCustomerNo());

        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        parmaKeyDict.put("Qydm", propertiesVal.getQydm());// 平台代码
        parmaKeyDict.put("TranFunc", "1318");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());//订单号
        parmaKeyDict.put("ServiceType", "01");

        parmaKeyDict.put("TranWebName", propertiesVal.getTranWebName());//交易网名称
        parmaKeyDict.put("ThirdCustId", signingRecord.getThirdCustId());//交易网会员代码
        parmaKeyDict.put("IdType", signingRecord.getIdType());//会员证件类型
        parmaKeyDict.put("IdCode", signingRecord.getIdCode());//会员证件号码
        parmaKeyDict.put("TranOutType", "1");//出金类型 1：会员出金
        parmaKeyDict.put("CustAcctId", signingRecord.getCustAcctId());//子账户账号
        parmaKeyDict.put("CustName", signingRecord.getCustName());//子账户名称
        parmaKeyDict.put("SupAcctId", signingRecord.getSupAcctId());//资金汇总账号
        parmaKeyDict.put("TranType", signingRecord.getTranType());//转账方式
        parmaKeyDict.put("OutAcctId", signingRecord.getRelatedAcctId());//出金账号
        parmaKeyDict.put("OutAcctIdName", orderWithdraw.getAccountName());//出金账户名称
        parmaKeyDict.put("OutAcctIdBankName", orderWithdraw.getBankName());//出金账号开户行名
        log.info("1318接口出金金额---amount{}", orderWithdraw.getOrderamt());
        parmaKeyDict.put("TranAmount", String.valueOf(orderWithdraw.getOrderamt()));//申请出金金额

        try {
            BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类
            String tranBackMessage = sendMessage(parmaKeyDict);
            log.info("返回报文：{}" + tranBackMessage);
            Map<String, String> retKeyDict = bm.parsingTranMessageString(tranBackMessage);// 返回报文解析结果

            if ("000000".equals(retKeyDict.get("RspCode"))) {
                updateOrder(orderWithdraw, retKeyDict.get("FrontLogNo"), "waiting", "");
            } else {
                log.info("msg={}", retKeyDict.get("RspMsg"));
                updateOrder(orderWithdraw, retKeyDict.get("FrontLogNo"), "waiting", retKeyDict.get("RspMsg"));
            }
        } catch (Exception e) {
            log.error("e={}", e);
            e.printStackTrace();
        }
    }


    /**
     * 1325接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queryWithdrawOrderTask(OrderWithdraw orderWithdraw) {
        log.info("银行1325接口---------------------orderWithdraw={}", JSONObject.toJSONString(orderWithdraw));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String startTime = simpleDateFormat.format(orderWithdraw.getCreateTime());
        String endTime = simpleDateFormat.format(orderWithdraw.getUpdateTime());
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        Map<String, String> retKeyDict;// 返回报文解析结果
        parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
        parmaKeyDict.put("TranFunc", "1325");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());//订单号
        parmaKeyDict.put("ServiceType", "01");

        parmaKeyDict.put("SupAcctId", orderWithdraw.getSupAcctId());
        parmaKeyDict.put("BeginDate", startTime);//开始日期
        parmaKeyDict.put("EndDate", endTime);//结束日期
        parmaKeyDict.put("PageNum", "1");//第几页
        BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类
        try {
            String tranBackMessage = sendMessage(parmaKeyDict);
            retKeyDict = bm.parsingTranMessageString(tranBackMessage);
            String ThirdLogNo = "";//交易网流水号(业务系统的订单号)
            String frontLogNo = "";//银行前置流水号
            String TranFlag = "";//记账标志
            String TranStatus = "";//交易状态
            String TranAmount = "";//交易金额
            String CustAcctId = "";//子账号
            String ThirdCustId = "";//会员代码
            String TranDate = "";//交易日期
            String AcctDate = "";//会计日期
            String remark = retKeyDict.get("RspMsg");
            if ("000000".equals(retKeyDict.get("RspCode"))) {
                String array = retKeyDict.get("Array");
                String[] arr = array.split("&");
                log.info("arr={}", JSONObject.toJSONString(arr));
                for (int j = 0; j < arr.length; j += 9) {
                    ThirdLogNo = arr[j];
                    frontLogNo = arr[j + 1];
                    TranFlag = arr[j + 2];
                    TranStatus = arr[j + 3];
                    TranAmount = arr[j + 4];
                    CustAcctId = arr[j + 5];
                    ThirdCustId = arr[j + 6];
                    TranDate = arr[j + 7];
                    AcctDate = arr[j + 8];
                    if ("2".equals(TranFlag)) { // 1、入金 2、出金
                        log.info("ThirdLogNo={},orderNo={}", ThirdLogNo, orderWithdraw.getOrderNo());
                        if (ThirdLogNo.equals(orderWithdraw.getOrderNo())) {  //判断订单号是否相等
                            orderWithdraw.setCustAcctId(CustAcctId);//会员子账号
                            if ("0".equals(TranStatus)) { //判断状态是否是成功的
                                //更新提现订单
                                if (updateOrder(orderWithdraw, frontLogNo, "success", "") != 1) {
                                    throw new ServiceException("更新订单状态失败");
                                }
                            }
                            if ("1".equals(TranStatus)) {//处理失败的
                                //更新提现订单
                                if (updateOrder(orderWithdraw, frontLogNo, "fail", "") != 1) {
                                    throw new ServiceException("更新订单状态失败");
                                }
                                if (!"success".equals(operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, ""))) {
                                    throw new ServiceException("调用账户扣钱加钱失败");
                                }
                            }
                        }
                    }
                }
            } else {
                log.error("RspCode={}", retKeyDict.get("RspCode"));
                log.error("RspMsg={}", retKeyDict.get("RspMsg"));
                //更新提现订单
                if (updateOrder(orderWithdraw, frontLogNo, "fail", retKeyDict.get("RspMsg")) != 1) {
                    throw new ServiceException("更新订单状态失败");
                }
                if (!"success".equals(operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, ""))) {
                    throw new ServiceException("调用账户扣钱加钱失败");
                }
            }
        } catch (Exception e) {
            log.error("e={}", e);
            e.printStackTrace();
        }

    }

    /**
     * 1317接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queryDataAsynchronousTask(OrderWithdraw orderWithdraw) {
        log.info("银行1317接口---------------------");
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(orderWithdraw.getCustomerNo());
        try {
            //组装数据
            HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
            Map<String, String> retKeyDict;// 返回报文解析结果
            parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
            parmaKeyDict.put("TranFunc", "1317");// 接口交易码
            parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());
            parmaKeyDict.put("ServiceType", "01");//请求

            parmaKeyDict.put("FrontLogNo", orderWithdraw.getExternalNo());//银行前置流水号
            parmaKeyDict.put("TranWebName", propertiesVal.getTranWebName());//交易网名称
            parmaKeyDict.put("ThirdCustId", signingRecord.getThirdCustId());
            parmaKeyDict.put("IdType", signingRecord.getIdType());
            parmaKeyDict.put("IdCode", signingRecord.getIdCode());
            parmaKeyDict.put("CustAcctId", signingRecord.getCustAcctId());
            parmaKeyDict.put("CustName", signingRecord.getCustName());
            parmaKeyDict.put("SupAcctId", signingRecord.getSupAcctId());
//            if ("2".equals(orderWithdraw.getStatus())) {
//                parmaKeyDict.put("TranStatus", "1");//审核状态(1、通过 2、未通过)
//            }
//            if ("6".equals(orderWithdraw.getStatus())) {
//                parmaKeyDict.put("TranStatus", "2");//审核状态(1、通过 2、未通过)
//            }
            parmaKeyDict.put("TranStatus", "2");//审核状态(1、通过 2、未通过)
            parmaKeyDict.put("TranType", signingRecord.getTranType());
            parmaKeyDict.put("OutAcctId", signingRecord.getRelatedAcctId());//出金账号
            parmaKeyDict.put("OutAcctIdName", signingRecord.getCustName());//子账户名称
            parmaKeyDict.put("CcyCode", "RMB");
            log.info("1317接口发给银行的金额,amount={}", orderWithdraw.getOrderamt());
            parmaKeyDict.put("TranAmount", orderWithdraw.getOrderamt().toString());
            parmaKeyDict.put("Reserve", propertiesVal.getWithdrawStr());

            BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类

            String tranBackMessage = sendMessage(parmaKeyDict);
            retKeyDict = bm.parsingTranMessageString(tranBackMessage);
            String frontLogNo = retKeyDict.get("FrontLogNo");
            if ("000000".equals(retKeyDict.get("RspCode"))) {
                //更新提现订单
//                if (updateOrder(orderWithdraw, frontLogNo, "success", "") != 1) {
//                    throw new ServiceException("更新订单状态失败");
//                }
                //更新订单
                updateOrder(orderWithdraw, "", "noPass", "");
                //退钱
                operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, "");
            } else {
                //更新提现订单
                if (updateOrder(orderWithdraw, frontLogNo, "noPass", retKeyDict.get("RspMsg")) != 1) {
                    throw new ServiceException("更新订单状态失败");
                }
                if (!"success".equals(operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, ""))) {
                    throw new ServiceException("调用账户加钱失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1312接口
     *
     * @param message
     */
    @Override
    @Transactional(propagation = Propagation.NESTED)
    public String withdrawForBank(String message) {
        log.info("1312接口入参,message={}", message);
        Map<String, String> result = bankInterfaceBackMessage.parsingTranMessageString(message);
        String tranFunc = result.get("TranFunc");
        if (tranFunc != null && "1312".equals(tranFunc)) {
            String orderNo = CodeUtils.generateTreeOrderCode();
            //创建出金订单
            OrderWithdraw orderWithdraw = new OrderWithdraw();
            orderWithdraw.setCustomerNo(result.get("ThirdCustId"));//会员编号
            orderWithdraw.setAccount(result.get("OutAcctId"));//出金账号
            orderWithdraw.setAccountName(result.get("CustName"));//出金账户名
            orderWithdraw.setCustAcctId(result.get("CustAcctId"));//会员子账号
            orderWithdraw.setCreateTime(new Date());
            orderWithdraw.setUpdateTime(new Date());
            orderWithdraw.setFlag((byte) 1);//未删除
            orderWithdraw.setEffective((byte) 1);//未处理
            //计算手续费
            BigDecimal Fee = Utils.getFee(new BigDecimal(result.get("TranAmount")), propertiesVal.getRate(), propertiesVal.getFee());
            orderWithdraw.setHandelFee(Fee);//手续费
            orderWithdraw.setExternalNo(result.get("externalNo"));//外部订单号
            orderWithdraw.setOrderType("2");//出金类型
            orderWithdraw.setStatus("6");//直接审核不通过
            orderWithdraw.setReviewer("系统");
//            if (validateService.compairAmount(result.get("ThirdCustId"), new BigDecimal(result.get("TranAmount"))) == true) {
//                orderWithdraw.setStatus("1");//状态
//            } else {
//                orderWithdraw.setStatus("2");//不需要后台审核
//                orderWithdraw.setReviewer("系统");
//            }
            orderWithdraw.setRemarks("");
            //出金金额需要扣减手续费
            BigDecimal amount = new BigDecimal(result.get("TranAmount")).subtract(Fee);
            orderWithdraw.setOrderamt(amount);//出金金额
            orderWithdraw.setOrderNo(orderNo);//出金订单号
            orderWithdrawMapper.insert(orderWithdraw);
            //扣钱
            try {
                if (!"success".equals(operatorMoney(Constants.WithdrawType.DEDUCTING_MONEY, orderWithdraw, ""))) {
                    throw new ServiceException("调用账户扣钱失败");
                }
            } catch (Exception e) {
                log.info("调用账户异常", e);
                updateOrder(orderWithdraw, result.get("externalNo"), "fail", "");
            }
            try {
                //组装报文发送到渠道
                BankMessageSplice bf = new BankMessageSplice();// 拼接报文类
                HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
                parmaKeyDict.put("ThirdLogNo", orderNo);//交易网流水号
                parmaKeyDict.put("TranFunc", "1312");//接口交易码
                parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
                parmaKeyDict.put("ServiceType", "02");//应答

                parmaKeyDict.put("RspCode", "111111");//这样告诉银行这笔单是需要人工审核的
                parmaKeyDict.put("Reserve", "");

                String Message = bf.getSignMessage(parmaKeyDict);//拼接报文
                byte[] Messages;
                Messages = Message.getBytes("gbk");
                String HeadMessage = bf.getYinShangJieSuanTongHeadMessage(Messages.length, parmaKeyDict);
                String tranMessage = HeadMessage + Message;
                log.info("tranMessage={}", tranMessage);
                return tranMessage;
            } catch (UnsupportedEncodingException e) {
                log.info("e={}", e);
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 1010接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult queryMemberBlance(String SelectFlag, String pageNum) {
        ApiResult apiResult = new ApiResult();

        List<MemberBalanceVo> balanceVoList = Lists.newArrayList();

        //组装数据
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        Map<String, String> retKeyDict;// 返回报文解析结果
        parmaKeyDict.put("Qydm", propertiesVal.getQydm());//平台代码
        parmaKeyDict.put("TranFunc", "1010");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", "&");
        parmaKeyDict.put("ServiceType", "01");//请求

        parmaKeyDict.put("SelectFlag", SelectFlag);//查询标志

        parmaKeyDict.put("PageNum", pageNum);//第几页

        parmaKeyDict.put("Reserve", "");//保留域

        BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类

        String tranBackMessage = sendMessage(parmaKeyDict);
        retKeyDict = bm.parsingTranMessageString(tranBackMessage);
        if ("000000".equals(retKeyDict.get("RspCode"))) {
            String array = retKeyDict.get("Array");
            String[] arr = array.split("&");
            for (int j = 0; j < arr.length; j += 11) {
                AccountBook accountBook = new AccountBook();
                accountBook.setCustAcctid(arr[j]);
                accountBook.setCustFlag(Byte.valueOf(arr[j + 1]));
                accountBook.setCustType(Byte.valueOf(arr[j + 2]));
                accountBook.setCustStatus(Byte.valueOf(arr[j + 3]));
                accountBook.setCustomerNo(arr[j + 4]);
                accountBook.setMainAcctid(arr[j + 5]);
                accountBook.setCustName(arr[j + 6]);
                accountBook.setTotalAmount(new BigDecimal(arr[j + 7]));
                accountBook.setTotalBalance(new BigDecimal(arr[j + 8]));
                accountBook.setTotalFreezeAmount(new BigDecimal(arr[j + 9]));
                accountBook.setTranDate(arr[j + 10]);
                accountBook.setFlag((byte) 1);
                accountBook.setCreateTime(new Date());
                accountBook.setModifyTime(new Date());
                MemberBalanceVo memberBalanceVo = BeanUtil.copyProperties(accountBook, MemberBalanceVo.class);
                accountBookMapper.insert(accountBook);
                balanceVoList.add(memberBalanceVo);
                apiResult.setData(balanceVoList);
            }
            return apiResult;
        } else {
            apiResult.setCode(-901);
            apiResult.setMsg(retKeyDict.get("RspMsg"));
            return apiResult;

        }
    }


    @Override
    public OrderWithdraw getOrderByExternalNo(String externalNo) {
        Condition condition = new Condition(OrderWithdraw.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(externalNo)) {
            criteria.andEqualTo("externalNo", externalNo);
        }
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList.size() == 0 ? null : orderWithdrawList.get(0);
    }

    @Override
    public List<OrderWithdraw> getPeriodOrderList(String period) {
        return orderWithdrawMapper.selectPeriodOrderList(period);
    }

    @Override
    public List<OrderWithdraw> getPeriodOrderListNotInBankOrders(String period) {
        return orderWithdrawMapper.selectPeriodOrderListNotInBankOrders(period);
    }

    @Transactional(rollbackFor = Exception.class)
    public String operatorMoney(String type, OrderWithdraw order, String sign) {
        String result = "";
        if (Constants.WithdrawType.ADD_MONEY.equals(type)) {
            //加回之前扣除的手续费
            accountService.updateWithDraw(order.getCustomerNo(), order.getHandelFee(), order.getOrderNo(),
                    sign == "diffType" ? (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode() :
                            (byte) FundTradeTypeEnum.WITHDRAW_BACK.getCode(), (byte) 2);

            //加回之前扣除的钱
            result = accountService.updateWithDraw(order.getCustomerNo(), order.getOrderamt(),
                    order.getOrderNo(),
                    sign == "diffType" ? (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode() :
                            (byte) FundTradeTypeEnum.MONEY_OUT_BACK.getCode(), (byte) 2);
        }
        if (Constants.WithdrawType.DEDUCTING_MONEY.equals(type)) {
            //扣除手续费
            accountService.updateWithDraw(order.getCustomerNo(), order.getHandelFee(), order.getOrderNo(),
                    sign == "diffType" ? (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode() :
                            (byte) FundTradeTypeEnum.MONEY_OUT_FEE.getCode(), (byte) 1);

            //扣除钱
            result = accountService.updateWithDraw(order.getCustomerNo(), order.getOrderamt(),
                    order.getOrderNo(),
                    sign == "diffType" ? (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode() :
                            (byte) FundTradeTypeEnum.MONEY_OUT.getCode(), (byte) 1);
        }
        return result;
    }

    public int updateOrder(OrderWithdraw order, String ourOrderNo, String type, String tranBackMessage) {
        log.info("更新入参------------,order={},type={}", JSONObject.toJSONString(order), type);
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("effective", 1);
        criteria.andEqualTo("orderNo", order.getOrderNo());

        order.setExternalNo(ourOrderNo);//外部订单号
        order.setCustomerNo(order.getCustomerNo());
        if ("success".equals(type)) {
            order.setStatus("4");//提现成功
            order.setEffective((byte) 0);//置为已处理
        }
        if ("fail".equals(type)) {
            order.setStatus("5");//提现失败
            order.setRemarks(tranBackMessage);//返回报文
            order.setEffective((byte) 0);//置为已处理
        }
        if ("waiting".equals(type)) {
            order.setStatus("3");//处理中
            order.setEffective((byte) 1);//置为未处理
        }
        if ("noPass".equals(type)) {
            order.setStatus("6");//提现审核不过
            order.setRemarks("");//返回报文
            order.setEffective((byte) 0);//置为已处理
        }
        order.setUpdateTime(new Date());
        order.setSupAcctId(order.getSupAcctId());

        return orderWithdrawMapper.updateByConditionSelective(order, condition);
    }

    public int updateOrderByDiff(OrderWithdraw order) {
        order.setUpdateTime(new Date());
        return orderWithdrawMapper.updateByPrimaryKeySelective(order);
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

    @Transactional(rollbackFor = Exception.class)
    public void findStatusTask(OrderWithdraw orderWithdraw) {
        if ("2".equals(orderWithdraw.getStatus())) {
            if ("1".equals(orderWithdraw.getOrderType())) {
                //调用1318接口
                queryWithdrawTask(orderWithdraw);
            }
//            if ("2".equals(orderWithdraw.getOrderType())) {
//                //调用1317接口
//                queryDataAsynchronousTask(orderWithdraw);
//            }
        }
        if ("6".equals(orderWithdraw.getStatus())) {
            //更新订单
            updateOrder(orderWithdraw, "", "noPass", "");
            //退钱
            operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, "");
        }
        if("6".equals(orderWithdraw.getStatus()) && "2".equals(orderWithdraw.getOrderType())){
            //调用1317接口
            queryDataAsynchronousTask(orderWithdraw);
        }
    }

    @Override
    public OrderWithdraw getByOrderNo(String orderNo) {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(orderNo)) {
            criteria.andEqualTo("orderNo", orderNo);
        }
        List<OrderWithdraw> withdrawList = orderWithdrawMapper.selectByCondition(condition);
        return withdrawList.size() == 0 ? null : withdrawList.get(0);
    }

    @Override
    public List<CustomerCountVo> sumFee(Map<String, Object> param) {
        return orderWithdrawMapper.sumFee(param);
    }


    @Override
    public List<OrderWithdraw> selectDoingList() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("status", "3");//提现处理中的
        criteria.andEqualTo("orderType", "1");//交易网
        criteria.andEqualTo("effective", 1);//查询未处理
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public void operatorReview() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status", "1");//提现申请中
        criteria.andEqualTo("effective", 1);//有效单
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (int i = 0; i < orderWithdrawList.size(); i++) {
                OrderWithdraw orderWithdraw = orderWithdrawList.get(i);
                //更新订单
                updateOrder(orderWithdraw, "", "noPass", "");
                //退钱
                operatorMoney(Constants.WithdrawType.ADD_MONEY, orderWithdraw, "");
            }
        }
    }

    @Override
    public BigDecimal sumAmountOfCustomer(String customerNo) {
        return orderWithdrawMapper.sumAmountOfCustomer(customerNo, new Date());
    }


    @Override
    public void clear() {
        accountBookMapper.clear();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderWithdraw> selectByStatus() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("status", 2);//提现审核成功
        criteria.orEqualTo("status", 6);//提现审核失败
        criteria.andEqualTo("effective", 1);//查询未处理
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }
}
