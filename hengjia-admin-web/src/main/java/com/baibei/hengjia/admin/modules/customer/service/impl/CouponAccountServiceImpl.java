package com.baibei.hengjia.admin.modules.customer.service.impl;

import com.baibei.hengjia.admin.modules.customer.bean.vo.CouponAccountVo;
import com.baibei.hengjia.admin.modules.customer.dao.CouponAccountMapper;
import com.baibei.hengjia.admin.modules.customer.model.CouponAccount;
import com.baibei.hengjia.admin.modules.customer.model.ProductMarket;
import com.baibei.hengjia.admin.modules.customer.service.ICouponAccountService;
import com.baibei.hengjia.admin.modules.customer.service.IProductMarketService;
import com.baibei.hengjia.admin.modules.product.service.ITradeProductService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
* @author: hyc
* @date: 2019/09/05 14:07:50
* @description: CouponAccount服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponAccountServiceImpl extends AbstractService<CouponAccount> implements ICouponAccountService {

    @Autowired
    private CouponAccountMapper tblCpCouponAccountMapper;

    @Autowired
    private IProductMarketService productMarketService;

    @Override
    public BigDecimal findByCustomerNo(String customerNo) {
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        BigDecimal totalCoupon=BigDecimal.ZERO;
        for (int i = 0; i <couponAccounts.size() ; i++) {
            totalCoupon=totalCoupon.add(couponAccounts.get(i).getBalance());
        }
        return totalCoupon;
    }

    @Override
    public List<CouponAccountVo> findProductByCustomerNo(String customerNo) {
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        List<CouponAccountVo> couponAccountVos=new ArrayList<>();
        for (int i = 0; i <couponAccounts.size() ; i++) {
            ProductMarket productMarket=productMarketService.findByProductTradeNo(couponAccounts.get(i).getProductTradeNo());
            CouponAccountVo couponAccountVo=new CouponAccountVo();
            if(productMarket!=null){
                couponAccountVo.setProductName(productMarket.getProductTradeName());
            }
            couponAccountVo.setBalance(couponAccounts.get(i).getBalance());
            couponAccountVos.add(couponAccountVo);
        }
        return couponAccountVos;
    }
}
