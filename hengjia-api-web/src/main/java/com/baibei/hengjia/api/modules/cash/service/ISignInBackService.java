package com.baibei.hengjia.api.modules.cash.service;
import com.baibei.hengjia.api.modules.cash.model.SignInBack;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.constants.Constants;


/**
* @author: uqing
* @date: 2019/06/18 14:50:10
* @description: SignInBack服务接口
*/
public interface ISignInBackService extends Service<SignInBack> {

    /**
     * 查询今天是否签到|签退
     * @param status 签到 1 ,签退 2
     * @return
     */
    Boolean isToDaySignInBack(String status);

}

