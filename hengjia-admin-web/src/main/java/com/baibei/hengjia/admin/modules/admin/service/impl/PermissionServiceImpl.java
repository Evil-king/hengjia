package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.bean.dto.PermissionDTO;
import com.baibei.hengjia.admin.modules.admin.dao.PermissionMapper;
import com.baibei.hengjia.admin.modules.admin.model.Permission;
import com.baibei.hengjia.admin.modules.admin.model.RolesPermissions;
import com.baibei.hengjia.admin.modules.admin.service.IPermissionService;
import com.baibei.hengjia.admin.modules.admin.service.IRolesPermissionsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Permission服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends AbstractService<Permission> implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private IRolesPermissionsService rolesPermissionsService;

    @Override
    public List<Permission> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Condition condition = new Condition(Permission.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("id", ids);
        return findByCondition(condition);
    }

    @Override
    public List<Permission> findByPid(Long pid) {
        Condition condition = new Condition(Permission.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid", pid);
        return findByCondition(condition);
    }

    @Override
    public Object getPermissionTree(List<Permission> permissions) {
        List<Map<String, Object>> list = new LinkedList<>();
        permissions.forEach(permission -> {
                    if (permission != null) {
                        List<Permission> permissionList = findByPid(permission.getId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", permission.getId());
                        map.put("label", permission.getAlias());
                        if (permissionList != null && permissionList.size() != 0) {
                            map.put("children", getPermissionTree(permissionList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<PermissionDTO> listByRoleId(Long roleId) {
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        List<Permission> list = listByIds(rolesPermissionsService.getPermissionList(roleIdList));
        return BeanUtil.copyProperties(list, PermissionDTO.class);
    }

    @Override
    public MyPageInfo<PermissionDTO> buildTree(List<PermissionDTO> permissionDTOS) {
        List<PermissionDTO> trees = new ArrayList<PermissionDTO>();

        for (PermissionDTO permissionDTO : permissionDTOS) {

            if ("0".equals(permissionDTO.getPid().toString())) {
                trees.add(permissionDTO);
            }

            for (PermissionDTO it : permissionDTOS) {
                if (it.getPid().toString().equals(permissionDTO.getId().toString())) {
                    if (permissionDTO.getChildren() == null) {
                        permissionDTO.setChildren(new ArrayList<PermissionDTO>());
                    }
                    permissionDTO.getChildren().add(it);
                }
            }
        }
        return new MyPageInfo<>(trees.size() == 0 ? permissionDTOS : trees);
    }

    @Override
    public List<PermissionDTO> queryAll(String name) {
        Condition condition = new Condition(Permission.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtil.isEmpty(name)) {
            criteria.andLike("alias", "%" + name + "%");
        }
        List<Permission> list = findByCondition(condition);
        return BeanUtil.copyProperties(list, PermissionDTO.class);
    }

    @Override
    public void create(Permission permission) {
        permission.setCreateTime(new Date());
        save(permission);
    }

    @Override
    public ApiResult delete(Long id) {
        if(StringUtils.isEmpty(id)){
            throw  new ServiceException("删除权限不明确");
        }
        Condition condition = new Condition(Permission.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid",id);
        List<Permission> permissionList = permissionMapper.selectByCondition(condition);
        if(permissionList.size()>0){
            throw new ServiceException("删除失败，存在下级权限");
        }
        //查看该权限是否已经被引用
        List<RolesPermissions> refByPermissionId = rolesPermissionsService.getRefByPermissionId(id);
        if(refByPermissionId.size()>0){
            throw  new ServiceException("删除权限失败，该权限已被引用");
        }
        this.deleteById(id);
        return ApiResult.success();
    }
}
