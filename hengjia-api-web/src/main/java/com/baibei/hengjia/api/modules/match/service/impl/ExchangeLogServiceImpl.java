package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.dao.CouponAccountMapper;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.bean.dto.ExchangeDto;
import com.baibei.hengjia.api.modules.match.dao.ExchangeLogMapper;
import com.baibei.hengjia.api.modules.match.model.ExchangeLog;
import com.baibei.hengjia.api.modules.match.service.IExchangeLogService;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.model.TradeDay;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.NoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/08/05 15:48:26
* @description: ExchangeLog服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ExchangeLogServiceImpl extends AbstractService<ExchangeLog> implements IExchangeLogService {

    @Autowired
    private ExchangeLogMapper exchangeLogMapper;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private ICouponAccountService couponAccountService;
    @Autowired
    private CouponAccountMapper couponAccountMapper;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private HoldTotalMapper holdTotalMapper;
    @Autowired
    private HoldDetailsMapper holdDetailsMapper;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${exchange.max.num}")
    private String echangeMaxNum;//单一账户当天累计最大兑换量
    @Value("${exchange.period}")
    private String exchangePeriod;//可兑换时间范围
    @Value("${out.period.msg}")
    private String outPeriodMsg;//非兑换时间提示语
    @Override
    public void exchange(ExchangeDto exchangeDto) {
        /**
         * 主要流程：风控-->新增用户持仓(总持仓和持仓明细)-->扣减挂牌商持仓-->生成成交单-->减少用户券账户余额-->新增兑换流水
         */
        //判断当前时间是否休市状态
        boolean tradeDay = tradeDayService.isTradeDay(new Date());
        if(!tradeDay){
            throw new ServiceException("非交易日不能兑换");
        }
        //判断是否在兑换时间范围内
        boolean exchangeTime = isExchangeTime();
        if(!exchangeTime){
            throw new ServiceException(outPeriodMsg);
        }
        if (StringUtils.isEmpty(exchangeDto.getExchangeNum())||exchangeDto.getExchangeNum()==0) {
            throw new ServiceException("兑换失败，未指定兑换数量");
        }
        //T+5解锁
        TradeDay theFifthTradeDay = tradeDayService.getTheFifthTradeDay();
        //查询券账户信息
        CouponAccountVo couponAccountVo = this.getCouponAccount(exchangeDto);
        if(exchangeDto.getExchangeNum()>couponAccountVo.getExchangenNum()){
            throw new ServiceException("券余额不足");
        }
        if (!Constants.CouponType.VOUCHERS.equals(couponAccountVo.getCouponType())) {
            throw new ServiceException("异常兑换");
        }
        //风控:单一账户当天累计最大兑换量N，后台配置（当前兑换量+当天成功兑换量>N，触发拦截）
        int currentDayExchangeCount = this.getCurrentDayExchangeCount(couponAccountVo.getCustomerNo(), couponAccountVo.getProductTradeNo());
        if(new BigDecimal((currentDayExchangeCount+exchangeDto.getExchangeNum())).compareTo(new BigDecimal(echangeMaxNum))>0){
            throw new ServiceException("兑换失败，超出当天累计可兑量");
        }
        /*risk(couponAccountVo,exchangeDto);*/
        //成交单号
        String dealNo = NoUtil.getDealNo();
        //查询挂牌商编码
        /*String memberCustomerNo = customerService.getMemberCustomerNoByProductNo(couponAccountVo.getProductTradeNo());
        if(StringUtils.isEmpty(memberCustomerNo)){
            throw new ServiceException("挂牌商不存在，商品交易编码为："+couponAccountVo.getProductTradeNo());
        }*/
        //查询经销商编码
        String memberCustomerNo = customerService.getDistributorNo();
        if(StringUtils.isEmpty(memberCustomerNo)){
            throw new ServiceException("经销商不存在");
        }
        //获取用户持仓信息
        HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(couponAccountVo.getCustomerNo(), couponAccountVo.getProductTradeNo(), Constants.HoldType.MATCH);
        if(StringUtils.isEmpty(holdTotal)){
            //新增持仓
            addHoldTotal(couponAccountVo,exchangeDto);
        }else{
            //更新用户商品持仓
            boolean b = holdTotalService.addCustomerProductHoldForMatch(couponAccountVo.getCustomerNo(), couponAccountVo.getProductTradeNo(),
                    new BigDecimal(exchangeDto.getExchangeNum()), Constants.HoldType.MATCH);
            if(!b)
                throw new ServiceException("配货失败，乐观锁问题，请联系管理员");
        }
        //新增持仓明细
        addHoldDetails(couponAccountVo,exchangeDto,theFifthTradeDay,dealNo);
       /* //扣减经销商持仓
        boolean b = holdTotalService.deductMemberProductHold(memberCustomerNo, couponAccountVo.getProductTradeNo(), new BigDecimal(exchangeDto.getExchangeNum()), Constants.HoldType.MAIN);
        if(!b){
            throw new ServiceException("扣减经销商总持仓失败，乐观锁问题");
        }*/
        //扣减挂牌商持仓
        boolean b1 = holdDetailsService.deductProductHoldByTradeTime(memberCustomerNo, couponAccountVo.getProductTradeNo(), new BigDecimal(exchangeDto.getExchangeNum()), Constants.HoldType.MAIN);
        if(!b1){
            throw new ServiceException("扣减经销商持仓失败，乐观锁问题");
        }
        //生成成交单
        createDealOrder(couponAccountVo,exchangeDto,theFifthTradeDay,dealNo,memberCustomerNo);
        //减少用户券账户余额，新增券账户流水
        ApiResult apiResult = detuctCouponAmount(couponAccountVo,exchangeDto,dealNo);
        if (!apiResult.hasSuccess()) {
            throw new ServiceException(apiResult.getMsg());
        }
        //新增兑换流水
        addExchangeLog(couponAccountVo,exchangeDto,dealNo,memberCustomerNo);
    }

    /**
     * 判断是否是在兑换时间范围内
     * @return
     */
    public boolean isExchangeTime(){
        boolean result=true;
        String[] exchangePeriodArr = exchangePeriod.trim().split("~");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = sf1.format(new Date())+" "+exchangePeriodArr[0];
        String endTimeStr = sf1.format(new Date())+" "+exchangePeriodArr[1];
        try {
            long startTime = sf2.parse(startTimeStr).getTime();
            long endTime = sf2.parse(endTimeStr).getTime();
            long exchangeTime = new Date().getTime();
            if(exchangeTime<startTime||exchangeTime>endTime){
                result=false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean addLogs(ExchangeLog exchangeLog) {
        boolean result=false;
        if (!StringUtils.isEmpty(exchangeLog)) {
            result = this.save(exchangeLog);
        }
        return result;
    }

    private void risk(CouponAccountVo couponAccountVo, ExchangeDto exchangeDto) {
        String key = MessageFormat.format(RedisConstant.EXCHANGE_DAY_NUM,couponAccountVo.getCustomerNo(),couponAccountVo.getProductTradeNo());
        String exchangeDayNum = redisUtil.get(key);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        String expireTimeStr=format+" 23:59:59";
        Date expireTime = null;
        try {
            expireTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(new BigDecimal(exchangeDto.getExchangeNum()).compareTo(new BigDecimal(echangeMaxNum))>0){
            throw new ServiceException("兑换失败，超出当天累计可兑量");
        }
        if(exchangeDayNum==null){
            redisUtil.set(key,exchangeDto.getExchangeNum());
        }else{
            if (new BigDecimal(exchangeDayNum).add(new BigDecimal(exchangeDto.getExchangeNum())).compareTo(new BigDecimal(echangeMaxNum))>0) {
                throw new ServiceException("兑换失败，超出当天累计可兑量");
            }
            //当天累计兑换量
            redisUtil.incr(key,exchangeDto.getExchangeNum());
        }
        redisUtil.expireAt(key,expireTime);
    }

    private void addExchangeLog(CouponAccountVo couponAccountVo, ExchangeDto exchangeDto,String dealNo, String memberCustomerNo) {
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setExchangeNo(dealNo);
        exchangeLog.setCustomerNo(couponAccountVo.getCustomerNo());
        exchangeLog.setProductTradeNo(couponAccountVo.getProductTradeNo());
        exchangeLog.setMemberNo(memberCustomerNo);
        exchangeLog.setExchangeNum(exchangeDto.getExchangeNum());
        exchangeLog.setRetailPrice(couponAccountVo.getIssuePrice());//零售价
        exchangeLog.setTotalPrice(new BigDecimal(exchangeDto.getExchangeNum()).multiply(couponAccountVo.getIssuePrice()));//商品总价=零售价*兑换数量
        exchangeLog.setCouponPrice(new BigDecimal(exchangeDto.getExchangeNum()).multiply(couponAccountVo.getIssuePrice()));//使用券金额=商品总价
        exchangeLog.setRealPrice(exchangeLog.getCouponPrice().subtract(exchangeLog.getTotalPrice()));//实付金额=使用券金额-商品总价
        exchangeLog.setExchangeTime(new Date());//兑换时间
        this.save(exchangeLog);
    }

    private ApiResult detuctCouponAmount(CouponAccountVo couponAccountVo, ExchangeDto exchangeDto, String dealNo) {
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCustomerNo(couponAccountVo.getCustomerNo());
        changeCouponAccountDto.setProductTradeNo(couponAccountVo.getProductTradeNo());
        changeCouponAccountDto.setCouponType(couponAccountVo.getCouponType());
        changeCouponAccountDto.setOrderNo(dealNo);
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.MAIN_EXCHANGE.getCode());
        changeCouponAccountDto.setReType(new Byte("1"));
        changeCouponAccountDto.setChangeAmount(new BigDecimal(exchangeDto.getExchangeNum()).multiply(couponAccountVo.getIssuePrice()));
        return couponAccountService.changeAmount(changeCouponAccountDto);
    }

    private void createDealOrder(CouponAccountVo couponAccountVo, ExchangeDto exchangeDto, TradeDay theFifthTradeDay, String dealNo,String memberCustomerNo) {
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(dealNo);
        dealOrder.setEntrustId(0L);
        dealOrder.setProductTradeNo(couponAccountVo.getProductTradeNo());
        dealOrder.setBuyCustomerNo(couponAccountVo.getCustomerNo());
        dealOrder.setSellCustomerNo(memberCustomerNo);//挂牌商
        /*//手续费：两位小数，截断
        BigDecimal buyFee = new BigDecimal(exchangeDto.getExchangeNum()).multiply(couponAccountVo.getIssuePrice())
                .multiply(new BigDecimal(echangeFeeRate)).setScale(2,BigDecimal.ROUND_DOWN);*/
        dealOrder.setBuyFee(new BigDecimal(0));
        dealOrder.setType(Constants.TradeDirection.EXCHANGE);
        dealOrder.setPrice(new BigDecimal(0));
        dealOrder.setCount(exchangeDto.getExchangeNum());
        dealOrder.setHoldType(Constants.HoldType.MATCH);
        dealOrderService.save(dealOrder);
    }

    public CouponAccountVo getCouponAccount(ExchangeDto exchangeDto){
        CouponAccountDto couponAccountDto = new CouponAccountDto();
        couponAccountDto.setCouponAccountId(exchangeDto.getCouponAccountId());
        List<CouponAccountVo> couponAccountVos = couponAccountMapper.selectCouponAccount(couponAccountDto);
        if (StringUtils.isEmpty(couponAccountVos)||couponAccountVos.size()==0) {
            throw new ServiceException("未找到指定的券账户");
        }
        if(couponAccountVos.size()>1){
            throw new ServiceException("参数异常，找到多个券账户信息");
        }
        return couponAccountVos.get(0);
    }

    /**
     * 新增用户持仓
     * @param couponAccountVo
     * @param exchangeDto
     */
    public void addHoldTotal(CouponAccountVo couponAccountVo,ExchangeDto exchangeDto){
        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setCustomerNo(couponAccountVo.getCustomerNo());
        newHoldTotal.setProductTradeNo(couponAccountVo.getProductTradeNo());
        newHoldTotal.setTotalCount(new BigDecimal(exchangeDto.getExchangeNum()));
        newHoldTotal.setFrozenCount(new BigDecimal(0));
        newHoldTotal.setCanSellCount(new BigDecimal(0));
        newHoldTotal.setCost(new BigDecimal(0));
        newHoldTotal.setHoldType(Constants.HoldType.MATCH);
        newHoldTotal.setCreateTime(new Date());
        newHoldTotal.setModifyTime(new Date());
        newHoldTotal.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdTotalMapper.insert(newHoldTotal);
    }

    /**
     * 新增持仓明细
     */
    public void addHoldDetails(CouponAccountVo couponAccountVo,ExchangeDto exchangeDto,TradeDay theFifthTradeDay,String dealNo){
        HoldDetails holdDetails = new HoldDetails();
        holdDetails.setCustomerNo(couponAccountVo.getCustomerNo());
        holdDetails.setProductTradeNo(couponAccountVo.getProductTradeNo());
        holdDetails.setOriginalCount(new BigDecimal(exchangeDto.getExchangeNum()));
        holdDetails.setFrozenCount(new BigDecimal(0));
        holdDetails.setRemaindCount(new BigDecimal(exchangeDto.getExchangeNum()));
        holdDetails.setCost(new BigDecimal(0));
        holdDetails.setHoldTime(new Date());
        holdDetails.setHoldNo(dealNo);
        holdDetails.setResource(Constants.HoldResource.EXCHANGE);
        holdDetails.setScanner(Byte.valueOf("0"));
        holdDetails.setHoldType(Constants.HoldType.MATCH);
        holdDetails.setCreateTime(new Date());
        holdDetails.setModifyTime(new Date());
        holdDetails.setTradeTime(theFifthTradeDay.getTradeDay());//交易日期
        holdDetails.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdDetailsMapper.insert(holdDetails);
    }

    @Override
    public int getCurrentDayExchangeCount(String customerNo, String productTradeNo) {
        Integer count=exchangeLogMapper.selectCurrentDayExchangeCount(customerNo,productTradeNo);
        return count==null?0:count;
    }
}
