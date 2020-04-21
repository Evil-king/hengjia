package com.baibei.hengjia.api.modules.user.web.api;


import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.user.bean.dto.AuthenticationDto;
import com.baibei.hengjia.api.modules.user.bean.dto.InviteCodeDto;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.InvitationCodeCustomerVo;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/28 16:05
 * @description:
 */
@Controller
@RequestMapping("/auth/api/customer")
public class AuthApiCustomerControllor {
    @Autowired
    private ICustomerService customerService;

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public ApiResult<String> updatePassword(@RequestBody @Validated UpdatePasswordDto updatePasswordDto){

        return customerService.updatePassword(updatePasswordDto);
    }

    /**
     * 生成二维码并且将地址返回给前端
     * @param customerNoDto
     * @return
     */
    @PostMapping("/createQrCode")
    @ResponseBody
    public ApiResult<String> createQrCode(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerService.createQrCode(customerNoDto);
    }

    /**
     * 通过邀请码获取名下用户
     * @param inviteCodeDto
     * @return
     */
    @PostMapping("/findByInviteCode")
    @ResponseBody
    public ApiResult<MyPageInfo<InvitationCodeCustomerVo>> findByInviteCode(@RequestBody @Validated InviteCodeDto inviteCodeDto) {

        return ApiResult.success(customerService.findByInviteCode(inviteCodeDto));
    }

    /**
     * 实名认证
     * @param
     * @return
     */
    @PostMapping("/authentication")
    @ResponseBody
    public ApiResult authentication(@RequestParam String customerNo,@RequestParam String realname,@RequestParam String idcard, @RequestParam(value = "idcardFront") MultipartFile frontFile,@RequestParam(value = "idcardBack") MultipartFile backFile) {
        return customerService.authentication(customerNo,realname,idcard,frontFile,backFile);
    }

    /**
     * 根据用户编号查询用户信息
     * @param customerNoDto
     * @return
     */
    @PostMapping("/getCustomer")
    @ResponseBody
    public ApiResult<CustomerVo> getCustomer(@RequestBody @Validated CustomerNoDto customerNoDto){
        return ApiResult.success(customerService.findUserByCustomerNo(customerNoDto.getCustomerNo()));
    }
    /**
     * 退出登录
     * @param customerNoDto
     * @return
     */
    @PostMapping("/exitLogin")
    @ResponseBody
    public ApiResult<String> exitLogin(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerService.exitLogin(customerNoDto.getCustomerNo());
    }

    @GetMapping("/updateOpenid")
    @ResponseBody
    public ApiResult updateOpenid(@RequestParam(name = "customerNo") String customerNo, @RequestParam(name = "openId") String openId) {
        return ApiResult.success(customerService.updateOpenid(customerNo,openId));
    }
}
