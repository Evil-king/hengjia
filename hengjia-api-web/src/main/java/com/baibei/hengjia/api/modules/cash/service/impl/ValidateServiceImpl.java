package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.dao.OrderWithdrawMapper;
import com.baibei.hengjia.api.modules.cash.dao.SigningRecordMapper;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISignInBackService;
import com.baibei.hengjia.api.modules.cash.service.IValidateService;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.Utils;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hwq
 * @date 2019/06/06
 * <p>
 * 出金前的一系列操作(验证参数、创建订单)
 * </p>
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ValidateServiceImpl implements IValidateService {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private SigningRecordMapper signingRecordMapper;
    @Autowired
    private OrderWithdrawMapper orderWithdrawMapper;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    PropertiesVal propertiesVal;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private ISignInBackService signInBackService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;


    @Override
    public ApiResult validate(OrderWithdrawDto orderWithdrawDto) {
        boolean flag = Boolean.FALSE;
        ApiResult apiResult = new ApiResult();

        //判断是否是交易日时间
        flag = tradeDayService.isTradeDay(new Date());
        if (!flag) {
            apiResult.setCode(-301);
            apiResult.setMsg("非交易日时间");
            return apiResult;
        }

        //判断是否签退，签退就不能出金
        if(signInBackService.isToDaySignInBack(Constants.SigningInBack.SIGNING_IN) == false
        || signInBackService.isToDaySignInBack(Constants.SigningInBack.SIGNING_BACK) == true){
            return ApiResult.error("非提现时间");
        }

        //判断是否是出入金时间
        flag = Utils.compareTime(propertiesVal.getDepositTime(), propertiesVal.getWithdrawTime());
        if (!flag) {
            apiResult.setCode(-301);
            apiResult.setMsg("非出入金时间");
            return apiResult;
        }

        //出金金额判断
        if(orderWithdrawDto.getOrderAmt().toString().indexOf("-") != -1
        && orderWithdrawDto.getOrderAmt().compareTo(new BigDecimal(0)) == -1){
            return ApiResult.error("提现金额不符合要求");
        }

        //判断余额是否足够、判断资金密码是否正确
        apiResult = accountService.getWithdrawCash(orderWithdrawDto.getCustomerNo(), orderWithdrawDto.getOrderAmt(), orderWithdrawDto.getPassword());
        if (apiResult.getCode()!= 200) {
            apiResult.setCode(-301);
            return apiResult;
        }

        //判断用户状态以及判断出金账号是否存在
        CustomerVo customerVo = customerService.findUserByCustomerNo(orderWithdrawDto.getCustomerNo());
        if (customerVo == null) {
            log.info("用户信息，customerVo={}", customerVo);
            return ApiResult.error("用户为信息有误");
        }

        if("0".equals(customerVo.getSigning())){
            log.info("用户未签约，sign={}", customerVo.getSigning());
            return ApiResult.error("用户未签约");
        }

        if (customerVo.getCustomerStatus().indexOf("103") != -1) {
            return ApiResult.error("账号状态异常，请联系客户");
        }

        return ApiResult.success();
    }

    @Override
    public OrderWithdraw createOrder(OrderWithdrawDto orderWithdrawDto) {

        if (!"dealDiff".equals(orderWithdrawDto.getType())) {
            if (orderWithdrawDto.getPassword() == null || "".equals(orderWithdrawDto.getPassword())) {
                throw new ServiceException("资金密码不能为空");
            }
        }
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("thirdCustId", orderWithdrawDto.getCustomerNo());

        List<SigningRecord> signingRecords = signingRecordMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(signingRecords)) {
            return null;
        }
        String orderNo = CodeUtils.generateTreeOrderCode();

        //创建出金订单
        OrderWithdraw orderWithdraw = new OrderWithdraw();
        orderWithdraw.setCustomerNo(orderWithdrawDto.getCustomerNo());//会员编号
        orderWithdraw.setAccount(signingRecords.get(0).getRelatedAcctId());//出金账号
        orderWithdraw.setAccountName(orderWithdrawDto.getAccountName());//出金账户名称
        orderWithdraw.setBankName(orderWithdrawDto.getBankName());//银行用户名称
        orderWithdraw.setCreateTime(new Date());
        orderWithdraw.setUpdateTime(new Date());
        orderWithdraw.setFlag((byte) 1);//未删除
        orderWithdraw.setEffective((byte) 1);//未处理
        //计算手续费
        BigDecimal Fee = Utils.getFee(orderWithdrawDto.getOrderAmt(), propertiesVal.getRate(), propertiesVal.getFee());
        orderWithdraw.setHandelFee(Fee);//手续费
        orderWithdraw.setExternalNo("dealDiff".equals(orderWithdrawDto.getType())?orderWithdrawDto.getExternalNo():"");//外部订单号
        if ("dealDiff".equals(orderWithdrawDto.getType())) {
            orderWithdraw.setStatus(orderWithdrawDto.getStatus());//状态
        } else {
            if(compairAmount(orderWithdrawDto.getCustomerNo(),orderWithdrawDto.getOrderAmt()) == true){
                orderWithdraw.setStatus("1");//状态
            } else {
                orderWithdraw.setStatus("2");//不需要后台审核
                orderWithdraw.setReviewer("系统");
                orderWithdraw.setReviewerTime(new Date());
            }

        }
        orderWithdraw.setOrderType("dealDiff".equals(orderWithdrawDto.getType())?"3":"1");//出金类型
        orderWithdraw.setRemarks("");
        orderWithdraw.setHandelFee(Utils.getFee(orderWithdrawDto.getOrderAmt(), propertiesVal.getRate(), propertiesVal.getFee()));//出金手续费
        //出金金额需要扣减手续费
        BigDecimal amount = orderWithdrawDto.getOrderAmt().subtract(Fee);
        orderWithdraw.setOrderamt(amount);
        orderWithdraw.setOrderNo(orderNo);//出金订单号
        orderWithdraw.setSupAcctId(signingRecords.get(0).getSupAcctId());//资金汇总账号
        orderWithdrawMapper.insert(orderWithdraw);

        //扣除手续费
        accountService.updateWithDraw(orderWithdrawDto.getCustomerNo(), Fee, orderNo,
                (byte) FundTradeTypeEnum.MONEY_OUT_FEE.getCode(), (byte) 1);
        //交易扣钱
        accountService.updateWithDraw(orderWithdrawDto.getCustomerNo(), orderWithdrawDto.getOrderAmt().subtract(Fee), orderNo,
                (byte) FundTradeTypeEnum.MONEY_OUT.getCode(), (byte) 1);
        return orderWithdraw;
    }

    public boolean compairAmount(String customerNo,BigDecimal money){
        BigDecimal sumAmount = orderWithdrawService.sumAmountOfCustomer(customerNo);
        if(sumAmount == null){
            sumAmount = BigDecimal.ZERO;
        }
        log.info("用户当日成功出金金额,sumAmount={}", sumAmount);
        BigDecimal amount = sumAmount.add(money);
        log.info("对比金额,amount={}", amount);
        if(amount.compareTo(propertiesVal.getWithdrawalAmount()) > -1){
            return true;
        } else {
            return false;
        }
    }

}
