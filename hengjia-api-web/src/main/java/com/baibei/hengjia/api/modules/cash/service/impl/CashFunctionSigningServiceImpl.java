package com.baibei.hengjia.api.modules.cash.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.model.BankCard;
import com.baibei.hengjia.api.modules.account.service.IBankCardService;
import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.bean.dto.SigningRecordDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.SigningRecordVo;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.PABAnswerCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class CashFunctionSigningServiceImpl extends AbstractCashFunction<SigningRecordDto, SigningRecordVo> {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISigningRecordService signingRecordService;

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    @Autowired
    private IBankCardService bankCardService;

    @Autowired
    private ICleanDataService cleanDataService;

    /**
     * 1303交易码
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.SIGNING;
    }


    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1303(retKeyDict);
        return retKeyDict;
    }


    @Override
    public SigningRecordDto toEntityByHashMapRequest(Map<String, String> params) {
        SigningRecordDto signingRecordDto = new SigningRecordDto();
        signingRecordDto.setFuncFlag(params.get("FuncFlag"));
        signingRecordDto.setSupAcctId(params.get("SupAcctId")); // 资金汇总账号
        signingRecordDto.setCustAcctId(params.get("CustAcctId")); // 会员子账号
        signingRecordDto.setCustName(params.get("CustName")); // 会员名称

        signingRecordDto.setCustomerNo(params.get("ThirdCustId")); // 会员代码

        signingRecordDto.setIdType(params.get("IdType")); // 会员证件类型
        signingRecordDto.setIdCode(params.get("IdCode")); // 会员证件号码
        signingRecordDto.setRelatedAcctId(params.get("RelatedAcctId")); // 出入金代码
        signingRecordDto.setAcctFlag(params.get("AcctFlag")); // 账号性质
        signingRecordDto.setTranType(params.get("TranType")); //转账方式
        signingRecordDto.setAcctName(params.get("AcctName")); // 账号名称
        signingRecordDto.setBankName(params.get("BankName")); //开户行名称
        signingRecordDto.setBankCode(params.get("BankCode")); //联行号
        signingRecordDto.setOldRelatedAcctId(params.get("OldRelatedAcctId")); // 原入金账号
        signingRecordDto.setReserve(params.get("Reserve")); //保留域
        return signingRecordDto;
    }

    public SigningRecord toEntity(SigningRecordDto signingRecord) {
        SigningRecord entity = new SigningRecord();
        entity.setFuncFlag(new Byte(signingRecord.getFuncFlag())); // 功能标识
        entity.setSupAcctId(signingRecord.getSupAcctId()); // 资金汇总账号
        entity.setCustAcctId(signingRecord.getCustAcctId()); // 会员子账号
        entity.setCustName(signingRecord.getCustName()); // 会员名称
        entity.setThirdCustId(signingRecord.getCustomerNo());//会员代码
        entity.setIdType(signingRecord.getIdType()); // 会员证件类型
        entity.setIdCode(signingRecord.getIdCode()); //会员证件号码
        entity.setRelatedAcctId(signingRecord.getRelatedAcctId()); // 出入金账号
        entity.setAcctFlag(signingRecord.getAcctFlag()); // 账号性质
        if (signingRecord.getFuncFlag().equals(Constants.SigningStatus.SIGNING_CREATE)) { //签约时间
            entity.setCreateTime(new Date());
        }
        entity.setTranType(signingRecord.getTranType()); // 转账方式
        entity.setAcctName(signingRecord.getAcctName()); // 账号名称
        entity.setBankCode(signingRecord.getBankCode()); // 联行户
        entity.setBankName(signingRecord.getBankName()); // 开户行名称
        entity.setOldRelatedAcctId(signingRecord.getOldRelatedAcctId());
        entity.setReserve(signingRecord.getReserve()); // 保留域
        return entity;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> doResponse(SigningRecordDto request, Map<String, String> parmaKeyDict) {
        SigningRecord signingRecord;
        String thirdLogNo = parmaKeyDict.get("ThirdLogNo");

        Customer customer = customerService.findByCustomerNo(request.getCustomerNo());
        if (customer == null) {
            String info = "创建会员签约信息失败,会员子账号不存在";
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }
        BankCard bankCardEntity = new BankCard();
        bankCardEntity.setCustomerNo(request.getCustomerNo());
        bankCardEntity.setBankCardNo(request.getBankCode());
        bankCardEntity.setIdcard(request.getIdCode());
        bankCardEntity.setBankName(request.getBankName());
        bankCardEntity.setName(request.getCustName());
        if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_CREATE)) {
            SigningRecord signingUser = signingRecordService.findByThirdCustId(request.getCustomerNo());
            if (!signingRecordService.isOnlyIdCode(request.getIdCode())) {
                String info = "不支持身份证重复绑定银行卡";
                return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
            }
            if (signingUser != null) {
                logger.info("当前会员已经签约不需要重复签约{}", JSONObject.toJSONString(signingUser));
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "当前会员已经签约不需要重复签约");
                return parmaKeyDict;
            }
            SigningRecord releaseThirdCustId = signingRecordService.findByReleaseThirdCustId(request.getCustomerNo());
            if (releaseThirdCustId != null) {
                releaseThirdCustId.setFlag(new Byte(Constants.Flag.UNVALID));
                this.signingRecordService.update(releaseThirdCustId);
            }
            signingRecord = toEntity(request);
            signingRecord.setThirdLogNo(thirdLogNo); //签约流水号
            signingRecord.setFlag(new Byte(Constants.Flag.VALID));
            logger.info("创建签约信息开始:[{}]", JSONObject.toJSONString(signingRecord));
            signingRecordService.save(signingRecord);
            customer.setSigning(Constants.Flag.VALID); // 设置客户签约
            customer.setModifyTime(new Date()); //修改时间
            customerService.update(customer);
            bankCardEntity.setFlag(new Byte(Constants.Flag.VALID));
            saveBankCard(bankCardEntity);
        } else if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_UPDATE)) {
            signingRecord = signingRecordService.findByThirdCustId(request.getCustomerNo());
            if (signingRecord == null) {
                logger.error("修改会员签约信息失败,会员不存在");
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "修改会员签约信息失败会员不存在");
                return parmaKeyDict;
            }
            if (!this.signingRecordService.isOnlyIdCode(request.getIdCode(), request.getCustomerNo())) {
                String info = "不支持身份证重复绑定银行卡";
                return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
            }
            if (!signingRecord.getRelatedAcctId().equals(request.getOldRelatedAcctId())) { // stop1 原出入金账号不等于之前签约的出入金账号，不允许修改
                logger.error("原出入金账号不匹配");
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "原出入金账号不匹配");
            }
            request.setFuncFlag(Constants.SigningStatus.SIGNING_UPDATE);
            SigningRecord result = toEntity(request);
            result.setId(signingRecord.getId());
            result.setThirdLogNo(thirdLogNo);
            result.setUpdateTime(new Date());
            logger.info("修改会员签约信息开始:[{}]", JSONObject.toJSONString(result));
            signingRecordService.update(result);
        } else if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_DELETE)) {
            signingRecord = signingRecordService.findByThirdCustId(request.getCustomerNo());
            if (signingRecord == null) {
                logger.info("删除签约会员失败,会员不存在");
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "删除签约会员失败会员不存在");
                return parmaKeyDict;
            }
            boolean isToDayClear = cleanDataService.isCleanSuccess(request.getCustomerNo()); // 获取当前用户是否清算成功,成功可以解约
            if (!isToDayClear) {
                logger.info("非解约时间,请于一个交易日后解约,当前用户[{}]没有清算成功", request.getCustomerNo());
                parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
                parmaKeyDict.put("RspMsg", "非解约时间请于一个交易日后解约");
                return parmaKeyDict;
            }
            signingRecord.setFuncFlag(new Byte(Constants.SigningStatus.SIGNING_DELETE));
            signingRecord.setUpdateTime(new Date());
            logger.info("删除签约信息开始:[{}]", JSONObject.toJSONString(signingRecord));
            signingRecord.setUpdateTime(new Date());
            signingRecordService.update(signingRecord);
            customer.setSigning(Constants.Flag.UNVALID); // 设置客户解约
            customer.setModifyTime(new Date()); //设置修改时间
            customerService.update(customer);
            bankCardEntity.setFlag(new Byte(Constants.Flag.UNVALID)); // 设置银行的信息
            saveBankCard(bankCardEntity);
        }
        parmaKeyDict.put("RspCode", PABAnswerCodeEnum.SUCCESS.getCode());
        parmaKeyDict.put("RspMsg", PABAnswerCodeEnum.SUCCESS.getMsg());
        logger.info("签约操作成功");
        return parmaKeyDict;
    }


    @Override
    public Map<String, String> valid(SigningRecordDto request, Map<String, String> parmaKeyDict) {
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateThiredLogNo(SigningRecord.class,
                signingRecordService, "thirdLogNo"));
        parmaKeyDict.put("Reserve", ""); //保留域
        String info;
     /*   if (customer.getSigning() == null || customer.getSigning().equals(Constants.SigningStatus.UN_SIGNING)) {
            info = "用户没有在交易网中签约";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }*/
        if (request.getFuncFlag() == null) {
            info = "功能标识不能为空";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }
        if (!request.getFuncFlag().matches("1|2|3")) {
            info = "功能标识不支持";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getFuncFlag().equals("2")) { //当修改签约信息时,原出入金账号不能为空
            if (request.getOldRelatedAcctId() == null) {
                info = "修改签约信息原入金账号不能为空";
                logger.error(info);
                return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
            }
        }

        if (request.getSupAcctId() == null || request.getSupAcctId().length() > 32) {
            info = "资金汇总账号不能为空或者长度错误";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }


        if (!request.getSupAcctId().equals(supAcctId)) {
            info = "资金汇总账号不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getCustomerNo() == null) {
            info = "会员子账号不能为空";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getCustomerNo().length() > 32) {
            info = "会员子账号长度错误";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getCustName() == null || request.getCustName().length() > 120) {
            info = "会员名称不能为空或者长度不超过120位";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getCustAcctId() == null || request.getCustAcctId().length() > 32) {
            info = "会员子账号不能为空或者长度不超过32位";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getIdType() == null || request.getIdType().length() > 2) {
            info = "会员证件类型不能位空或者长度不超过2位";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getIdCode() == null || request.getIdCode().length() > 20) {
            info = "会员证件号码不能位空或者长度不超过20位";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getRelatedAcctId() == null || request.getRelatedAcctId().length() > 32) {
            info = "出入金账号不能位空或者长度不超过32位";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }
        if (request.getAcctFlag() == null || !request.getAcctFlag().matches("3")) {
            info = "账号性质不能为空或者格式不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getTranType() == null || !request.getTranType().matches("1|2|3")) {
            info = "转账方式不能为空或者格式不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getAcctName() == null || request.getAcctName().length() > 120) {
            info = "账号名称不能为空或者长度不正确";
            logger.error(info);
            parmaKeyDict.put("RspCode", PABAnswerCodeEnum.ERR074.getCode());
            parmaKeyDict.put("RspMsg", info);
            return parmaKeyDict;
        }
        if (request.getBankCode() != null && request.getBankCode().length() > 12) {
            info = "联行号长度不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }

        if (request.getBankName() != null && request.getBankName().length() > 120) {
            info = "开户行名称长度不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }
        if (request.getOldRelatedAcctId() != null && request.getOldRelatedAcctId().length() > 32) {
            info = "原出入金账号长度不正确";
            logger.error(info);
            return validResult(parmaKeyDict, PABAnswerCodeEnum.ERR074.getCode(), info);
        }
        return super.valid(request, parmaKeyDict);
    }

    public Map<String, String> validResult(Map<String, String> parmaKeyDict, String errorCode, String errorMessage) {
        parmaKeyDict.put("RspCode", errorCode);
        parmaKeyDict.put("RspMsg", errorMessage);
        return parmaKeyDict;
    }


    //根据结果生成应答报文体
    public String responseResult(Map<String, String> parmaKeyDict) {
        return interfaceMessage.getSignMessageBody_1303(parmaKeyDict);
    }

    /**
     * 如果信息不存在,则保存用户的卡号信息
     *
     * @param bankCardEntity
     */
    public void saveBankCard(BankCard bankCardEntity) {
        BankCard bankCard = bankCardService.findOneBank(bankCardEntity);
        if (bankCard == null) {
            bankCardEntity.setCreateTime(new Date());
            bankCardService.save(bankCardEntity);
        } else {
            bankCardEntity.setModifyTime(new Date());
            bankCardService.update(bankCard);
        }
    }
}
