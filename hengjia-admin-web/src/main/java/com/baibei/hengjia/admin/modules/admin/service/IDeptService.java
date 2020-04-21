package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.DeptDTO;
import com.baibei.hengjia.admin.modules.admin.model.Dept;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;
import java.util.Set;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: Dept服务接口
 */
public interface IDeptService extends Service<Dept> {

    List<Dept> listByIds(List<Long> ids);

    List<Dept> getByRoleId(Long roleId);

    List<Dept> findByPid(Long pid);

    MyPageInfo<DeptDTO> buildTree(List<DeptDTO> deptList);

    List<Dept> queryAll(DeptDTO deptDTO, Set<Long> idList);

}
