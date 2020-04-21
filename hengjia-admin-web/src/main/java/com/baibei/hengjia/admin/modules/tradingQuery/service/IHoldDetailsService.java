package com.baibei.hengjia.admin.modules.tradingQuery.service;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldDetails;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 *
 * @author: uqing
 * @date: 2019/07/15 13:39:41
 * @description: HoldDetails服务接口
 */
public interface IHoldDetailsService extends Service<HoldDetails> {

    MyPageInfo<HoldDetailsVo>pageList(HoldDetailsDto holdDetailsDto);

    List<HoldDetailsVo> HoldDetailsVoList(HoldDetailsDto holdDetailsDto);
}
