package com.baibei.hengjia.admin.modules.admin.web;

import com.baibei.hengjia.admin.modules.admin.model.User;
import com.baibei.hengjia.admin.modules.admin.service.IUserService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/31 4:35 PM
 * @description:
 */
@RestController
@RequestMapping("/admin/user")
public class UserTestController {
    @Autowired
    private IUserService userService;

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/pageList")
    public ApiResult<MyPageInfo<User>> pageList(@RequestBody PageParam pageParam) {

        return ApiResult.success(userService.pageList(new Condition(User.class), pageParam));
    }
}
