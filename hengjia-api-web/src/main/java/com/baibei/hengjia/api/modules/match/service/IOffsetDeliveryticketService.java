package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.dto.ComsumeAuthorityDto;
import com.baibei.hengjia.api.modules.match.model.OffsetDeliveryticket;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
* @author: Longer
* @date: 2019/10/10 09:47:51
* @description: OffsetDeliveryticket服务接口
*/
public interface IOffsetDeliveryticketService extends Service<OffsetDeliveryticket> {

    /**
     * 获取目前还没有全部消费完配货权的用户信息集合
     * @return
     */
    List<OffsetDeliveryticket> getInfosWithAuthority();

    /**
     * 消费配货权
     * @param comsumeAuthorityDto
     */
    ApiResult comsumeAuthority(ComsumeAuthorityDto comsumeAuthorityDto);

    OffsetDeliveryticket getOne(String customerNo,String productTradeNo);

    /**
     * 提货券补扣
     * @param customerNo
     * @param productTradeNo
     * @param validTradeDayFlag 是否判断交易日。valid=判断；unvalid=不判断
     * @return
     */
    ApiResult offset(String customerNo,String productTradeNo,String validTradeDayFlag);

    /**
     * 初始化补扣提货券名单
     * @return
     */
    void initOffsetData();
}
