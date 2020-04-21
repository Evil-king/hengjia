package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DictDetailsQueryDto;
import com.baibei.hengjia.admin.modules.admin.dao.Dict_DetailMapper;
import com.baibei.hengjia.admin.modules.admin.model.Dict;
import com.baibei.hengjia.admin.modules.admin.model.Dict_Detail;
import com.baibei.hengjia.admin.modules.admin.service.IDictService;
import com.baibei.hengjia.admin.modules.admin.service.IDict_DetailService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dict_Detail服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class Dict_DetailServiceImpl extends AbstractService<Dict_Detail> implements IDict_DetailService {

    @Autowired
    private Dict_DetailMapper dictDetailMapper;
    @Autowired
    private IDictService dictService;

    @Override
    public MyPageInfo<Dict_Detail> listByDictName(DictDetailsQueryDto queryDto) {
        Dict dict = dictService.findByName(queryDto.getDictName());
        Condition condition = new Condition(Dict_Detail.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("dictId", dict.getId());
        if (queryDto.getLabel() != null) {
            criteria.andEqualTo("label", queryDto.getLabel());
        }
        return new MyPageInfo<>(findByCondition(condition));
    }

    @Override
    public void saveDictDetail(DictDetailDTO dictDetailDTO) {
        Dict_Detail dict_detail = BeanUtil.copyProperties(dictDetailDTO, Dict_Detail.class);
        this.save(dict_detail);
    }

    @Override
    public void updateDictDetail(DictDetailDTO dictDetailDTO) {
        Dict_Detail dict_detail = BeanUtil.copyProperties(dictDetailDTO, Dict_Detail.class);
        this.update(dict_detail);
    }

    @Override
    public void deleteDictDetail(Long id) {
        this.deleteById(id);
    }
}
