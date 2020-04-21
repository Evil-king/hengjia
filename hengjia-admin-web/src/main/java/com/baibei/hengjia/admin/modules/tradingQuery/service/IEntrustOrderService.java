package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.EntrustOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.EntrustOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/07/18 11:01:43
 * @description: EntrustOrder服务接口
 */
public interface IEntrustOrderService extends Service<EntrustOrder> {

    MyPageInfo<EntrustOrderVo> pageList(EntrustOrderDto entrustOrderDto);

    List<EntrustOrderVo> EntrustOrderVoList(EntrustOrderDto entrustOrderDto);
}
