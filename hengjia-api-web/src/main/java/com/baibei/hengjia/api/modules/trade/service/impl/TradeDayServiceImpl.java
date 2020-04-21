package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.TradeDayMapper;
import com.baibei.hengjia.api.modules.trade.model.TradeDay;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: TradeDay服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TradeDayServiceImpl extends AbstractService<TradeDay> implements ITradeDayService {

    @Autowired
    private TradeDayMapper tblTradeDayMapper;
    @Autowired
    private RedisUtil redisUtil;

    private static SimpleDateFormat sf1 = (SimpleDateFormat) DateUtil.yyyyMMddWithLine.get();

    private static SimpleDateFormat sf2 = (SimpleDateFormat) DateUtil.yyyyMMddHHmmssWithLine.get();
    /**
     * 判断交易日方式（使用validateFromDb()方法还是使用validateFromDb2()方法去校验）
     * validateFromDb=使用validateFromDb()方法
     * validateFromDb2=使用validateFromDb2()方法
     */
    @Value("${tradeday.validate.form}")
    private String validForm;


    @Override
    public boolean isTradeTime() {
        String key = RedisConstant.TRADE_TRADE_DAY;
        String tradeDayFlag = redisUtil.get(key);
        if (StringUtils.isEmpty(tradeDayFlag)) {
            boolean isOpen = validateFromDb();
            if (isOpen) {
                redisUtil.set(key, "1");
                return true;
            } else {
                redisUtil.set(key, "0");
                return false;
            }
        } else {
            return "1".equals(tradeDayFlag);
        }
    }

    @Override
    public boolean setTradeDayToRedis() {
        String key = RedisConstant.TRADE_TRADE_DAY;
        boolean isOpen = false;
        if (StringUtils.isEmpty(validForm) || "validateFromDb2".equals(validForm)) {//默认使用validateFromDb2()方法
            isOpen = validateFromDb2();
        }
        if ("validateFromDb".equals(validForm)) {
            isOpen = validateFromDb();
        }
        if (isOpen) {
            redisUtil.set(key, "1");
            return true;
        } else {
            redisUtil.set(key, "0");
            return false;
        }
    }

    /**
     * 从数据库校验当前时间是否为开市时间
     *
     * @return
     */
    private boolean validateFromDb() {
        Date middleTime = DateUtil.getMiddleTime();
        TradeDay tradeDay;
        Date zeroDate = DateUtil.getBeginDay();
        Date now = new Date();
        log.info("middleTime={},zeroDate={},now={},TradeDay={}", middleTime, zeroDate, now);
        if (now.before(middleTime)) {
            tradeDay = findTradeDay(zeroDate, "morning");
        } else {
            tradeDay = findTradeDay(zeroDate, "afternoon");
        }
        System.out.println(tradeDay == null);
        if (tradeDay == null) {
            log.warn("交易日配置不存在,请检查配置,zeroDate={}",
                    DateUtil.yyyyMMddHHmmssWithLine.get().format(zeroDate));
            return false;
        }
        Date startTime = DateUtil.appendHhmmss(tradeDay.getStartTime());
        Date endTime = DateUtil.appendHhmmss(tradeDay.getEndTime());
        if (now.after(startTime) && now.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateFromDb2() {
        TradeDay tradeDay;
        Date now = new Date();
        long nowTime = now.getTime();
        SimpleDateFormat sf1 = TradeDayServiceImpl.sf1;
        SimpleDateFormat sf2 = TradeDayServiceImpl.sf2;
        //当天中午的时间
        String middleTimeStr = sf1.format(now) + " 12:00:00";
        long middleTime = 0;
        //获取天的0点
        String zeroDateStr = sf1.format(now) + " 00:00:00";
        Date zeroDate = null;
        try {
            middleTime = sf2.parse(middleTimeStr).getTime();
            zeroDate = sf2.parse(zeroDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (nowTime < middleTime) {
            tradeDay = findTradeDay(zeroDate, "morning");
        } else {
            tradeDay = findTradeDay(zeroDate, "afternoon");
        }
        if (tradeDay == null) {
            log.warn("交易日配置不存在,请检查配置,zeroDate={}",
                    DateUtil.yyyyMMddHHmmssWithLine.get().format(zeroDate));
            return false;
        }
        Date startTime = DateUtil.appendHhmmss(tradeDay.getStartTime());
        Date endTime = DateUtil.appendHhmmss(tradeDay.getEndTime());
        if (now.after(startTime) && now.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TradeDay findTradeDay(Date tradeDay, String period) {
        Condition condition = new Condition(TradeDay.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("tradeDay", tradeDay);
        criteria.andEqualTo("period", period);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<TradeDay> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public TradeDay findTradeDay2(Date tradeDay, String period) {
        String tradeDayStr = DateUtil.yyyyMMddHHmmssWithLine.get().format(tradeDay);
        return tblTradeDayMapper.selectTradeDay(tradeDayStr, period);
    }

    @Override
    public TradeDay getTheFifthTradeDay() {
        Date currentDate = new Date();
        //查询当前时间是属于哪个交易日
        TradeDay currentTradeDay = this.getCurrentTradeDay(currentDate);
        //获取第T+5个交易日信息
        TradeDay fifthTradeDay = tblTradeDayMapper.selectTheFifthTradeDay(new SimpleDateFormat("yyyy-MM-dd").format(currentTradeDay.getTradeDay()));
        return fifthTradeDay;
    }

    /**
     * @param currentDate
     * @return
     */
    @Override
    public TradeDay getCurrentTradeDay(Date currentDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = sf.format(currentDate);
        /**
         * 不可以用下面的语句格式化。否则部署到服务器上会有问题
         */
        /*String currentDateStr = DateFormat.getDateInstance().format(currentDate);*/
        TradeDay currentTradeDay = tblTradeDayMapper.selectCurrentTradeDay(currentDateStr);
        return currentTradeDay;
    }


    @Override
    public Date getAddNTradeDay(int n) {
        int value = Math.abs(n);
        Date currentTradeDay = DateUtil.getBeginDay();
        Map<String, Object> param = new HashMap<>();
        param.put("currentTradeDay", currentTradeDay);
        // T+N往前取,T-N往后取
        param.put("isAdd", n > 0 ? 1 : 0);
        param.put("limit", value);
        List<Date> tradeList = tblTradeDayMapper.listTradeDay(param);
        if (CollectionUtils.isEmpty(tradeList)) {
            log.warn("tradeList is null");
            return null;
        }
        if (tradeList.size() < value) {
            log.warn("tradeList not enough,tradeList.size={},n={}", tradeList.size(), value);
            return null;
        }
        return tradeList.get(value - 1);
    }


    @Override
    public boolean isTradeDay(Date date) {
        List<TradeDay> list = tblTradeDayMapper.selectDate(date, Constants.Flag.VALID);
        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public Date getAddNTradeDay(Date time, int n) {
        int value = Math.abs(n);
        Map<String, Object> param = new HashMap<>();
        param.put("currentTradeDay", time);
        // T+N往前取,T-N往后取
        param.put("isAdd", n > 0 ? 1 : 0);
        param.put("limit", value);
        List<Date> tradeList = tblTradeDayMapper.listTradeDay(param);
        if (CollectionUtils.isEmpty(tradeList)) {
            log.warn("tradeList is null");
            return null;
        }
        if (tradeList.size() < value) {
            log.warn("tradeList not enough,tradeList.size={},n={}", tradeList.size(), value);
            return null;
        }
        return tradeList.get(value - 1);
    }
}
