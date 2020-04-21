package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.vo.GlobalConfigVo;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/15 3:59 PM
 * @description:
 */
@RestController
@RequestMapping("/api/global")
public class ApiGlobalConfigController {
    /**
     * 出金手续费
     */
    @Value("${withdraw.fee.rate}")
    private String withdrawFeeRate;

    /**
     * 最小提货量
     */
    @Value("${delivery.count}")
    private String minDeliveryCount;

    /**
     * 最小出金手续费
     */
    @Value("${withdraw.fee}")
    private String withdrawFee;

    /**
     * 微信渠道开关
     */
    @Value("${weixin.flag}")
    private String winXinFlag;

    /**
     * 平安出金开关
     */
    @Value("${pingan.flag}")
    private String pingAnFlag;

    /**
     * 支付宝渠道开关
     */
    @Value("${alipay.flag}")
    private String aliPayFlag;

    /**
     * 签约个人还是企开关,personal为个人,enterprise 企业,
     */
    @Value("${signing.personalOrEnterprise}")
    private Boolean personalOrEnterprise;

    /**
     * 全局参数配置
     *
     * @return
     */
    @PostMapping("/config")
    public ApiResult<GlobalConfigVo> config() {
        GlobalConfigVo result = new GlobalConfigVo();
        result.setMinDeliveryCount(Integer.valueOf(minDeliveryCount));
        result.setWithdrawFeeRate(new BigDecimal(withdrawFeeRate));
        result.setWithdrawFee(Integer.valueOf(withdrawFee));
        result.setWeixinChannel(Integer.valueOf(winXinFlag));
        result.setPinganChannel(Integer.valueOf(pingAnFlag));
        result.setAliPayChannel(Integer.valueOf(aliPayFlag));
        result.setPersonalOrEnterprise(personalOrEnterprise);
        return ApiResult.success(result);
    }
}
