package com.baibei.hengjia.admin.modules.security.rest;

import com.baibei.hengjia.admin.modules.security.security.AuthenticationInfo;
import com.baibei.hengjia.admin.modules.security.security.AuthorizationUser;
import com.baibei.hengjia.admin.modules.security.security.JwtUser;
import com.baibei.hengjia.admin.modules.security.utils.EncryptUtils;
import com.baibei.hengjia.admin.modules.security.utils.JwtTokenUtil;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 登录授权
     *
     * @param authorizationUser
     * @return
     */
    //@Log("用户登录")
    @PostMapping(value = "${jwt.auth.path}")
    public ApiResult login(@Validated @RequestBody AuthorizationUser authorizationUser) {

        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(authorizationUser.getPassword()))) {
            return ApiResult.error("密码错误");
        }

        if (!jwtUser.isEnabled()) {
            return ApiResult.error("账号已停用，请联系管理员");
        }

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        // 每次登陆会生成新的token
        jwtTokenUtil.redisTokenSave(jwtUser.getUsername(), token);
        // 返回 token
        return ApiResult.success(new AuthenticationInfo(token, jwtUser));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "${jwt.auth.account}")
    public ResponseEntity getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    @GetMapping(path = "/loginOut")
    public ApiResult loginOut() {
        String username = SecurityUtils.getUsername();
        jwtTokenUtil.redisTokenClear(username);
        SecurityContextHolder.clearContext();
        return ApiResult.success();
    }
}
