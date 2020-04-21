package com.baibei.hengjia.admin.modules.withdraw.service;

import com.baibei.hengjia.admin.modules.withdraw.bean.dto.WithdrawListDto;
import com.baibei.hengjia.admin.modules.withdraw.bean.vo.WithdrawListVo;
import com.baibei.hengjia.admin.modules.withdraw.model.OrderWithdraw;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/07/15 10:27:46
 * @description: OrderWithdraw服务接口
 */
public interface IOrderWithdrawService extends Service<OrderWithdraw> {

    MyPageInfo<WithdrawListVo> pageList(WithdrawListDto withdrawListDto);

    ApiResult reviewStatus(String orderNo, String type);

    List<WithdrawListVo> list(WithdrawListDto withdrawListDto);
}
