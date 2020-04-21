package com.baibei.hengjia.admin.modules.product.web;

import com.baibei.hengjia.admin.modules.product.bean.dto.TradeProductDto;
import com.baibei.hengjia.admin.modules.product.bean.dto.TradeProductPageListDto;
import com.baibei.hengjia.admin.modules.product.bean.vo.TradeProductVo;
import com.baibei.hengjia.admin.modules.product.model.TradeProduct;
import com.baibei.hengjia.admin.modules.product.service.ITradeProductService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/2 6:48 PM
 * @description:
 */
@RestController
@RequestMapping("/admin")
public class AdminTradeProductController {
    @Autowired
    private ITradeProductService tradeProductService;

    @GetMapping("/tradeProduct")
    public ApiResult<MyPageInfo<TradeProductVo>> pageList(TradeProductPageListDto tradeProductPageListDto) {

        return ApiResult.success(tradeProductService.pageList(tradeProductPageListDto));
    }


    @PostMapping("/tradeProduct")
    public ApiResult<String> save(@RequestBody TradeProductDto tradeProductDto) {
        tradeProductService.save(BeanUtil.copyProperties(tradeProductDto, TradeProduct.class));
        return ApiResult.success("创建成功");
    }

    @PutMapping("/tradeProduct")
    public ApiResult<String> update(@RequestBody TradeProductDto tradeProductDto) {
        tradeProductService.update(BeanUtil.copyProperties(tradeProductDto, TradeProduct.class));
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("/tradeProduct/{id}")
    public ApiResult<String> delete(@PathVariable Long id) {
        tradeProductService.deleteById(id);
        return ApiResult.success("删除成功");
    }

}
