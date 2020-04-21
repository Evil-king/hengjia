package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.model.BuymatchUser;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: BuymatchUser服务接口
*/
public interface IBuymatchUserService extends Service<BuymatchUser> {
    /**
     * 获取当天符合“买入配货”规格的用户，并将其入库
     * 规格：以零售价买入商品，获得以批发价买入商品的权利（批发权）。收市后直接扣钱配仓单，收手续费。持仓；类型为：折扣商品。
     */
    void buyMatchUsers();

    /**
     * 根据批次号删除数据（物理删除）
     * @param batchNo
     */
    void deleteBybatchNo(String batchNo);

    /**
     * 获取某批次下的用户集合
     * @param batchNo
     * @param status run=已执行，unrun=未执行
     * @return
     */
    List<BuymatchUser> getByBatchNo(String batchNo,String status);

    /**
     * 获取某批次下的用户编码集合
     */
    List<String> getExistCustomerNos(String batchNo);

    /**
     * 获取某批次下当个配货用户信息
     * @param batchNo
     * @param customerNo
     * @param productTradeNo
     * @return
     */
    BuymatchUser getOneMatchUser(String batchNo,String customerNo,String productTradeNo);

}
