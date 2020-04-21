package com.baibei.hengjia.api.modules.account.service;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAndIntegralAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CustomerDealOrderVo;
import com.baibei.hengjia.api.modules.account.bean.vo.FundInformationVo;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.settlement.bean.vo.AccountInfo;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:39:31
* @description: Account服务接口
*/
public interface IAccountService extends Service<Account> {
    ApiResult<String> register(String customerNo);

    /**
     * 修改余额（业务系统的可用余额，资管系统请使用修改可提现金额接口）
     * @param accountBalanceDto
     * @return
     */
    void changeAccount(ChangeAmountDto accountBalanceDto) ;

    /**
     * 同时修改积分和提货券（特殊情况下，允许穿仓）
     * @param changeCouponAndIntegralAmountDto
     * @return
     */
    void changeCouponAndIntegralAmount(ChangeCouponAndIntegralAmountDto changeCouponAndIntegralAmountDto) ;

    /**
     * 修改冻结金额
     * @param frozenAmountDto
     * @return
     */
    void changeFreezenAmountByType(ChangeAmountDto frozenAmountDto) ;
    /**
     * 成交解冻扣除金额
     * @param frozenAmountDto
     * @return
     */
    void thawAmountByTrade(ChangeAmountDto frozenAmountDto) ;
    /**
     * 通过用户编码查看账户
     * @param customerNo
     * @return
     */
    Account checkAccount(String customerNo);

    /**
     * 修改资金密码
     * @param updatePasswordDto
     * @return
     */
    ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    /**
     * 资金信息
     * @param customerNoDto
     * @param marketValue
     * @return
     */
    ApiResult<FundInformationVo> fundInformation(CustomerNoDto customerNoDto, BigDecimal marketValue);

    /**
     * 忘记密码
     * @param customerNo
     * @param password
     * @return
     */
    ApiResult<String> forgetPassword(String customerNo, String password);

    /**
     * 修改可提现金额
     * @param customerNo 用户编号
     * @param password 资金密码
     * @param amount 修改数额
     * @param orderNo 订单号
     * @param tradeType 交易类型
     * @param reType 1：出金 2:入金
     * @return
     */
    String updateWithDrawWithPassword(String customerNo,String password,BigDecimal amount,String orderNo,Byte tradeType,Byte reType);

    /**
     * 修改可提现金额,无资金密码方法
     * @param customerNo 用户编号
     * @param amount 修改数额
     * @param orderNo 订单号
     * @param tradeType 交易类型
     * @param reType 1：出金 2:入金
     * @return
     */
    String updateWithDraw(String customerNo,BigDecimal amount,String orderNo,Byte tradeType,Byte reType);

    ApiResult getWithdrawCash(String customerNo,BigDecimal amount,String password) ;

    ApiResult<String> insertFundPassword(String customerNo,String password);

    /**
     * 定时操作
     */
    void updateWithdrawTiming(String customerNo) ;

    /**
     * 查询系统所有的账户列表
     *
     * @return
     */
    List<AccountInfo> allAccountList();

    /**
     * /查询用户字段deduction为0并且购买大于等于5笔成交单
     * @return
     */
    List<CustomerDealOrderVo> selectByCustomerDealOrder();
}
