package com.baibei.hengjia.admin.modules.settlement.service;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.BankOrderDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.model.BankOrder;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: 会跳舞的机器人
* @date: 2019/07/12 10:46:21
* @description: BankOrder服务接口
*/
public interface IBankOrderService extends Service<BankOrder> {

    MyPageInfo<BankOrderVo> pageList(BankOrderDto bankOrderDto);

    List<BankOrderVo> BankOrderVoList(BankOrderDto bankOrderDto);
}
