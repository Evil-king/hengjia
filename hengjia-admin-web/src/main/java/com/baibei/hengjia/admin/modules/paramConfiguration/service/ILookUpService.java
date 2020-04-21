package com.baibei.hengjia.admin.modules.paramConfiguration.service;
import com.baibei.hengjia.admin.modules.paramConfiguration.model.LookUp;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: wenqing
* @date: 2019/09/04 15:17:57
* @description: LookUp服务接口
*/
public interface ILookUpService extends Service<LookUp> {

    int addLogistics(String name);

    int deleteToId(long id);

    int selectByName(String name);

}
