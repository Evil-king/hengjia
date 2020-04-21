package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.DealOrderDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.DealOrderDataStatisticsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DealOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DealOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.DealOrderMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.DealOrder;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDealOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/07/17 10:48:24
* @description: DealOrder服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class DealOrderServiceImpl extends AbstractService<DealOrder> implements IDealOrderService {

    @Autowired
    private DealOrderMapper dealOrderMapper;

    @Override
    public MyPageInfo<DealOrderVo> pageList(DealOrderDto dealOrderDto) {
        PageHelper.startPage(dealOrderDto.getCurrentPage(), dealOrderDto.getPageSize());
        List<DealOrderVo> pageList = dealOrderMapper.pageList(dealOrderDto);
        MyPageInfo<DealOrderVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }

    @Override
    public List<DealOrderVo> list(DealOrderDto dealOrderDto) {
        List<DealOrderVo> pageList = dealOrderMapper.pageList(dealOrderDto);
        return pageList;
    }

    @Override
    public BigDecimal findTradeFeeByCustomerNo(String customerNo,String startTime,String endTime) {
        return dealOrderMapper.findTradeFeeByCustomerNo(customerNo,startTime,endTime);
    }

    @Override
    public BigDecimal findSellMoneyByCustomer(String customerNo, String createTime) {
        return dealOrderMapper.findSellMoneyByCustomer(customerNo,createTime);
    }

    @Override
    public MyPageInfo<DealOrderDataStatisticsVo> dealOrderPageList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto) {
        PageHelper.startPage(dealOrderDataStatisticsDto.getCurrentPage(), dealOrderDataStatisticsDto.getPageSize());
        List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVos=dealOrderDataStatisticsVoList(dealOrderDataStatisticsDto);
        MyPageInfo<DealOrderDataStatisticsVo> page = new MyPageInfo<>(dealOrderDataStatisticsVos);
        return page;
    }
    @Override
    public List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVoList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(dealOrderDataStatisticsDto.getEndTime())&&StringUtils.isEmpty(dealOrderDataStatisticsDto.getStartTime())){
            dealOrderDataStatisticsDto.setStartTime(sdf.format(new Date()));
            dealOrderDataStatisticsDto.setEndTime(sdf.format(new Date()));
        }
        List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVos=dealOrderMapper.dealOrderDataStatisticsVoList(dealOrderDataStatisticsDto);
        for (int i = 0; i <dealOrderDataStatisticsVos.size() ; i++) {
            DealOrderDataStatisticsVo dealOrderDataStatisticsVo=dealOrderDataStatisticsVos.get(i);
            //卖出数量
            BigDecimal sellAmount=dealOrderMapper.findSellAmountByCustomerAndDate(dealOrderDataStatisticsVo.getCustomerNo(),dealOrderDataStatisticsVo.getCreateTime());
            //买入数量
            BigDecimal buyAmount=dealOrderMapper.findBuyAmountByCustomerAndDate(dealOrderDataStatisticsVo.getCustomerNo(),dealOrderDataStatisticsVo.getCreateTime());

            //手续费(单纯只有交易，兑换、非交易过户、配货不计算在内)
            BigDecimal totalFee=findBuyOrSellTradeFeeByCustomerNo(dealOrderDataStatisticsVo.getCustomerNo(),dealOrderDataStatisticsVo.getCreateTime());
            dealOrderDataStatisticsVos.get(i).setBuyAmount(buyAmount);
            dealOrderDataStatisticsVos.get(i).setSellAmount(sellAmount);
            dealOrderDataStatisticsVos.get(i).setDealAmount(sellAmount.add(buyAmount));
            dealOrderDataStatisticsVos.get(i).setTotalFee(totalFee);
        }
        return dealOrderDataStatisticsVos;
    }

    private BigDecimal findBuyOrSellTradeFeeByCustomerNo(String customerNo, String createTime) {
        return dealOrderMapper.findBuyOrSellTradeFeeByCustomerNo(customerNo,createTime);
    }
}
