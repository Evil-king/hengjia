package com.baibei.hengjia.api.modules.account.service;

import com.baibei.hengjia.api.modules.account.bean.dto.SigningDataDto;
import com.baibei.hengjia.api.modules.account.model.BankCard;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: hyc
 * @date: 2019/06/03 14:40:16
 * @description: BankCard服务接口
 */
public interface IBankCardService extends Service<BankCard> {
    /**
     * 签约方法，暂时还未确定此信息是否正确，所以此方法插入的时候flag为0
     *
     * @param insertPasswordDto
     */
    void insertOne(SigningDataDto insertPasswordDto);


    /**
     * 查询用户身份卡号
     */
    BankCard findOneBank(BankCard bankCard);
}
