package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.dto.TradeDeListDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
import com.baibei.hengjia.api.modules.trade.biz.TradeBiz;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/3 7:30 PM
 * @description: 挂摘牌交易
 */
@RestController
@RequestMapping("/auth/api/trade")
public class AuthApiTradeController {
    @Autowired
    private TradeBiz tradeBiz;

    /**
     * 挂牌买入/卖出
     *
     * @param tradeListDto
     * @return
     */
    @PostMapping("/list")
    public ApiResult<String> list(@RequestBody @Validated TradeListDto tradeListDto) {
        ApiResult<String> apiResult = null;
        if (Constants.TradeDirection.BUY.equals(tradeListDto.getDirection())) {
            apiResult = tradeBiz.listBuy(tradeListDto);
        } else if (Constants.TradeDirection.SELL.equals(tradeListDto.getDirection())) {
            if (tradeListDto.getId() == null) {
                return ApiResult.badParam("持有ID不能为空");
            }
            apiResult = tradeBiz.listSell(tradeListDto);
        } else {
            apiResult = ApiResult.badParam("挂牌方向错误");
        }
        return apiResult;
    }

    /**
     * 摘牌买入/卖出
     *
     * @param tradeDeListDto
     * @return
     */
    @PostMapping("/delist")
    public ApiResult<String> delist(@RequestBody @Validated TradeDeListDto tradeDeListDto) {
        ApiResult<String> apiResult = null;
        if (Constants.TradeDirection.BUY.equals(tradeDeListDto.getDirection())) {
            apiResult = tradeBiz.delistBuy(tradeDeListDto);
        } else if (Constants.TradeDirection.SELL.equals(tradeDeListDto.getDirection())) {
            if (tradeDeListDto.getId() == null) {
                return ApiResult.badParam("持有ID不能为空");
            }
            apiResult = tradeBiz.delistSell(tradeDeListDto);
        } else {
            apiResult = ApiResult.badParam("挂牌方向错误");
        }
        return apiResult;
    }

    /**
     * 卖出校验
     *
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/sellValidate")
    public ApiResult sellValidate(@RequestBody @Validated CustomerBaseDto customerBaseDto) {
        //return tradeBiz.sellValidate(customerBaseDto);
        return ApiResult.success();
    }
}
