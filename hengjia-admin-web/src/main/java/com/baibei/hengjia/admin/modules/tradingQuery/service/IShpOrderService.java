package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.ShopOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.LookOrderDetailVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.LookUpVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.ShopOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.ShpOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/07/19 14:30:09
 * @description: ShpOrder服务接口
 */
public interface IShpOrderService extends Service<ShpOrder> {

    MyPageInfo<ShopOrderVo> pageList(ShopOrderDto shopOrderDto);

    List<LookOrderDetailVo> lookInfo(String orderNo);

    String confirmSend(String orderNo, String name, String logisticsNo);

    MyPageInfo<ShopOrderVo> list(ShopOrderDto shopOrderDto);

    List<LookUpVo> listData();

    int selectByOrderNoExist(String orderNo);
}
