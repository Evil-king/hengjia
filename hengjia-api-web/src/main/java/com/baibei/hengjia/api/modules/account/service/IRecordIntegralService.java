package com.baibei.hengjia.api.modules.account.service;
import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordIntegral;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:41:05
* @description: RecordIntegral服务接口
*/
public interface IRecordIntegralService extends Service<RecordIntegral> {

    MyPageInfo<RecordVo> getAll(RecordDto recordIntegralDto);
}
