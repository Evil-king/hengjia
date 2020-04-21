package com.baibei.hengjia.api.modules.sms.service.impl;

import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.sms.dao.PubSmsMapper;
import com.baibei.hengjia.api.modules.sms.model.PubSms;
import com.baibei.hengjia.api.modules.sms.process.WuJiXianTxtProcess;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/06/03 15:50:13
 * @description: PubSms服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubSmsServiceImpl extends AbstractService<PubSms> implements IPubSmsService {

    @Autowired
    private WuJiXianTxtProcess wuJiXianTxtProcess;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private PubSmsMapper tblPubSmsMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PubSmsMapper smsMapper;


    @Override
    public ApiResult getSms(String phone, String message) {
        //验证参数
        ApiResult apiResult = validate(phone, "");
        if (!apiResult.hasSuccess()) {
            return apiResult;
        }
        boolean flag = wuJiXianTxtProcess.doProcess(phone, message);
        if (!flag) {
            apiResult.setCode(ResultEnum.PUBLIC_SMS_CODE.getCode());
            apiResult.setMsg(ResultEnum.PUBLIC_SMS_CODE.getMsg());
            return apiResult;
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult validateCode(String phone, String code) {
        ApiResult apiResult = new ApiResult();
        //判断短信码5分钟之内有效
        String key = MessageFormat.format(RedisConstant.SMS_USER_PHONE, phone + code);
        String redisResule = redisUtil.get(key);
        if (redisResule == null) {
            apiResult.setMsg(ResultEnum.SMS_TIME_MAX.getMsg());
            apiResult.setCode(ResultEnum.SMS_TIME_MAX.getCode());
            return apiResult;
        }
        return ApiResult.success();
    }

    @Override
    public boolean sendNormalSms(String mobile, String msg) {
        return wuJiXianTxtProcess.doProcess(mobile, msg);
    }

    private ApiResult validate(String phone, String cunstomerNo) {
        ApiResult apiResult = new ApiResult();
        if (phone.length() != 11) {
            apiResult.setMsg(ResultEnum.MOBILE_NUM_CHECKOUT.getMsg());
            apiResult.setCode(ResultEnum.MOBILE_NUM_CHECKOUT.getCode());
            return apiResult;
        }
        /**
         * 判断该手机号一天之内的次数是否大于20
         */
        String nowDay = DateUtil.get1DayByNowTime();
        Condition condition = new Condition(PubSms.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("mobile", phone);
        criteria.andGreaterThanOrEqualTo("createTime", nowDay);
        int count = tblPubSmsMapper.selectCountByCondition(condition);
        if (count > propertiesVal.getSmsMaxCount()) {
            apiResult.setMsg(ResultEnum.SMS_COUNT_CHECKOUT.getMsg());
            apiResult.setCode(ResultEnum.SMS_COUNT_CHECKOUT.getCode());
            return apiResult;
        }

        Condition condition1 = new Condition(PubSms.class);
        condition1.orderBy("createTime").desc();
        Example.Criteria criteria1 = condition1.createCriteria();
        criteria1.andEqualTo("mobile", phone);
        List<PubSms> smsList = tblPubSmsMapper.selectByCondition(condition1);
        if (!CollectionUtils.isEmpty(smsList)) {
            long d = new Date().getTime() - smsList.get(0).getCreateTime().getTime();
            if (d <= propertiesVal.getSmsTimeOut()) { // 120秒后才能重新发送
                apiResult.setMsg(ResultEnum.SMS_TIME_OUT.getMsg());
                apiResult.setCode(ResultEnum.SMS_TIME_OUT.getCode());
                return apiResult;
            }
        }
        return apiResult;
    }
}
