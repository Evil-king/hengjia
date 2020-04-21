package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.*;
import com.baibei.hengjia.api.modules.trade.bean.vo.*;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IEntrustOrderService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:17 PM
 * @description:
 */
@RestController
@RequestMapping("/auth/api/entrust")
public class AuthApiEntrustOrderController {
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IProductMarketService productMarketService;
    @Autowired
    private IAccountService accountService;


    /**
     * 挂牌委托单查询
     *
     * @param entrustAllListDto
     * @return
     */
    @PostMapping("/allList")
    public ApiResult<MyPageInfo<EntrustOrderVo>> allList(@RequestBody @Validated EntrustAllListDto entrustAllListDto) {

        return ApiResult.success(entrustOrderService.allList(entrustAllListDto));
    }


    /**
     * 当前转让委托单列表查询
     *
     * @param entrustAllListDto
     * @return
     */
    @PostMapping("/allListForTransfer")
    public ApiResult<MyPageInfo<EntrustOrderVo>> allListForTransfer(@RequestBody @Validated EntrustAllListDto entrustAllListDto) {

        return ApiResult.success(entrustOrderService.allListForTransfer(entrustAllListDto));
    }


    /**
     * 我的当前委托单列表
     *
     * @param customerBaseAndPageDto
     * @return
     */
    @PostMapping("/myList")
    public ApiResult<MyPageInfo<MyEntrustOrderVo>> myList(@RequestBody @Validated CustomerBaseAndPageDto customerBaseAndPageDto) {

        return ApiResult.success(entrustOrderService.myList(customerBaseAndPageDto));
    }

    /**
     * 我的持仓单列表
     *
     * @param myHoldDto
     * @return
     */
    @PostMapping("/myHoldList")
    public ApiResult<MyPageInfo<MyHoldVo>> myHoldList(@RequestBody @Validated MyHoldDto myHoldDto) {

        return ApiResult.success(holdTotalService.myHoldList(myHoldDto));
    }

    /**
     * 我的持仓单列表
     *
     * @param myHoldDto
     * @return
     */
    @PostMapping("/myHoldListForNew")
    public ApiResult<MyPageInfo<MyHoldNewVo>> myHoldList(@RequestBody @Validated MyHoldNewDto myHoldDto) {
        return ApiResult.success(holdTotalService.myHoldList(myHoldDto));
    }


    /**
     * 查询用户某个持仓单信息
     *
     * @param myHoldDto
     * @return
     */
    @PostMapping("/myDeliveryHold")
    public ApiResult<MyDeliveryHoldVo> myOneHold(@RequestBody @Validated MyDeliveryHoldDto myHoldDto) {
        MyDeliveryHoldVo myDeliveryHold = holdTotalService.findMyDeliveryHold(myHoldDto.getCustomerNo(), myHoldDto.getProductTradeNo(), myHoldDto.getHoldType());
        return ApiResult.success(myDeliveryHold);
    }


    /**
     * 撤单
     *
     * @param revokeDto
     * @return
     */
    @PostMapping("/revoke")
    public ApiResult revoke(@RequestBody @Validated RevokeDto revokeDto) {
        entrustOrderService.revoke(revokeDto, Constants.CheckTradeDay.CHECK);
        return ApiResult.success();
    }

    /**
     * 批量撤单(不需要判断交易日)
     *
     * @param revokeDtoList
     * @return
     */
    @PostMapping("/batchRevoke")
    public ApiResult batchRevoke(@RequestBody @Validated List<RevokeDto> revokeDtoList) {
        entrustOrderService.batchRevoke(revokeDtoList);
        return ApiResult.success();
    }


    /**
     * 撤销当前所有委托单(不需要判断交易日)
     *
     * @return
     */
    @GetMapping("/revokeAll")
    public ApiResult batchRevoke(@RequestParam("transferType") String transferType) {
        entrustOrderService.revokeAll(transferType);
        return ApiResult.success();
    }


    /**
     * 计算客户可买/可卖数量
     *
     * @param productTradeNoDto
     * @return
     */
    @PostMapping("/calculation")
    public ApiResult calculation(@RequestBody @Validated ProductTradeNoDto productTradeNoDto) {
        // 摘牌买入计算可买数量
        if (Constants.TradeDirection.BUY.equals(productTradeNoDto.getDirection())) {
            ProductMarket market = productMarketService.findByProductTradeNo(productTradeNoDto.getProductTradeNo());
            if (market == null) {
                return ApiResult.badParam("商品交易编码不存在");
            }
            Account account = accountService.checkAccount(productTradeNoDto.getCustomerNo());
            if (account == null) {
                return ApiResult.badParam("账户信息不存在");
            }
            return ApiResult.success(account.getBalance().divide(market.getIssuePrice(), BigDecimal.ROUND_DOWN).intValue());
            // 摘牌卖出计算可卖数量
        } else if (Constants.TradeDirection.SELL.equals(productTradeNoDto.getDirection())) {
            // 本票
            HoldTotal mainHold = holdTotalService.findByCustomerAndProductNo(productTradeNoDto.getCustomerNo(),
                    productTradeNoDto.getProductTradeNo(), Constants.HoldType.MAIN);
            BigDecimal mainCount = mainHold == null ? BigDecimal.ZERO : mainHold.getCanSellCount();
            // 配票
            HoldTotal matchHold = holdTotalService.findByCustomerAndProductNo(productTradeNoDto.getCustomerNo(),
                    productTradeNoDto.getProductTradeNo(), Constants.HoldType.MATCH);
            BigDecimal matchCount = matchHold == null ? BigDecimal.ZERO : matchHold.getCanSellCount();
            return ApiResult.success(mainCount.add(matchCount).intValue());
        } else {
            return ApiResult.badParam("方向错误");
        }
    }

}
