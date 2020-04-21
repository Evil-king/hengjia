package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.bean.dto.MyHoldDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MyHoldNewDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryHoldVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldNewVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.StatisticsVo;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: HoldTotal服务接口
 */
public interface IHoldTotalService extends Service<HoldTotal> {
    /**
     * 查询客户持仓商品
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @return
     */
    HoldTotal findByCustomerAndProductNo(String customerNo, String productTradeNo, String holdType);


    /**
     * 冻结并减少客户持仓商品可卖数量
     *
     * @param customerNo
     * @param productTradeNo
     * @param frozenCount
     * @param holdType
     * @return
     */
    boolean frozenCustomerProductHold(String customerNo, String productTradeNo, int frozenCount, String holdType);


    /**
     * 解冻客户商品持仓并扣减总量
     *
     * @param customerNo
     * @param productTradeNo
     * @param frozenCount
     * @param holdType
     * @return
     */
    boolean unfrozenCustomerProductHold(String customerNo, String productTradeNo, int frozenCount, String holdType);


    /**
     * 扣除客户商品持仓
     *
     * @param customerNo
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean deductCustomerProductHold(String customerNo, String productTradeNo, int changeAmount, String holdType);


    /**
     * 增加客户商品持仓
     *
     * @param customerNo
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean addCustomerProductHold(String customerNo, String productTradeNo, int changeAmount, String holdType);

    boolean addCustomerProductHoldForMatch(String customerNo, String productTradeNo, BigDecimal changeAmount, String holdType);

    /**
     * 统计客户持仓相关数据
     *
     * @param customerNo
     * @return
     */
    StatisticsVo marketValue(String customerNo);

    /**
     * 我的持仓单列表
     *
     * @param myHoldDto
     * @return
     */
    MyPageInfo<MyHoldVo> myHoldList(MyHoldDto myHoldDto);

    /**
     * 我的持仓单列表
     *
     * @param myHoldDto
     * @return
     */
    MyPageInfo<MyHoldNewVo> myHoldList(MyHoldNewDto myHoldDto);


    List<HoldTotal> matchHoldList(String customerNo);

    void updateTotalCountById(Long id, String deliveryMatchCount);

    /**
     * 提货持仓信息
     *
     * @param customerNo
     * @param productTradeNo
     * @param holdType
     * @return
     */
    MyDeliveryHoldVo findMyDeliveryHold(String customerNo, String productTradeNo, String holdType);

    /**
     * 扣除挂牌商商品持仓
     *
     * @param memberNo       挂牌商编码
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean deductMemberProductHold(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType);

    /**
     * 扣除挂牌商商品持仓
     *
     * @param memberNo       挂牌商编码
     * @param productTradeNo
     * @param changeAmount
     * @param holdType
     * @return
     */
    boolean deductProductHoldByTradeTime(String memberNo, String productTradeNo, BigDecimal changeAmount, String holdType, BigDecimal detuchCanSellCount);

    /**
     * 查询可卖数量大于0的持仓汇总
     *
     * @return
     */
    List<HoldTotal> findCanSellHoldTotal(String productTradeNo);
}
