package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DeptDTO;
import com.baibei.hengjia.admin.modules.admin.dao.DeptMapper;
import com.baibei.hengjia.admin.modules.admin.model.Dept;
import com.baibei.hengjia.admin.modules.admin.service.IDeptService;
import com.baibei.hengjia.admin.modules.admin.service.IRolesDeptsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dept服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl extends AbstractService<Dept> implements IDeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private IRolesDeptsService rolesDeptsService;

    @Override
    public List<Dept> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Condition condition = new Condition(Dept.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("id", ids);
        return findByCondition(condition);
    }

    @Override
    public List<Dept> getByRoleId(Long roleId) {
        List<Long> deptIdList = rolesDeptsService.getDeptIdList(roleId);
        return listByIds(deptIdList);
    }

    @Override
    public List<Dept> findByPid(Long pid) {
        Condition condition = new Condition(Dept.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid", pid);
        return findByCondition(condition);
    }

    @Override
    public MyPageInfo<DeptDTO> buildTree(List<DeptDTO> deptDTOList) {
        if (CollectionUtils.isEmpty(deptDTOList)) {
            return null;
        }
        List<DeptDTO> trees = new LinkedList<>();
        List<DeptDTO> list = new LinkedList<>();
        List<String> deptNames = deptDTOList.stream().map(DeptDTO::getName).collect(Collectors.toList());
        Boolean isChild;
        for (DeptDTO deptDTO : deptDTOList) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                list.add(deptDTO);
            }
            for (DeptDTO it : deptDTOList) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
           /* if (isChild)
                list.add(deptDTO);
            else if (!deptNames.contains(findNameById(deptDTO.getPid())))
                list.add(deptDTO);*/
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = list;
        }

        return new MyPageInfo<>(trees.size() == 0 ? deptDTOList : trees);

        // map.put("content", CollectionUtils.isEmpty(trees) ? deptDTOList : trees);
        //return map;
    }

    @Override
    public List<Dept> queryAll(DeptDTO deptDTO, Set<Long> idList) {
        System.out.println(JSON.toJSONString(idList));
        Condition condition = new Condition(Dept.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!idList.isEmpty()) {
            criteria.andIn("id", Arrays.asList(idList.toArray()));
        }
        return findByCondition(condition);
    }

    private String findNameById(Long id) {
        Condition condition = new Condition(Dept.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", id);
        List<Dept> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0).getName();
    }
}
