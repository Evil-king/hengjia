package com.baibei.hengjia.admin.modules.admin.service.impl;

import com.baibei.hengjia.admin.modules.admin.bean.dto.UserDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.UserPageDto;
import com.baibei.hengjia.admin.modules.admin.bean.vo.UserVo;
import com.baibei.hengjia.admin.modules.admin.dao.UserMapper;
import com.baibei.hengjia.admin.modules.admin.model.Dept;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.model.UsersRoles;
import com.baibei.hengjia.admin.modules.admin.service.IDeptService;
import com.baibei.hengjia.admin.modules.admin.service.IRoleService;
import com.baibei.hengjia.admin.modules.admin.service.IUserService;
import com.baibei.hengjia.admin.modules.admin.service.IUsersRolesService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/31 16:32:12
 * @description: User服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends AbstractService<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUsersRolesService usersRolesService;

    @Autowired
    private IRoleService roleService;


    @Override
    public User findByName(String userName) {
        return findBy("username", userName);
    }

    @Override
    public User findByEmail(String email) {
        return findBy("email", email);
    }

    @Override
    public MyPageInfo<UserVo> pageList(UserPageDto userPageDto) {
        PageHelper.startPage(userPageDto.getCurrentPage(), userPageDto.getPageSize());
        List<User> userList = userMapper.findByUser(userPageDto);
        MyPageInfo<User> myPageInfo = new MyPageInfo<>(userList);
        MyPageInfo<UserVo> pageList = PageUtil.transform(myPageInfo, UserVo.class);
        for (UserVo user : pageList.getList()) {
            Dept dept = null;
            if (user.getDeptId() != null) {
                dept = deptService.findBy("id", user.getDeptId());
            }
            user.setRoles(this.roleService.findByUserRole(user.getId()));
            user.setDeptName(dept == null ? "" : dept.getName());
        }
        return pageList;
    }

    @Override
    public void updatePass(String userName, String password) {
        Condition condition = new Condition(User.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("username", userName);
        User user = new User();
        user.setPassword(password);
        user.setLastPasswordResetTime(new Date());
        this.updateByConditionSelective(user, condition);
    }

    @Override
    public UserDTO create(User user) {
        User userName = this.findByName(user.getUsername());
        if (userName != null) {
            throw new ServiceException("用户已经存在");
        }
        User userEmail = this.findByEmail(user.getEmail());
        if (userEmail != null) {
            throw new ServiceException("email已经存在");
        }
        user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //默认密码为123456
        user.setAvatar("https://i.loli.net/2019/04/04/5ca5b971e1548.jpeg");
        user.setCreateTime(new Date());
        this.save(user);
        List<UsersRoles> usersRolesList = new ArrayList<>();
        user.getRoles().stream().forEach(
                role -> {
                    UsersRoles usersRoles = new UsersRoles();
                    usersRoles.setUserId(user.getId());
                    usersRoles.setRoleId(role.getId());
                    usersRolesList.add(usersRoles);
                }
        );
        if (!usersRolesList.isEmpty()) {
            this.usersRolesService.save(usersRolesList);
        }
        UserDTO userDTO = toDto(user);
        if (user.getEnabled().equals(Long.valueOf(Constants.Flag.VALID))) {
            userDTO.setEnabled(true);
        } else {
            userDTO.setEnabled(false);
        }
        return userDTO;
    }

    @Override
    public void updateUser(User user) {
        // stop1 修改用户信息
        this.update(user);
        // stop2 删除用户对应的角色
        this.usersRolesService.deleteByUserRole(user.getId());
        // stop3 添加用户的对应的角色
        List<UsersRoles> usersRolesList = new ArrayList<>();
        user.getRoles().stream().forEach(role -> {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setUserId(user.getId());
            usersRoles.setRoleId(role.getId());
            usersRolesList.add(usersRoles);
        });
        if (!usersRolesList.isEmpty()) {
            this.usersRolesService.save(usersRolesList);
        }
    }

    public UserDTO toDto(User user) {
        UserDTO userDTO;
        userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return userDTO;
    }

}
