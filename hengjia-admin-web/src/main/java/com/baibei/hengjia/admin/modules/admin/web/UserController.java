package com.baibei.hengjia.admin.modules.admin.web;

import com.baibei.hengjia.admin.config.DataScope;
import com.baibei.hengjia.admin.modules.admin.bean.dto.UserDTO;
import com.baibei.hengjia.admin.modules.admin.bean.dto.UserPageDto;
import com.baibei.hengjia.admin.modules.admin.bean.vo.UserVo;
import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.service.IUserService;
import com.baibei.hengjia.admin.modules.security.utils.EncryptUtils;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jie
 * @date 2018-11-23
 */
@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private IUserService userService;
/*
    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private PictureService pictureService;*/

    @Autowired
    private DataScope dataScope;
/*
    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VerificationCodeService verificationCodeService;*/


    private static final String ENTITY_NAME = "user";

    //@Log("查询用户")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ApiResult<MyPageInfo<UserVo>> getUsers(UserPageDto userPageDto) {
        return ApiResult.success(userService.pageList(userPageDto));
    }

    @PostMapping(value = "/users/validPass")
    public ResponseEntity validPass(@RequestBody User user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        Map map = new HashMap();
        map.put("status", 200);
        if (!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))) {
            map.put("status", 400);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/users/updatePass")
    public ApiResult updatePass(@RequestBody User user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if (userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))) {
            throw new ServiceException("新密码不能与旧密码相同");
        }
        userService.updatePass(userDetails.getUsername(), EncryptUtils.encryptPassword(user.getPassword()));
        return ApiResult.success();
    }

    /**
     * 创建用户
     *
     * @param resources
     * @return
     */
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ApiResult create(@Validated @RequestBody User resources) {
        /* checkLevel(resources);*/
        ApiResult result;
        try {
            UserDTO userDTO = userService.create(resources);
            result = ApiResult.success(userDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = ApiResult.error(ex.getMessage());
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @param resources
     * @return
     */
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@RequestBody User resources) {
        /* checkLevel(resources);*/
        userService.updateUser(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

/*

    //@Log("修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@Validated(User.Update.class) @RequestBody User resources) {
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    //@Log("删除用户")
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = Collections.min(roleService.findByUsers_Id(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));

        if (currentLevel > optLevel) {
            throw new ServiceException("角色权限不足");
        }
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }*/

    /**
     * 验证密码
     * @param user
     * @return
     *//*
    @PostMapping(value = "/users/validPass")
    public ResponseEntity validPass(@RequestBody User user){
        UserDetails userDetails = SecurityUtils.getUserDetails();
        Map map = new HashMap();
        map.put("status",200);
        if(!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))){
           map.put("status",400);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }



    *//**
     * 修改头像
     * @param file
     * @return
     *//*
    @PostMapping(value = "/users/updateAvatar")
    public ResponseEntity updateAvatar(@RequestParam MultipartFile file){
        Picture picture = pictureService.upload(file, SecurityUtils.getUsername());
        userService.updateAvatar(SecurityUtils.getUsername(),picture.getUrl());
        return new ResponseEntity(HttpStatus.OK);
    }

    *//**
     * 修改邮箱
     * @param user
     * @param user
     * @return
     *//*
    @Log("修改邮箱")
    @PostMapping(value = "/users/updateEmail/{code}")
    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody User user){
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if(!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))){
            throw new BadRequestException("密码错误");
        }
        VerificationCode verificationCode = new VerificationCode(code, ElAdminConstant.RESET_MAIL,"email",user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(userDetails.getUsername(),user.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }
    */

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources
     */
    /*private void checkLevel(User resources) {
        Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new ServiceException("角色权限不足");
        }
    }*/

}
