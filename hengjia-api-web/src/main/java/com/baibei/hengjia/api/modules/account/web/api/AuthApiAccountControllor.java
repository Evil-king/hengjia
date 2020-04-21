package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.bean.dto.InsertPasswordDto;
import com.baibei.hengjia.api.modules.account.bean.dto.SigningDataDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.bean.vo.FundInformationVo;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.IBankCardService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.api.modules.user.bean.dto.ForgetPasswordDto;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerDetailService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.IDCardUtils;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.IDCardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/28 16:14
 * @description:
 */
@Controller
@RequestMapping("/auth/api/account")
public class AuthApiAccountControllor {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IHoldTotalService iHoldTotalService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDetailService customerDetailService;

    @Autowired
    private IPubSmsService pubSmsService;

    @Autowired
    private IBankCardService bankCardService;

    @Value("${signing.personalOrEnterprise}")
    private String signingEnterprise;
    @Autowired
    private ICouponAccountService couponAccountService;

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public ApiResult<String> updatePassword(@RequestBody @Validated UpdatePasswordDto updatePasswordDto){
        return accountService.updatePassword(updatePasswordDto);
    }
    /**
     * 签约存入数据
     * @param insertPasswordDto
     * @return
     */
    @PostMapping("/signingData")
    @ResponseBody
    public ApiResult<String> signingData(@RequestBody @Validated SigningDataDto insertPasswordDto){
        ApiResult result=new ApiResult();
        //判断签约者年龄
        if(Constants.Identity.PERSONAL.equals(insertPasswordDto.getIdentity())){
            Integer age=IDCardUtils.getAgeByIDNumber(insertPasswordDto.getCertificateNo());
            if(age>=65||age<18){
                result.setCode(ResultEnum.SIGNING_AGE_ERROR.getCode());
                result.setMsg(ResultEnum.SIGNING_AGE_ERROR.getMsg());
                return result;
            }
        }
        //密码校验
        if(!(insertPasswordDto.getPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        if(Constants.Identity.ENTERPRISE.equals(insertPasswordDto.getIdentity())&&("true".equals(signingEnterprise))){
            return ApiResult.badParam("当前暂不支持企业用户签约");
        }
        Customer customer=customerService.findByCustomerNo(insertPasswordDto.getCustomerNo());
        if(customer==null){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        bankCardService.insertOne(insertPasswordDto);
        return accountService.insertFundPassword(insertPasswordDto.getCustomerNo(),insertPasswordDto.getPassword());
    }
    @PostMapping("/insertFundPassword")
    @ResponseBody
    public ApiResult insertFundPassword(@RequestBody @Validated InsertPasswordDto insertPasswordDto){
        if(!(insertPasswordDto.getPassword().matches("\\d{6}")&&insertPasswordDto.getRepeatPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        if(!insertPasswordDto.getPassword().equals(insertPasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次密码输入不一致");
        }
        return accountService.insertFundPassword(insertPasswordDto.getCustomerNo(),insertPasswordDto.getPassword());
    }

    @PostMapping("/forgetPassword")
    @ResponseBody
    public ApiResult<String> forgetPassword(@RequestBody @Validated ForgetPasswordDto forgetPasswordDto){
        ApiResult<String> result=new ApiResult<String>();
        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        //通过手机号查询到交易商账号
        ApiResult<String> apiResult=customerService.findCustomerNoByMobile(forgetPasswordDto.getMobile());
        if(!apiResult.hasSuccess()){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        if(!apiResult.getData().equals(forgetPasswordDto.getCustomerNo())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        ApiResult validateResult=pubSmsService.validateCode(forgetPasswordDto.getMobile(),forgetPasswordDto.getMobileVerificationCode());
        if(!validateResult.hasSuccess()){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        //存在则进行重置密码操作
        return accountService.forgetPassword(apiResult.getData(),forgetPasswordDto.getPassword());
    }
    /**
     * 资金信息
     * @param customerNoDto
     * @return
     */
    @PostMapping("/fundInformation")
    @ResponseBody
    public ApiResult<FundInformationVo> fundInformation(@RequestBody @Validated CustomerNoDto customerNoDto){
        //先从交易那里拿到持仓市值
        BigDecimal marketValue=iHoldTotalService.marketValue(customerNoDto.getCustomerNo()).getMarketValue();
        return accountService.fundInformation(customerNoDto,marketValue);
    }

    @PostMapping("/couponAccountList")
    @ResponseBody
    public ApiResult<MyPageInfo<CouponAccountVo>> update(@RequestBody @Validated CouponAccountDto couponAccountDto){
        MyPageInfo<CouponAccountVo> listMyPageInfo = couponAccountService.myList(couponAccountDto);
        return ApiResult.success(listMyPageInfo);
    }
}
