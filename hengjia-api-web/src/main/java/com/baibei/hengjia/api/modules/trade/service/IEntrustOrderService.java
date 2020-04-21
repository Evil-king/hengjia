package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.bean.dto.EntrustAllListDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.RevokeDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyEntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.model.EntrustOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: EntrustOrder服务接口
 */
public interface IEntrustOrderService extends Service<EntrustOrder> {


    /**
     * 初始化保存委托单
     *
     * @param customerNo
     * @param productTradeNo
     * @param direction
     * @param price
     * @param entrustNo
     * @param count
     * @return
     */
    EntrustOrder initEntrustOrder(String customerNo, String productTradeNo, String direction, String price,
                                  String entrustNo, int count, BigDecimal fee,String holdType,Byte canShow);

    /**
     * 根据entrustNo更新委托单信息
     *
     * @param entrustNo
     * @param entrustOrder
     */
    void updateEntrustOrder(String entrustNo, EntrustOrder entrustOrder);

    /**
     * 根据委托单单号查询
     *
     * @param entrustNo
     * @return
     */
    EntrustOrder findByOrderNo(String entrustNo);


    /**
     * 委托单列表
     *
     * @param entrustAllListDto
     * @return
     */
    MyPageInfo<EntrustOrderVo> allList(EntrustAllListDto entrustAllListDto);

    /**
     * 委托单列表
     *
     * @param entrustAllListDto
     * @return
     */
    MyPageInfo<EntrustOrderVo> allListForTransfer(EntrustAllListDto entrustAllListDto);

    /**
     * 我的当前委托单
     *
     * @param customerBaseAndPageDto
     * @return
     */
    MyPageInfo<MyEntrustOrderVo> myList(CustomerBaseAndPageDto customerBaseAndPageDto);

    /**
     * 撤单
     * @param revokeDto
     * @param checkTradeDay 是否需要校验 交易日。（check:校验；uncheck:不校验）
     */
    void revoke(RevokeDto revokeDto,String checkTradeDay);

    /**
     * 批量撤单
     * @param revokeDtoList
     */
    void batchRevoke(List<RevokeDto> revokeDtoList);

    /**
     * 统计挂单数量
     *
     * @param direction
     * @return
     */
    BigDecimal entrustCount(String productTraNo,String direction);

    /**
     * 撤销当前所有委托单
     * '委托单类型，normal=普通委托单，transfer=转让卖出委托单'
     * @return
     */
    void revokeAll(String transferType);


    /**
     * 查询未成交的委托单
     *
     * @param customerNo
     * @param productTradeNo
     * @param direction
     * @return
     */
    List<EntrustOrder> findNotDealEntrustOrder(String customerNo,String productTradeNo,String direction);
}
