package com.baibei.hengjia.api.modules.trade.service;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MatchVo;
import com.baibei.hengjia.api.modules.trade.model.MatchLog;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
* @author: Longer
* @date: 2019/06/10 14:03:27
* @description:配票记录相关
*/
public interface IMatchLogService extends Service<MatchLog> {

    /**
     * 配票逻辑
     * @param matchListDto
     * @return
     */
    MatchVo match(MatchListDto matchListDto);


    /**
     * 提货配票
     */
    void deliveryMatch();

    /**
     * 获取未提货配票的用户customerNo集合
     * @return
     */
    List<String> getCustomerNos();

    /**
     * 统计手续费
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumFee(Map<String, Object> param);

    /**
     * 统计配票亏损
     *
     * @param param
     * @return
     */
    List<CustomerCountVo> sumLoss(Map<String, Object> param);

    /**
     * 计算总成本
     *
     * @param param
     * @return
     */
    BigDecimal sumCost(Map<String,Object> param);


}
