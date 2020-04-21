package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/06 15:24:04
* @description: MatchAuthority服务接口
*/
public interface IMatchAuthorityService extends Service<MatchAuthority> {

    /**
     * 扣减配货权
     * @param customerNo
     * @param productTradeNo
     * @return
     */
    int detuchAuthority(String customerNo,String productTradeNo);

    /**
     * 新增或刷新配货权
     * @param customerNo
     * @param productTradeNo
     */
    void addOrRefleshAuthority(String customerNo,String productTradeNo);

    /**
     * 获取用户配货权
     * @param customerNo
     * @param productTradeNo
     * @return
     */
    MatchAuthority getAuthority(String customerNo,String productTradeNo);

    /**
     * 查询配货权为0的用户集合
     * @return
     */
    List<String> getCustomerListWithAuthorityZero();

    /**
     * 获取非0配货权数据
     * @return
     */
    List<MatchAuthority> getListWithAuthority();
}
