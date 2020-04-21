package com.baibei.hengjia.admin.modules.admin.service;

import com.baibei.hengjia.admin.modules.admin.bean.dto.UserDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.UserPageDto;
import com.baibei.hengjia.admin.modules.admin.bean.vo.UserVo;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: User服务接口
 */
public interface IUserService extends Service<User> {

    User findByName(String userName);

    User findByEmail(String email);

    MyPageInfo<UserVo> pageList(UserPageDto userPageDto);

    void updatePass(String userName, String password);

    UserDTO create(User user);

    void updateUser(User user);
}
