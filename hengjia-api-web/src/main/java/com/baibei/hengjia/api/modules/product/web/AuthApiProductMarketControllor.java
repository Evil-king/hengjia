package com.baibei.hengjia.api.modules.product.web;

import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.product.bean.dto.ProductMarketDto;
import com.baibei.hengjia.api.modules.product.bean.dto.ProductMarketNewDto;
import com.baibei.hengjia.api.modules.product.bean.vo.ProductMarketVo;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/6/5 18:09
 * @description:
 */
@RestController
@RequestMapping("/auth/api/productMarket")
public class AuthApiProductMarketControllor {
    @Autowired
    private IProductMarketService productMarketService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IHoldDetailsService holdDetailsService;

    @RequestMapping("/findByProductTradeNo")
    public ApiResult<ProductMarketVo> findByProductTradeNo(@RequestBody @Validated ProductMarketDto productMarketDto) {
        if ("SELL".equals(productMarketDto.getDirection()) || "BUY".equals(productMarketDto.getDirection())) {
            ProductMarketVo productMarketVo = productMarketService.findByProductTradeNo(productMarketDto);
            if (productMarketVo == null) {
                return ApiResult.badParam("商品不存在");
            }
            if ("SELL".equals(productMarketDto.getDirection())) {
                //卖出,则需要获取到用户持仓数量
                // 本票
                HoldTotal mainHold = holdTotalService.findByCustomerAndProductNo(productMarketDto.getCustomerNo(),
                        productMarketDto.getProductTradeNo(), Constants.HoldType.MAIN);
                int mainCount = mainHold == null ? 0 : mainHold.getCanSellCount().divide(new BigDecimal(1), 0, BigDecimal.ROUND_DOWN).intValue();
                // 配票
                HoldTotal matchHold = holdTotalService.findByCustomerAndProductNo(productMarketDto.getCustomerNo(),
                        productMarketDto.getProductTradeNo(), Constants.HoldType.MATCH);
                int matchCount = matchHold == null ? 0 : matchHold.getCanSellCount().divide(new BigDecimal(1), 0, BigDecimal.ROUND_DOWN).intValue();
                productMarketVo.setProductAmount(mainCount + matchCount);
            } else {
                //买入,则需要使用余额除以发行价
                Account account = accountService.checkAccount(productMarketDto.getCustomerNo());
                if (account == null) {
                    return ApiResult.badParam("账户不存在");
                }
                BigDecimal result = account.getBalance().divide(productMarketVo.getIssuePrice(), 2, BigDecimal.ROUND_DOWN);
                Integer canBuy = result.setScale(0, BigDecimal.ROUND_DOWN).intValue();
                productMarketVo.setProductAmount(canBuy);
            }

            return ApiResult.success(productMarketVo);
        } else {
            return ApiResult.badParam("买卖方向错误");
        }
    }


    /**
     * 卖出页面数据获取
     *
     * @param productMarketNewDto
     * @return
     */
    @PostMapping("/findByProductTradeNoForSell")
    public ApiResult<ProductMarketVo> findByProductTradeNoForSell(@RequestBody @Validated ProductMarketNewDto productMarketNewDto) {
        HoldDetails holdDetails = holdDetailsService.findById(productMarketNewDto.getId());
        if (holdDetails == null) {
            return ApiResult.badParam("持有数据不存在！");
        }
        ProductMarketDto dto = new ProductMarketDto();
        dto.setProductTradeNo(holdDetails.getProductTradeNo());
        dto.setDirection("SELL");
        ProductMarketVo productMarketVo = productMarketService.findByProductTradeNo(dto);
        if (productMarketVo == null) {
            return ApiResult.badParam("商品不存在");
        }
        productMarketVo.setProductAmount(holdDetails.getScanner().intValue() == 0 ? 0 : holdDetails.getRemaindCount().intValue());
        return ApiResult.success(productMarketVo);
    }
}
