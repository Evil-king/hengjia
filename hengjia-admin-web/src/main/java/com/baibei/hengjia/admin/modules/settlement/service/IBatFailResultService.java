package com.baibei.hengjia.admin.modules.settlement.service;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.FailResultVo;
import com.baibei.hengjia.admin.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/16 15:12:49
 * @description: BatFailResult服务接口
 */
public interface IBatFailResultService extends Service<BatFailResult> {

    MyPageInfo<FailResultVo> pageList(CleanResultDto cleanResultDto);

    List<FailResultVo> FailResultVoList(CleanResultDto cleanResultDto);
}
