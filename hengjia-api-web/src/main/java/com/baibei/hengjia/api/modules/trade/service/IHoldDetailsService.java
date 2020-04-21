package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: HoldDetails服务接口
 */
public interface IHoldDetailsService extends Service<HoldDetails> {


    /**
     * 列出客户有剩余数量的持仓明细列表
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @param sort
     * @param order
     * @return
     */
    List<HoldDetails> listByParams(String customerNo, String productTradeNo, String holdType, String sort, String order);

    /**
     * 列出客户有剩余数量的持仓明细列表
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @return
     */
    List<HoldDetails> listByParamsForTrade(String customerNo, String productTradeNo, String holdType);

    /**
     * 列出客户有剩余数量的持仓明细列表
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @param sort
     * @param order
     * @return
     */
    List<HoldDetails> listByParamsWithoutScan(String customerNo, String productTradeNo, String holdType, String sort, String order);

    /**
     * 查询达到可交易日期的持仓详情列表
     *
     * @return
     */
    List<HoldDetails> findCanTradeList();

    /**
     * 扣除挂牌商持仓明细
     *
     * @param memberNo       挂牌商编码
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean deductMemberProductHold(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType);

    /**
     * 根据解锁时间扣减持仓（总持仓和持仓明细）
     *
     * @param memberNo
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean deductProductHoldByTradeTime(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType);

    /**
     * 查询客户持仓明细商品
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @return
     */
    List<HoldDetails> findByCustomerAndProductNo(String customerNo, String productTradeNo, String holdType);


    List<HoldDetails> findHoldDetailsForNext(Map<String, Object> param);


    /**
     * 根据成交单编号查询成交的持仓明细
     *
     * @param dealOrderNoList
     * @return
     */
    List<HoldDetails> findByDealOrderNO(List<String> dealOrderNoList);

    /**
     * 挂牌卖出冻结
     *
     * @param holdDetails
     * @param frozenCount
     * @return
     */
    boolean frozen(HoldDetails holdDetails, int frozenCount);

    /**
     * 撤单解冻
     *
     * @param holdDetails
     * @param count
     * @return
     */
    boolean unfrozen(HoldDetails holdDetails, BigDecimal count);


    /**
     * 扣除持仓明细
     *
     * @param holdDetails
     * @param count
     * @return
     */
    boolean deduct(HoldDetails holdDetails, BigDecimal count);


    /**
     * 冻结成交扣除
     *
     * @param holdDetails
     * @param count
     * @return
     */
    boolean frozenDeduct(HoldDetails holdDetails, BigDecimal count);


}
