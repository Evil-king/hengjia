package com.baibei.hengjia.api.modules.cash.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.OrderDepositVo;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.model.OrderDeposit;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderDepositService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.OrderTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.PABAnswerCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 入金有签约时间，要做限制
 * 用户用入金限制，做限制
 */
@Service
public class CashFunctionDepositServiceImpl extends AbstractCashFunction<OrderDepositDto, OrderDepositVo> {

    @Autowired
    private IOrderDepositService orderDepositService;

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ISigningRecordService signingRecordService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> doResponse(OrderDepositDto request, Map<String, String> parmaKeyDict) {
        logger.info("-----------------开始入金-------------------");

        SigningRecord signingRecord = signingRecordService.findByOneCustAcctId(request.getCustAcctId()); //根据会员子账号查询用户
        if (signingRecord == null) {
            logger.error("创建会员签约信息失败会员子账号不存在");
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode()); //应答码
            parmaKeyDict.put("RspMsg", "创建会员签约信息失败会员子账号不存在"); //应答描述
            return parmaKeyDict;
        } else {
            if (Constants.SigningStatus.SIGNING_DELETE.equals(signingRecord.getFuncFlag())) {
                logger.error("创建会员签约信息失败,会员已经解约无法入金");
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode()); //应答码
                parmaKeyDict.put("RspMsg", "创建会员签约信息失败会员子账号不存在"); //应答描述
                return parmaKeyDict;
            }
        }
        logger.info("签约用户的信息为{}", JSON.toJSONString(signingRecord));
        OrderDeposit orderDeposit;
        String externalNo = parmaKeyDict.get("externalNo"); //外部流水号
        Condition condition = new Condition(OrderDeposit.class);
        condition.createCriteria().andEqualTo("externalNo", externalNo);
        List<OrderDeposit> resultList = orderDepositService.findByCondition(condition);
        logger.info("查询请求系统流水号[{}]的入金结果:{}", externalNo,resultList.size());
        if (resultList.size() > 0) {
            logger.info("当前订单{}已经存在,状态为", resultList.get(0), resultList.get(0).getStatus());
            if (resultList.get(0).getStatus().equals(Constants.Status.SUCCESS)) {
                logger.info("该外部订单号已经成功入金不能重复入金");
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.SUCCESS.getCode());
                parmaKeyDict.put("RspMsg", "该入金订单号已经成功入金");
                return parmaKeyDict;
            } else {
                orderDeposit = resultList.get(0);
            }
        } else {
            request.setCustomerNo(signingRecord.getThirdCustId());//保存会员代码
            orderDeposit = toEntity(request);
            orderDeposit.setOrderNo(parmaKeyDict.get("ThirdLogNo"));
            orderDeposit.setExternalNo(externalNo);
            orderDepositService.save(orderDeposit);
            logger.info("新增入金订单为:[{}]", JSONObject.toJSONString(orderDeposit));
        }
        try {
            accountService.updateWithDraw(request.getCustomerNo(), request.getTranAmount(), orderDeposit.getOrderNo(), new Byte("101"), new Byte("2"));
        } catch (Exception ex) {
            logger.error("增加用户余额失败,异常为:[{}]", ex.getMessage());// 增加余额失败,修改订单状态
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", "增加用户余额失败");
            orderDeposit.setStatus(Constants.Status.FAIL);
            orderDepositService.update(orderDeposit);
            return parmaKeyDict;
        }
        orderDeposit.setStatus(Constants.Status.SUCCESS);
        orderDepositService.update(orderDeposit);
        parmaKeyDict.put("RspCode", PABAnswerCodeEnum.SUCCESS.getCode()); // 应答码
        parmaKeyDict.put("RspMsg", "交易成功");
        return parmaKeyDict;
    }


    /**
     * 1310
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.DEPOSIT;
    }


    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1310(retKeyDict);
        return retKeyDict;
    }

    /**
     * 业务逻辑处理
     *
     * @param parmaKeyDict
     * @return
     */
    @Override
    public String responseResult(Map<String, String> parmaKeyDict) {
        return interfaceMessage.getSignMessageBody_1310(parmaKeyDict);
    }


    @Override
    public OrderDepositDto toEntityByHashMapRequest(Map<String, String> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        OrderDepositDto orderDepositDto = new OrderDepositDto();
        orderDepositDto.setSupAcctId(params.get("SupAcctId")); // 资金汇总账号
        orderDepositDto.setCustAcctId(params.get("CustAcctId")); // 会员子账号
        orderDepositDto.setTranAmount(new BigDecimal(params.get("TranAmount"))); // 入金金额
        orderDepositDto.setInAcctId(params.get("InAcctId")); // 入金账号
        orderDepositDto.setInAcctIdName(params.get("InAcctIdName"));
        orderDepositDto.setCcyCode(params.get("CcyCode"));
        try {
            orderDepositDto.setAcctDate(sdf.parse(params.get("AcctDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderDepositDto.setReserve(params.get("Reserve"));
        return orderDepositDto;
    }

    @Override
    public Map<String, String> valid(OrderDepositDto request, Map<String, String> parmaKeyDict) {
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateOrderNo(OrderDeposit.class, orderDepositService, "D", "orderNo"));
        parmaKeyDict.put("Reserve", ""); // 保留域
        String info;
        if (request.getSupAcctId() == null || request.getSupAcctId().length() > 32) {
            info = "资金汇总账号不能为空或者长度错误";
            logger.error(info);
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            return parmaKeyDict;
        }

        if (request.getCustAcctId() == null || request.getSupAcctId().length() > 32) {
            info = "会员子账号不能为空或者长度错误";
            logger.error(info);
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode()); //应答码
            parmaKeyDict.put("RspMsg", info); //应答描述
            return parmaKeyDict;
        }

        if (parmaKeyDict.get("externalNo") == null) {
            info = "请求方系统流水号不能为空";
            logger.error(info);
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode()); //应答码
            parmaKeyDict.put("RspMsg", info); //应答描述
            return parmaKeyDict;
        }

        if (!request.getSupAcctId().equals(supAcctId)) {
            info = "资金汇总账号不正确";
            logger.error(info);
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            return parmaKeyDict;
        }

        if (request.getAcctDate() == null) {
            info = "会计日期不能为空";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        if (request.getTranAmount() == null) {
            info = "交易金额不能为空";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }
        if (request.getTranAmount().compareTo(BigDecimal.ZERO) < 1) {
            info = "交易金额不能小于0或者等于0";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            return parmaKeyDict;
        }
        if (request.getTranAmount().compareTo(new BigDecimal("100000000000000")) > 1) {
            info = "交易金额长度超过限制";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        if (request.getInAcctId() == null || request.getInAcctId().length() > 32) {
            info = "入金账号不能为空或者长度错误";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        if (request.getInAcctIdName() == null || request.getInAcctIdName().length() > 120) {
            info = "入金账户名称不能为空或者长度错误";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        if (request.getCcyCode() == null || request.getCcyCode().length() > 3) {
            info = "币种不能为空或者长度不正确";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        if (request.getReserve() == null || request.getReserve().length() > 120) {
            info = "保留域不能为空或者长度错误";
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            logger.error(info);
            return parmaKeyDict;
        }

        return super.valid(request, parmaKeyDict);
    }

    public OrderDeposit toEntity(OrderDepositDto orderDepositDto) {
        OrderDeposit orderDeposit = new OrderDeposit();
        orderDeposit.setAccount(orderDepositDto.getInAcctId()); //入金账号
        orderDeposit.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType()); // 入金类型
        orderDeposit.setAccountName(orderDepositDto.getInAcctIdName()); // 入金账号名称
        orderDeposit.setCustomerNo(orderDepositDto.getCustomerNo()); // 会员代码
        orderDeposit.setOrderAmt(orderDepositDto.getTranAmount()); // 入金金额
        orderDeposit.setCreateTime(new Date()); // 创建时间
        orderDeposit.setFlag(new Byte(Constants.Flag.VALID)); // 未删除
        orderDeposit.setAcctDate(orderDepositDto.getAcctDate()); // 银行记账日期
        orderDeposit.setCcyCode(orderDepositDto.getCcyCode()); // 币种
        orderDeposit.setSupAcctId(orderDepositDto.getSupAcctId()); //资金汇总账号
        orderDeposit.setCustAcctId(orderDepositDto.getCustAcctId()); // 会员子账号
        return orderDeposit;
    }
}
