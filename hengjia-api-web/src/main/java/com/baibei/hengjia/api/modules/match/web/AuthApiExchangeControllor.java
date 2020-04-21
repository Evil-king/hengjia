package com.baibei.hengjia.api.modules.match.web;

import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.bean.dto.InsertPasswordDto;
import com.baibei.hengjia.api.modules.account.bean.dto.SigningDataDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.bean.vo.FundInformationVo;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.IBankCardService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.bean.dto.ExchangeDto;
import com.baibei.hengjia.api.modules.match.service.IExchangeLogService;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.api.modules.user.bean.dto.ForgetPasswordDto;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerDetailService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author: Longer
 * @date: 2019/8/5 18:17
 * @description:兑换相关
 */
@Controller
@RequestMapping("/auth/api/exchange")
public class AuthApiExchangeControllor {
    @Autowired
    private IExchangeLogService exchangeLogService;

    /**
     * 兑换
     * @param exchangeDto
     * @return
     */
    @PostMapping("/exchange")
    @ResponseBody
    public ApiResult updatePassword(@RequestBody @Validated ExchangeDto exchangeDto){
        exchangeLogService.exchange(exchangeDto);
        return ApiResult.success();
    }

}
