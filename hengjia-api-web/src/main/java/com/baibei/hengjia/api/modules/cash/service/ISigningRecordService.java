package com.baibei.hengjia.api.modules.cash.service;

import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/06/06 10:29:50
 * @description: SigningRecord服务接口
 */
public interface ISigningRecordService extends Service<SigningRecord> {


    /**
     * 根据会员代码和标识查询签约信息
     * 此方法因为方法名写错,需要修改,可以暂时不使用
     *
     * @param custAcctId
     * @return
     */
    @Deprecated
    SigningRecord findByCustAcctId(String custAcctId);

    /**
     * 根据会员代码和标识查询签约信息
     *
     * @param thirdCustId
     * @return
     */
    SigningRecord findByThirdCustId(String thirdCustId);

    /**
     * 根据 会员子账号查询签约信息
     *
     * @param custAcctId
     * @return
     */
    SigningRecord findByOneCustAcctId(String custAcctId);

    /**
     * 查询系统所有签约的客户列表
     *
     * @return
     */
    List<SigningRecord> allList();

    /**
     * 银行发起查询用户余额
     *
     * @param message
     */
    String getAcountBalance(String message);

    /**
     * 交易——银行，查询总账户余额
     *
     * @return
     */
    ApiResult queryTotalBalance();

    /**
     * 查询今天用户是否已经签约
     *
     * @param customerNo 会员代码
     * @return
     */
    Boolean isTodaySigning(String customerNo);


    /**
     * 验证身份证是否唯一性
     *
     * @param
     * @return
     */
    Boolean isOnlyIdCode(String idCode);

    Boolean isOnlyIdCode(String idCode, String customerNo);

    /**
     * 查询解约的用户并且flag为1的用户
     */
    SigningRecord findByReleaseThirdCustId(String thirdCustId);
}
