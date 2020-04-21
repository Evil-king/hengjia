package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailsQueryDto;
import com.baibei.hengjia.admin.modules.admin.model.Dict_Detail;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dict_Detail服务接口
 */
public interface IDict_DetailService extends Service<Dict_Detail> {

    MyPageInfo<Dict_Detail> listByDictName(DictDetailsQueryDto dictDetailsQueryDto);


    void saveDictDetail(DictDetailDTO dictDetailDTO);

    void updateDictDetail(DictDetailDTO dictDetailDTO);

    void deleteDictDetail(Long id);
}
