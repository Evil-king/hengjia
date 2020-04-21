package com.baibei.hengjia.api.modules.shop.service;

import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.shop.bean.dto.CancelPointOrderDto;
import com.baibei.hengjia.api.modules.shop.bean.dto.ConfirmReceiptDTO;
import com.baibei.hengjia.api.modules.shop.bean.dto.ExchangeListDTO;
import com.baibei.hengjia.api.modules.shop.bean.dto.ExchangePointDTO;
import com.baibei.hengjia.api.modules.shop.bean.vo.OrderListVO;
import com.baibei.hengjia.api.modules.shop.model.ShpOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.Date;


/**
* @author: wenqing
* @date: 2019/06/03 15:49:31
* @description: ShpOrder服务接口
*/
public interface IShpOrderService extends Service<ShpOrder> {

    /**
     * 积分商城首页立即兑换
     * @param exchangePointDTO
     */
    void exchangePoint(ExchangePointDTO exchangePointDTO);

    /**
     * 积分商城首页获取用户相关信息
     * @param customerBaseDto
     */
    IntegralDetailVo getUserInfo(CustomerBaseDto customerBaseDto);

    /**
     * 积分兑换记录
     * @param exchangeListDTO
     */
    MyPageInfo<OrderListVO> exchangeList( ExchangeListDTO exchangeListDTO);

    /**
     * 确认提货
     * @param confirmReceiptDTO
     * @return
     */
    ApiResult confirmReceipt(ConfirmReceiptDTO confirmReceiptDTO);

    /**
     * 14天自动收货
     * @return
     */
    int longDayConfirmSend();

    /**
     * 清算统计
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    BigDecimal sumAmount(Date beginTime,Date endTime);

    /**
     * 撤回积分订单并且将状态改为cancel然后回退积分
     * @param cancelPointOrderDto
     * @return
     */
    ApiResult cancelPointOrder(CancelPointOrderDto cancelPointOrderDto);
}
