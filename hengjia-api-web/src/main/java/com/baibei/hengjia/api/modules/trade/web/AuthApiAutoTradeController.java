package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.trade.bean.dto.AutoTradeOperateDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.AutoTradeSaveDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradePageVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradeRecordVo;
import com.baibei.hengjia.api.modules.trade.service.IAutoConfigService;
import com.baibei.hengjia.api.modules.trade.service.IAutoRecordService;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 11:22
 * @description: 自动购销
 */
@RestController
@RequestMapping("/auth/api/autoTrade")
@Slf4j
public class AuthApiAutoTradeController {
    @Autowired
    private IAutoConfigService autoConfigService;
    @Autowired
    private IAutoRecordService autoRecordService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private ICustomerAddressService customerAddressService;

    /**
     * 自动购销页面数据获取
     *
     * @return
     */
    @PostMapping("/pageInfo")
    public ApiResult<AutoTradePageVo> pageInfo(@Validated @RequestBody CustomerBaseDto customerBaseDto) {
        return autoConfigService.getPageInfo(customerBaseDto.getCustomerNo());
    }

    /**
     * 保存配置
     *
     * @param autoTradeSaveDto
     * @return
     */
    @PostMapping("/saveConfig")
    public ApiResult saveConfig(@Validated @RequestBody AutoTradeSaveDto autoTradeSaveDto) {
        return autoConfigService.saveConfig(autoTradeSaveDto);
    }

    /**
     * 开启/关闭预约购销
     *
     * @param autoTradeSaveDto
     * @return
     */
    @PostMapping("/operate")
    public ApiResult operate(@Validated @RequestBody AutoTradeOperateDto autoTradeSaveDto) {
        return autoConfigService.operate(autoTradeSaveDto);
    }

    /**
     * 预约购销记录
     *
     * @return
     */
    @PostMapping("/recordList")
    public ApiResult<MyPageInfo<AutoTradeRecordVo>> record(@RequestBody @Validated CustomerBaseAndPageDto dto) {
        return ApiResult.success(autoRecordService.pageList(dto));
    }

    /**
     * 智能购销校验
     *
     * @return
     */
    @PostMapping("/validate")
    public ApiResult validate(@RequestBody @Validated CustomerBaseDto customerBaseDto) {
        String customerNo = customerBaseDto.getCustomerNo();
        // 校验是否签约
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo);
        if (signingRecord == null) {
            return new ApiResult(ResultEnum.NOT_SIGN);
        }
        // 校验是否填写地址
        List<CustomerAddress> list = customerAddressService.findByCustomerNo(customerNo);
        if (CollectionUtils.isEmpty(list)) {
            return new ApiResult(ResultEnum.NO_ADDRESS);
        }
        // 校验客户风控
        CustomerVo customerVo = customerService.findUserByCustomerNo(customerNo);
        if (CustomerStatusEnum.LIMIT_SELL.getCode() == Integer.parseInt(customerVo.getCustomerStatus())) {
            return ApiResult.badParam("账号状态异常，请联系客服");
        }
        // 挂牌商超级用户不允许卖出交易
        if (customerVo.getCustomerType().intValue() == 2) {
            return ApiResult.badParam("账号状态异常，请联系客服");
        }
        return ApiResult.success();
    }

}