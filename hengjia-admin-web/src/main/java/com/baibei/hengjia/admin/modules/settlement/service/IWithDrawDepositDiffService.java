package com.baibei.hengjia.admin.modules.settlement.service;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.WithDrawDepositDiffDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.WithDrawDepositDiffVo;
import com.baibei.hengjia.admin.modules.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/12 18:10:38
* @description: WithDrawDepositDiff服务接口
*/
public interface IWithDrawDepositDiffService extends Service<WithDrawDepositDiff> {

    MyPageInfo<WithDrawDepositDiffVo> pageList(WithDrawDepositDiffDto withDrawDepositDiffDto);

    /**
     * 出入金对账处理服务
     * @param diffId
     * @return
     */
    ApiResult dealDiff(Long diffId);

    List<WithDrawDepositDiffVo> WithDrawDepositDiffVoList(WithDrawDepositDiffDto withDrawDepositDiffDto);
}
