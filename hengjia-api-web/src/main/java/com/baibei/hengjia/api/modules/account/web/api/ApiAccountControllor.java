package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAndIntegralAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CustomerDealOrderVo;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/31 9:55
 * @description:
 */
@Controller
@RequestMapping("/api/account")
@Slf4j
public class ApiAccountControllor {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IPubSmsService pubSmsService;

    @Autowired
    private ICouponAccountService couponAccountService;


    @Autowired
    private ICleanDataService cleanDataService;
    @GetMapping("/updateWithdrawTiming")
    @ResponseBody
    public ApiResult updateWithdrawTiming(){
        List<String> cusmerNoList =cleanDataService.findCleanSuccessCustomerNo(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        if(cusmerNoList!=null){
            for (int i = 0; i < cusmerNoList.size(); i++) {
                accountService.updateWithdrawTiming(cusmerNoList.get(i));
            }
            return ApiResult.success();
        }else {
            return ApiResult.badParam("无清算成功用户");
        }
    }

    @GetMapping("/changeCouponAndIntegralAmount")
    @ResponseBody
    public ApiResult changeCouponAndIntegralAmount(){
        //查询用户字段deduction为0并且购买大于等于5笔成交单
        List<CustomerDealOrderVo> customerDealOrderVoList = accountService.selectByCustomerDealOrder();
        customerDealOrderVoList.stream().filter(customerDealOrderVo -> customerDealOrderVo.getCount() >= 5)
                .forEach(customerDealOrderVo -> {
            //执行扣减方法
            ChangeCouponAndIntegralAmountDto changeCouponAndIntegralAmountDto = new ChangeCouponAndIntegralAmountDto();
            changeCouponAndIntegralAmountDto.setCustomerNo(customerDealOrderVo.getCustomerNo());
            //积分对象
            ChangeIntegralDto changeIntegralDto = new ChangeIntegralDto();
            changeIntegralDto.setIntegralNo(101L);
            changeIntegralDto.setChangeAmount(new BigDecimal(25000));
            changeIntegralDto.setCustomerNo(customerDealOrderVo.getCustomerNo());
            changeIntegralDto.setOrderNo(CodeUtils.generateTreeOrderCode());
            changeIntegralDto.setReType((byte)1);
            changeIntegralDto.setTradeType((byte) IntegralTradeTypeEnum.DELIVERY.getCode());
            changeCouponAndIntegralAmountDto.setChangeIntegralDto(changeIntegralDto);
            //券对象
            ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
            changeCouponAccountDto.setCouponType("deliveryTicket");
            changeCouponAccountDto.setProductTradeNo("0002");
            changeCouponAccountDto.setChangeAmount(new BigDecimal(11600));
            changeCouponAccountDto.setOrderNo(CodeUtils.generateTreeOrderCode());
            changeCouponAccountDto.setReType((byte)1);
            changeCouponAccountDto.setCustomerNo(customerDealOrderVo.getCustomerNo());
            changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.DELIVERY.getCode());
            changeCouponAndIntegralAmountDto.setChangeCouponAccountDto(changeCouponAccountDto);
            accountService.changeCouponAndIntegralAmount(changeCouponAndIntegralAmountDto);
        });
        return ApiResult.success();
    }
}
