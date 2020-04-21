package com.baibei.hengjia.admin.modules.product.service;

import com.baibei.hengjia.admin.modules.product.bean.dto.TradeProductPageListDto;
import com.baibei.hengjia.admin.modules.product.bean.vo.TradeProductVo;
import com.baibei.hengjia.admin.modules.product.model.TradeProduct;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/02 18:48:18
 * @description: TradeProduct服务接口
 */
public interface ITradeProductService extends Service<TradeProduct> {

    MyPageInfo<TradeProductVo> pageList(TradeProductPageListDto tradeProductPageListDto);

}
