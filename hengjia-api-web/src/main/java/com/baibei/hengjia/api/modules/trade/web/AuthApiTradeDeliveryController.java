package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname AuthApiTradeDeliveryController
 * @Description 用户提货
 * @Date 2019/6/3 15:37
 * @Created by Longer
 */
@RestController
@RequestMapping("/auth/api/delivery")
public class AuthApiTradeDeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    /**
     * 用户提货接口(提货申请)
     *
     * @param deliveryApplyDto 入参实体
     * @return
     */
    @PostMapping("/application")
    public ApiResult deliveryApply(@RequestBody @Validated DeliveryApplyDto deliveryApplyDto) {
        ApiResult<TradeBanner> apiResult;
        try {
            deliveryApplyDto.setSource(Constants.DeliverySource.COMMON);
            apiResult = deliveryService.deliveryApply(deliveryApplyDto);
        } catch (ServiceException se) {
            se.printStackTrace();
            apiResult = ApiResult.error(se.getMessage());
        } catch (Exception e) {
            apiResult = ApiResult.error(e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/deliveryList")
    public ApiResult<MyPageInfo<MyDeliveryVo>> allList(@RequestBody @Validated DeliveryQueryDto queryDto) {
        MyPageInfo<MyDeliveryVo> myDeliveryVoMyPageInfo = deliveryService.myDeliveryList(queryDto);
        return ApiResult.success(myDeliveryVoMyPageInfo);
    }

    /**
     * 确认收货
     *
     * @param deliveryApplyDto
     * @return
     */
    @PostMapping(path = "/receipt")
    public ApiResult receipt(@RequestBody DeliveryApplyDto deliveryApplyDto) {
        if (deliveryApplyDto.getDeliveryNo() == null) {
            return ApiResult.error("订货订单不能为空");
        }
        deliveryService.receipt(deliveryApplyDto);
        return ApiResult.success();
    }
}
