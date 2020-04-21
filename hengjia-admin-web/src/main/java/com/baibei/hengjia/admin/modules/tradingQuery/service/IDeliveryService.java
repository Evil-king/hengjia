package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DeliveryDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryExportVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.Delivery;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/19 13:51:38
 * @description: Delivery服务接口
 */
public interface IDeliveryService extends Service<Delivery> {
    MyPageInfo<DeliveryVo> pageList(DeliveryDto deliveryDto);

    /**
     * 审核
     *
     * @param deliveryDto
     * @return
     */
    ApiResult audit(DeliveryDto deliveryDto);

    /**
     * 发货
     *
     * @param deliveryDto
     * @return
     */
    ApiResult send(DeliveryDto deliveryDto);

    /**
     * 14天自动收货
     */
    ApiResult autoReceive();

    /**
     * 添加提货订单物流信息
     */
    void addLogisticsInfo(DeliveryDto deliveryDto);

    /**
     * 导出数据
     * @param deliveryDto
     * @return
     */
    List<DeliveryExportVo> list(DeliveryDto deliveryDto);

    /**
     * 发货
     * @param deliveryNo
     * @param logisticsCompany
     * @param logisticsNo
     * @return
     */
    String send(String deliveryNo, String logisticsCompany, String logisticsNo);

    /**
     * 判断提货订单是否存在
     * @param orderNo
     * @return
     */
    int selectByOrderNoExist(String orderNo);
}
