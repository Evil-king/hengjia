package com.baibei.hengjia.admin.modules.settlement.service;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CustDzFailVo;
import com.baibei.hengjia.admin.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/16 15:12:49
 * @description: CustDzFail服务接口
 */
public interface ICustDzFailService extends Service<CustDzFail> {

    MyPageInfo<CustDzFailVo> pageList(CleanResultDto cleanResultDto);

    List<CustDzFailVo> CustDzFailVoList(CleanResultDto cleanResultDto);
}
