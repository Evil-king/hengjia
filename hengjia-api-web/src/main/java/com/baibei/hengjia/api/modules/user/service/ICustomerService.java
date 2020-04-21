package com.baibei.hengjia.api.modules.user.service;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.user.bean.dto.InviteCodeDto;
import com.baibei.hengjia.api.modules.user.bean.dto.LoginDto;
import com.baibei.hengjia.api.modules.user.bean.dto.RegisterDto;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerTokenVo;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.InvitationCodeCustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.StatisticalCustomerVo;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
* @author: hyc
* @date: 2019/06/03 14:42:36
* @description: Customer服务接口
*/
public interface ICustomerService extends Service<Customer> {
    /**
     * 注册
     * @param registerDto
     * @param customer
     * @return
     */
    ApiResult<CustomerTokenVo> register(RegisterDto registerDto, Customer customer);

    /**
     * 登录
     * @param customer
     * @param loginDto
     * @param request
     * @return
     */
    ApiResult<CustomerTokenVo> login(Customer customer, LoginDto loginDto, HttpServletRequest request);

    /**
     * 判断用户登录错误次数
     * @param customerNo
     * @return
     */
    Boolean checkLoginFailCount(String customerNo);


    /**
     * 判断手机是否注册
     * @param mobile
     * @return
     */
    boolean checkMobileRegistered(String mobile);

    /**
     * 修改用户缓存信息
     * @param customer
     */
    void updateRedisUser(Customer customer);

    /**
     * 查看用户信息
     * @param customerNo
     * @return
     */
    CustomerVo findUserByCustomerNo(String customerNo);

    /**
     * 重置密码
     * @param customer
     * @param password
     * @return
     */
    ApiResult<String> resetPassword(Customer customer,String password);

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    /**
     * 通过手机号获取用户编号
     * @param mobile
     * @return
     */
    ApiResult<String> findCustomerNoByMobile(String mobile);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    Customer checkUsername(String username);

    /**
     * 通过手机号获取用户
     * @param mobile
     * @return
     */
    Customer findByMobile(String mobile);

    /**
     * 通过用户编号获取二维码
     * @param customerNoDto
     * @return
     */
    ApiResult<String> createQrCode(CustomerNoDto customerNoDto);

    /**
     * 通过用户编号查询名下所有用户
     * @param inviteCodeDto
     * @return
     */
    MyPageInfo<InvitationCodeCustomerVo> findByInviteCode(InviteCodeDto inviteCodeDto);

    /**
     * 身份认证
     * @param customerNo
     * @param realname
     * @param idcard
     * @param frontFile
     * @param backFile
     * @return
     */
    ApiResult authentication(String customerNo, String realname, String idcard, MultipartFile frontFile, MultipartFile backFile);

    /**
     * 退出登录
     * @param customerNo
     * @return
     */
    ApiResult<String> exitLogin(String customerNo);

    /**
     * 修改openId
     * @param customerNo
     * @param openId
     * @return
     */
    boolean updateOpenid(String customerNo,String openId);

    /**
     * 通过用户编号查找用户
     * @param customerNo
     * @return
     */
    Customer findByCustomerNo(String customerNo);



    CustomerVo selectByOpenid(String openid);

    /**
     * 通过用户编号找到名下用户所有的一些基础信息
     * @param customerNo
     * @return
     */
    StatisticalCustomerVo getCustomerList(String customerNo,String date);


    /**
     * 通过商品交易编号获取对应的挂牌商用户编号
     * @param productTradeNo
     * @return
     */
    String getMemberCustomerNoByProductNo(String productTradeNo);

    /**
     * 获取对应的经销商用户编号
     * @param
     * @return
     */
    String getDistributorNo();

    /**
     * 更新用户is_new字段
     * @return
     */
    int updateByCustomer(Customer customer);
}
