package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.dao.BankOrderMapper;
import com.baibei.hengjia.admin.modules.settlement.model.BankOrder;
import com.baibei.hengjia.admin.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/12 10:46:21
 * @description: BankOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BankOrderServiceImpl extends AbstractService<BankOrder> implements IBankOrderService {

    @Autowired
    private BankOrderMapper bankOrderMapper;

    @Override
    public MyPageInfo<BankOrderVo> pageList(BankOrderDto bankOrderDto) {
        PageHelper.startPage(bankOrderDto.getCurrentPage(), bankOrderDto.getPageSize());
        List<BankOrderVo> list = bankOrderMapper.myList(bankOrderDto);
        list.stream().forEach(result -> dataConversion(result));
        MyPageInfo<BankOrderVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public List<BankOrderVo> BankOrderVoList(BankOrderDto bankOrderDto) {
        List<BankOrderVo> list = bankOrderMapper.myList(bankOrderDto);
        list.stream().forEach(result -> dataConversion(result));
        return list;
    }

    public void dataConversion(BankOrderVo bankOrderVo) {
        if ("1".equals(bankOrderVo.getType())) {
            bankOrderVo.setType("出金");
        } else if ("2".equals(bankOrderVo.getType())) {
            bankOrderVo.setType("入金");
        } else if ("3".equals(bankOrderVo.getType())) {
            bankOrderVo.setType("挂账");
        }
        bankOrderVo.setRealName(MobileUtils.changeName(bankOrderVo.getRealName()));
    }
}
