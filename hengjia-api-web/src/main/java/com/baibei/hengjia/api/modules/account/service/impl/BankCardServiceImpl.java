package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.SigningDataDto;
import com.baibei.hengjia.api.modules.account.dao.BankCardMapper;
import com.baibei.hengjia.api.modules.account.model.BankCard;
import com.baibei.hengjia.api.modules.account.service.IBankCardService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;


/**
 * @author: hyc
 * @date: 2019/06/03 14:40:16
 * @description: BankCard服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BankCardServiceImpl extends AbstractService<BankCard> implements IBankCardService {

    @Autowired
    private BankCardMapper tblBankCardMapper;

    @Override
    public void insertOne(SigningDataDto insertPasswordDto) {
        BankCard bankCard = new BankCard();
        bankCard.setBankCardNo(insertPasswordDto.getBankCardNo());
        bankCard.setCustomerNo(insertPasswordDto.getCustomerNo());
        bankCard.setIdcard(insertPasswordDto.getCertificateNo());
        bankCard.setMobile(insertPasswordDto.getMobile());
        bankCard.setName(insertPasswordDto.getRealName());
        bankCard.setContactName(insertPasswordDto.getContactName());
        bankCard.setCreateTime(new Date());
        bankCard.setModifyTime(new Date());
        bankCard.setFlag(new Byte("0"));
        tblBankCardMapper.insertSelective(bankCard);
    }

    @Override
    public BankCard findOneBank(BankCard bankCard) {
        Condition condition = new Condition(BankCard.class);
        condition.createCriteria()
                .andEqualTo("customerNo", bankCard.getCustomerNo())
                .andEqualTo("idcard", bankCard.getIdcard())
                .andEqualTo("bankCardNo", bankCard.getBankCardNo());
        return this.findOneByCondition(condition);
    }
}
