package com.baibei.hengjia.admin.modules.admin.service;
import com.baibei.hengjia.admin.modules.admin.model.RolesDepts;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: 会跳舞的机器人
* @date: 2019/05/31 16:32:12
* @description: RolesDepts服务接口
*/
public interface IRolesDeptsService extends Service<RolesDepts> {

    List<Long> getDeptIdList(Long roleId);

}
