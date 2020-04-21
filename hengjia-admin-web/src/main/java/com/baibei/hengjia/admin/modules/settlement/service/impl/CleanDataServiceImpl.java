package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.FundDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.FundDataStatisticsVo;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanDataPageDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanDataPageVo;
import com.baibei.hengjia.admin.modules.settlement.dao.CleanDataMapper;
import com.baibei.hengjia.admin.modules.settlement.model.CleanData;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDealOrderService;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IRecordMoneyService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/08 16:02:12
 * @description: CleanData服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CleanDataServiceImpl extends AbstractService<CleanData> implements ICleanDataService {

    @Autowired
    private CleanDataMapper tblSetCleanDataMapper;

    @Autowired
    private IRecordMoneyService recordMoneyService;

    @Autowired
    private IDealOrderService dealOrderService;

    @Override
    public MyPageInfo<CleanDataPageVo> pageList(CleanDataPageDto cleanDataPageDto) {
        Condition condition = new Condition(CleanData.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(cleanDataPageDto.getBatchNo())) {
            criteria.andEqualTo("batchNo", cleanDataPageDto.getBatchNo());
        }
        if (!StringUtils.isEmpty(cleanDataPageDto.getCustomerNo())) {
            criteria.andEqualTo("thirdCustId", cleanDataPageDto.getCustomerNo());
        }
        MyPageInfo<CleanData> pageInfo = pageList(condition, PageParam.buildWithDefaultSort(cleanDataPageDto.getCurrentPage(), cleanDataPageDto.getPageSize()));
        return PageUtil.transform(pageInfo, CleanDataPageVo.class);
    }

    @Override
    public List<CleanDataPageVo> CleanDataPageVoList(CleanDataPageDto cleanDataPageDto) {
        List<CleanData> cleanData=tblSetCleanDataMapper.CleanDataPageVoList(cleanDataPageDto);
        List<CleanDataPageVo> cleanDataPageVos=BeanUtil.copyProperties(cleanData,CleanDataPageVo.class);
        return cleanDataPageVos;
    }
    @Override
    public MyPageInfo<FundDataStatisticsVo> fundPageList(FundDataStatisticsDto fundDataStatisticsDto) {
        PageHelper.startPage(fundDataStatisticsDto.getCurrentPage(), fundDataStatisticsDto.getPageSize());
        List<FundDataStatisticsVo> fundDataStatisticsVos=fundDataStatisticsVoList(fundDataStatisticsDto);
        MyPageInfo<FundDataStatisticsVo> page = new MyPageInfo<>(fundDataStatisticsVos);
        return page;
    }

    @Override
    public List<FundDataStatisticsVo> fundDataStatisticsVoList(FundDataStatisticsDto fundDataStatisticsDto) {
        List<FundDataStatisticsVo> fundDataStatisticsVos=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(fundDataStatisticsDto.getEndTime())&&StringUtils.isEmpty(fundDataStatisticsDto.getStartTime())){
            fundDataStatisticsDto.setStartTime(sdf.format(new Date()));
            fundDataStatisticsDto.setEndTime(sdf.format(new Date()));
        }
        if (StringUtils.isEmpty(fundDataStatisticsDto.getEndTime())){
            fundDataStatisticsDto.setFlag("true");
            fundDataStatisticsVos=tblSetCleanDataMapper.fundPageList(fundDataStatisticsDto);
        }else {
            try {
                Date endTime=sdf.parse(fundDataStatisticsDto.getEndTime());
                if(endTime.getTime()>=sdf.parse(sdf.format(new Date())).getTime()){
                    //如果结束时间大于当前时间
                    fundDataStatisticsDto.setFlag("true");
                    fundDataStatisticsVos=tblSetCleanDataMapper.fundPageList(fundDataStatisticsDto);
                }else {
                    //此时则可以通过结算表直接查询得到
                    fundDataStatisticsVos=tblSetCleanDataMapper.findByFundDataStatisticsDto(fundDataStatisticsDto);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i <fundDataStatisticsVos.size() ; i++) {
            FundDataStatisticsVo fundDataStatisticsVo=fundDataStatisticsVos.get(i);
            //查询到上一天的数据的金额字段（即为当天的期初资金）

            BigDecimal initMoney=BigDecimal.ZERO;
            if(dayForWeek(fundDataStatisticsVo.getCreateTime())!=1){
                //如果当天不是周一,查昨天的期末金额
                initMoney=tblSetCleanDataMapper.findTodayInitMoney(fundDataStatisticsVo.getCreateTime(),fundDataStatisticsVo.getCustomerNo());
            }else {
                //如果是周一，查询上周五的期末金额
                initMoney=tblSetCleanDataMapper.findInitMoneyAndMonday(fundDataStatisticsVo.getCreateTime(),fundDataStatisticsVo.getCustomerNo());
            }
            fundDataStatisticsVo.setInitMoney(initMoney);
            //查询当天的净入金总额（记得扣除入金回退）
            BigDecimal totalRecharge=recordMoneyService.findSumByDateAndCustomerNoAndTradeType(fundDataStatisticsVo.getCustomerNo(),"101",fundDataStatisticsVo.getCreateTime(),fundDataStatisticsVo.getCreateTime());
            fundDataStatisticsVo.setRechargeMoney(totalRecharge);
            //查询当天的卖出总额
            BigDecimal sellOutMoney=dealOrderService.findSellMoneyByCustomer(fundDataStatisticsVo.getCustomerNo(),fundDataStatisticsVo.getCreateTime());
            fundDataStatisticsVo.setSellMoeny(sellOutMoney);
            //查询当天的出金总额
            BigDecimal totalWithDraw=recordMoneyService.findSumByDateAndCustomerNoAndTradeType(fundDataStatisticsVo.getCustomerNo(),"102",fundDataStatisticsVo.getCreateTime(),fundDataStatisticsVo.getCreateTime());
            //出金回退数额
            BigDecimal totalWithDrawBack=recordMoneyService.findSumByDateAndCustomerNoAndTradeType(fundDataStatisticsVo.getCustomerNo(),"110",fundDataStatisticsVo.getCreateTime(),fundDataStatisticsVo.getCreateTime());
            fundDataStatisticsVo.setWithdrawMoney(totalWithDraw.subtract(totalWithDrawBack).setScale(2,BigDecimal.ROUND_DOWN));
        }
        return fundDataStatisticsVos;
    }
    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

}
