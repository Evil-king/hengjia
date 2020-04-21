package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldTotal;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/07/15 15:30:06
 * @description: HoldTotal服务接口
 */
public interface IHoldTotalService extends Service<HoldTotal> {

    /**
     * 持仓汇总
     *
     * @param holdTotalDto
     * @return
     */
    MyPageInfo<HoldTotalVo> pageList(HoldTotalDto holdTotalDto);

    /**
     * 获取单个人的持仓市值（区分本票与配票）
     *
     * @param holdTotalDto
     * @return
     */
    List<HoldTotalVo> getTotal(HoldTotalDto holdTotalDto);

    /**
     * 查询用户持仓明细
     * @param holdTotalDto
     * @return
     */
    List<HoldTotalVo> findUserByHoldTotalService(HoldTotalDto holdTotalDto);
}
