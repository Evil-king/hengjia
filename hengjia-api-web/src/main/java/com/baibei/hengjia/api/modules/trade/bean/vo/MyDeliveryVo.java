package com.baibei.hengjia.api.modules.trade.bean.vo;

import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname MyDeliveryVo
 * @Description 提货记录VO
 * @Date 2019/6/5 20:18
 * @Created by Longer
 */
@Data
public class MyDeliveryVo {
    /*
        提货订单号
     */
    private String deliveryNo;
    /*
        商品交易编码
     */
    private String productTradeNo;
    /*
        商品名称
     */
    private String productTradeName;
    /*
        商品图片
     */
    private String imgUrl;
    /*
        提货时间
     */
    private Date deliveryTime;
    /*
        物流公司名
     */
    private String logisticsCompany;
    /*
        运单号
     */
    private String logisticsNo;
    /*
        商品发行价
     */
    private BigDecimal issuePrice;
    /*
        提货状态
     */
    private Integer deliveryStatus;
    /*
        提货数量
     */
    private Integer deliveryCount;

    private String deliveryStatusStr;
    //如果为plan则前端需要采用标识
    private String source;

    public String getDeliveryStatusStr(){
        String statusStr="";
        if(getDeliveryStatus().intValue()==Integer.parseInt(Constants.DeliveryStatus.AUDITING))
            statusStr="待审核";
        if(getDeliveryStatus().intValue()==Integer.parseInt(Constants.DeliveryStatus.UNSEND))
            statusStr="待发货";
        if(getDeliveryStatus().intValue()==Integer.parseInt(Constants.DeliveryStatus.SENT))
            statusStr="待收货";
        if(getDeliveryStatus().intValue()==Integer.parseInt(Constants.DeliveryStatus.RECEIVED))
            statusStr="已收货";
        if(getDeliveryStatus().intValue()==Integer.parseInt(Constants.DeliveryStatus.REJECT))
            statusStr="驳回";
        return statusStr;
    }
}
