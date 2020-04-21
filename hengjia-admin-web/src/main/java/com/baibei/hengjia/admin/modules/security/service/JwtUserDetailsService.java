package com.baibei.hengjia.admin.modules.security.service;


import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DeptDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.JobDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.UserDTO;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.service.IUserService;
import com.baibei.hengjia.admin.modules.security.security.JwtUser;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author jie
 * @date 2018-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userService.findByName(username);
        if (user == null) {
            throw new ServiceException("账号不存在");
        } else {
            if (user.getEnabled().equals(Long.valueOf(Constants.Flag.UNVALID))) {
                throw new ServiceException("账号已冻结");
            }
            System.out.println("1" + JSON.toJSONString(user));
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            System.out.println("2" + JSON.toJSONString(userDTO));
            return createJwtUser(userDTO);
        }
    }

    public UserDetails createJwtUser(UserDTO user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptDTO::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobDTO::getName).orElse(null),
                permissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}
