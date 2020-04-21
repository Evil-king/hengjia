package com.baibei.hengjia.api.modules.user.web.api;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.sms.util.RandomUtils;
import com.baibei.hengjia.api.modules.sms.util.SmsUtil;
import com.baibei.hengjia.api.modules.user.bean.dto.ForgetPasswordDto;
import com.baibei.hengjia.api.modules.user.bean.dto.LoginDto;
import com.baibei.hengjia.api.modules.user.bean.dto.MobileDto;
import com.baibei.hengjia.api.modules.user.bean.dto.RegisterDto;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerTokenVo;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.StatisticalCustomerVo;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.baibei.hengjia.common.tool.utils.VerificationCodeUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: hyc
 * @date: 2019/5/24 9:49 AM
 * @description:
 */
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class ApiCustomerControllor {
    @Autowired
    private ICustomerService CustomerService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IPubSmsService pubSmsService;

    @Autowired
    private SmsUtil smsUtil;


    /**
     * 注册
     * @param registerDto
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiResult<CustomerTokenVo> register(@RequestBody  @Validated RegisterDto registerDto){
        log.info("注册收到的信息为"+registerDto);
        ApiResult result=new ApiResult();
        if(!(registerDto.getUsername().length()>=6 && registerDto.getUsername().length()<=20 && registerDto.getPassword().length()<=20 && registerDto.getPassword().length()>=6)){
            result.setCode(ResultEnum.REGISTER_PARAM_NONSTANDARD.getCode());
            result.setMsg(ResultEnum.REGISTER_PARAM_NONSTANDARD.getMsg());
            return result;
        }
        if(!registerDto.getPassword().equals(registerDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        //判断手机号是否被注册过了
        if(CustomerService.checkMobileRegistered(registerDto.getMobile())){
            result.setCode(ResultEnum.USER_EXIST.getCode());
            result.setMsg(ResultEnum.USER_EXIST.getMsg());
            return result;
        }
        //判断用户名是否重复
        if(CustomerService.checkUsername(registerDto.getUsername())!=null){
            result.setCode(ResultEnum.USERNAME_EXIST.getCode());
            result.setMsg(ResultEnum.USERNAME_EXIST.getMsg());
            return result;
        }
        ApiResult validateResult=pubSmsService.validateCode(registerDto.getMobile(),registerDto.getMobileVerificationCode());
        log.info("收到的短信认证结果为"+validateResult);
        if(!validateResult.hasSuccess()){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        //通过判断邀请码确定直属推荐人的ID
        Customer customer = CustomerService.findByCustomerNo(registerDto.getInvitationCode());
        if (null == customer) {
            result.setCode(ResultEnum.INVITER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.INVITER_NOT_EXIST.getMsg());
            return result;
        }
        ApiResult<CustomerTokenVo> customerResult = CustomerService.register(registerDto, customer);
        accountService.register(customerResult.getData().getCustomerNo());
        return customerResult;

    }
    @PostMapping("/login")
    @ResponseBody
    public ApiResult<CustomerTokenVo> login(@RequestBody @Validated LoginDto loginDto, HttpServletRequest request) {
        ApiResult result = new ApiResult();
        //通过交易账号判断是交易商编码或者是手机号，再通过不同的字段使用不同的方法查询用户的信息
        Customer customer = CustomerService.checkUsername(loginDto.getUsername());
        if (null == customer) {
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        if(customer.getCustomerStatus().indexOf("101")!=-1){
            //用户状态如果存在限制登录状态
            result.setCode(ResultEnum.LIMIT_LOGIN.getCode());
            result.setMsg(ResultEnum.LIMIT_LOGIN.getMsg());
            return result;
        }
        //从redis查看当前账户是否输入密码错误达到上限
        Boolean flag=CustomerService.checkLoginFailCount(customer.getCustomerNo());
        if(flag){
            result.setCode(ResultEnum.USER_LOCK.getCode());
            result.setMsg(ResultEnum.USER_LOCK.getMsg());
            return result;
        }
        //
        ApiResult<CustomerTokenVo> apiResult=CustomerService.login(customer,loginDto,request);

        return apiResult;
    }

    /**
     *
     * @param forgetPasswordDto
     * @return
     */
    @PostMapping("/forgetPassword")
    @ResponseBody
    public ApiResult<String> forgetPassword(@RequestBody @Validated ForgetPasswordDto forgetPasswordDto){
        ApiResult<String> result=new ApiResult<String>();
        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        //先判断该手机号是否存在用户
        Customer customer=CustomerService.findByMobile(forgetPasswordDto.getMobile());
        if(null==customer){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        ApiResult validateResult=pubSmsService.validateCode(forgetPasswordDto.getMobile(),forgetPasswordDto.getMobileVerificationCode());
        if(!validateResult.hasSuccess()){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        //存在则进行重置密码操作
        return CustomerService.resetPassword(customer,forgetPasswordDto.getPassword());
    }
    /**
     *  生成一个验证码
     * @param request 暂时不需要图片验证码
     */
    @PostMapping("/verificationCode")
    @ResponseBody
    public ApiResult<String> getVerificationCodeImage(HttpServletRequest request) {
        ApiResult result = new ApiResult();
        //生成一个随机四位数验证码
        String randomData = VerificationCodeUtils.getRandomCode(4);
        if (randomData == null) {
            return ApiResult.error("验证码生成失败！");
        }
        // 将系统生成的文本内容保存到session中
        HttpSession session = request.getSession(true);
        //存储验证码数据到Session中
        session.setAttribute("VerificationCode", randomData);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(randomData);
        return result;
    }
    /**
     * 请求手机短信验证码 1：注册 2：忘记密码
     * @param mobileDto    手机号以及短信类型
     * @return
     */
    @PostMapping("/requestMobileVerificationCode")
    @ResponseBody
    public ApiResult<String>  requestMobileVerificationCode(@RequestBody @Validated MobileDto mobileDto){
        ApiResult result=new ApiResult();
        if(!MobileUtils.isMobileNO(mobileDto.getMobile())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        //先判断该手机号是否存在用户
        Customer customer=CustomerService.findByMobile(mobileDto.getMobile());
        if("1".equals(mobileDto.getType())){
            //注册判断不存在
            if(null!=customer){
                result.setCode(ResultEnum.USER_EXIST.getCode());
                result.setMsg(ResultEnum.USER_EXIST.getMsg());
                return result;
            }
        }else  if("2".equals(mobileDto.getType())){
            //忘记密码判断存在
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
        }else  if("3".equals(mobileDto.getType())){
            //设置资金密码
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
        }else  if("5".equals(mobileDto.getType())){
            //忘记密码判断存在
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
        }else {
            return ApiResult.badParam("短信类型错误");
        }
        //发送短信并且入库
        String code = RandomUtils.getRandomNumber(6);
        String sms = smsUtil.operatorSms("1", mobileDto.getType(), code, mobileDto.getMobile());
        return pubSmsService.getSms(mobileDto.getMobile(), sms);
    }

    @GetMapping("/getCustomerList")
    @ResponseBody
    public ApiResult<StatisticalCustomerVo> getCustomerList(@RequestParam  String customerNo,@RequestParam String date){
        return ApiResult.success(CustomerService.getCustomerList(customerNo,date));
    }

}
