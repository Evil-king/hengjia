package com.baibei.hengjia.common.core.mybatis;

import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2017/7/6 19:05
 * @description: Service层基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
    boolean save(T model);//持久化

    void save(List<T> models);//批量持久化

    boolean deleteById(Long id);//通过主鍵刪除

    void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”

    boolean update(T model);//更新

    boolean updateByConditionSelective(T model, Condition condition);//根据条件更新

    T findById(Integer id);//通过ID查找

    T findById(Long id);//通过ID查找

    T findBy(String fieldName, Object value); //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”

    List<T> findByCondition(Condition condition);//根据条件查找

    List<T> findAll();//获取所有

    int selectCountByCondition(Condition condition); //count

    MyPageInfo<T> pageList(Condition condition, PageParam pageParam); //分页

    Condition buildValidCondition(Class clazz);  // 获取flag=1的condition条件

    Example.Criteria buildValidCriteria(Condition condition); // 获取flag=1的Criteria

    T findOneByCondition(Condition condition);

    /**
     * 通过主键软删除
     *
     * @param id
     * @return
     */
    boolean softDeleteById(Long id);

    /**
     * 批量软删除，多个id通过逗号,隔开
     *
     * @param ids
     * @return
     */
    boolean softDeleteByIds(String ids);


    /**
     * 批量软删除,通过Set集合进行软删除
     *
     * @param ids
     */
    void batchDelete(Iterable<String> ids);

    /**
     * 批量修改
     *
     * @param ids   批量修改的id
     * @param model 批量修改的实体类
     */
    void batchUpdate(Iterable<String> ids, T model);

}
