package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradeRecordVo;
import com.baibei.hengjia.api.modules.trade.model.AutoRecord;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoRecord服务接口
 */
public interface IAutoRecordService extends Service<AutoRecord> {

    MyPageInfo<AutoTradeRecordVo> pageList(CustomerBaseAndPageDto dto);

}
