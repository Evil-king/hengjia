package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDTO;
import com.baibei.hengjia.admin.modules.admin.model.Dict;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dict服务接口
 */
public interface IDictService extends Service<Dict> {

    Dict findByName(String dictName);

    MyPageInfo<DictDTO> queryAll(DictDTO dictDTO);

    void createDict(Dict dict);

    void updateDict(Dict dict);

    void deleteDict(Long id);
}
