package com.baibei.hengjia.api.modules.cash.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.dao.TempDepositMapper;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.api.modules.cash.service.IProcessService;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.Utils;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.BigDecimalUtil;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author hwq
 * @date 2019/06/22
 * <p>
 *     支付处理类
 * </p>
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IProcessServiceImpl implements IProcessService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private TempDepositMapper depositMapper;
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public ApiResult validate(JSONObject jsonObject) {

        Boolean flag = Boolean.FALSE;

        //判断用户状态
        CustomerVo customerVo = customerService.findUserByCustomerNo(jsonObject.getString("customerNo"));
        if ("102".indexOf(customerVo.getCustomerStatus()) != -1) {
            return ApiResult.error("该用户已经被限制充值");
        }

        //判断是否是交易日时间
        flag = tradeDayService.isTradeDay(new Date());
        if (!flag) {
            return ApiResult.error("非交易日时间");
        }

        //判断是否是出入金日时间
        flag = Utils.compareTime(propertiesVal.getDepositTime(), propertiesVal.getWithdrawTime());
        if (!flag) {
            return ApiResult.error("非出入金时间");
        }

        //判断入金金额上下限
        int minAmount = BigDecimalUtil.compareToBigDecimal(jsonObject.getBigDecimal("amount"), propertiesVal.getMinDeposit());
        int maxAmount = BigDecimalUtil.compareToBigDecimal(jsonObject.getBigDecimal("amount"), propertiesVal.getMaxDeposit());
        if (minAmount == -1) {
            return ApiResult.error("该用户入金下限不能小于1元");
        }
        if (maxAmount == 1) {
            return ApiResult.error("该用户入金上限不能大于20000元");
        }
        return ApiResult.success();
    }

    @Override
    public TempDeposit createOrder(JSONObject jsonObject) {
        String depositNo = "D" + CodeUtils.generateTreeOrderCode();
        //创建出金订单
        TempDeposit deposit = new TempDeposit();
        deposit.setCustomerNo(jsonObject.getString("customerNo"));
        deposit.setAmount(jsonObject.getBigDecimal("amount"));
        deposit.setFlag((byte) 1);
        deposit.setChannel(jsonObject.getString("channel"));
        deposit.setStatus("init");
        deposit.setOutorderNo("");
        deposit.setDepositNo(depositNo);
        deposit.setCreateTime(new Date());
        deposit.setModifyTime(new Date());
        depositMapper.insert(deposit);
        return deposit;
    }

    @Override
    public int updateOrder(String customerNo, String orderNo, String outOrderNo, String type) {
        Condition condition = new Condition(TempDeposit.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("depositNo", orderNo);
        TempDeposit deposit = new TempDeposit();
        deposit.setOutorderNo(outOrderNo);
        deposit.setModifyTime(new Date());
        if ("success".equals(type)) {
            deposit.setStatus("success");
        }
        if ("fail".equals(type)) {
            deposit.setStatus("fail");
        }
        return depositMapper.updateByConditionSelective(deposit, condition);
    }


}
