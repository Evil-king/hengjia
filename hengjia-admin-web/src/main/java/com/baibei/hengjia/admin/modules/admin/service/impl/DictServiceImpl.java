package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDTO;
import com.baibei.hengjia.admin.modules.admin.dao.DictMapper;
import com.baibei.hengjia.admin.modules.admin.model.Dict;
import com.baibei.hengjia.admin.modules.admin.service.IDictService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dict服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl extends AbstractService<Dict> implements IDictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Dict findByName(String dictName) {
        return findBy("name", dictName);
    }

    @Override
    public MyPageInfo<DictDTO> queryAll(DictDTO dictDTO) {
        Condition condition = new Condition(Dict.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(dictDTO.getName())) {
            criteria.andEqualTo("name", dictDTO.getName());
        }
        if (!StringUtils.isEmpty(dictDTO.getRemark())) {
            criteria.andLike("remark", dictDTO.getRemark());
        }
        return PageUtil.transform(pageList(condition, PageParam.build(1, Integer.MAX_VALUE)), DictDTO.class);
    }

    @Override
    public void createDict(Dict dict) {
        this.save(dict);
    }

    @Override
    public void updateDict(Dict dict) {
        this.update(dict);
    }

    @Override
    public void deleteDict(Long id) {
        this.deleteById(id);
    }

}
