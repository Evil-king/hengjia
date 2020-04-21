package com.baibei.hengjia.admin.modules.settlement.service;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.AmountReturnDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.AmountReturnVo;
import com.baibei.hengjia.admin.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/12 10:55:58
 * @description: AmountReturn服务接口
 */
public interface IAmountReturnService extends Service<AmountReturn> {

    MyPageInfo<AmountReturnVo> pageList(AmountReturnDto amountReturnDto);

}
