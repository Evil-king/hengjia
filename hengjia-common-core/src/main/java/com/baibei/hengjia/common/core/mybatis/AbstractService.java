package com.baibei.hengjia.common.core.mybatis;


import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2017/7/6 17:22
 * @description: 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {

    @Autowired
    protected MyMapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public boolean save(T model) {

        return mapper.insertSelective(model) > 0;
    }

    @Override
    public void save(List<T> models) {
        mapper.insertList(models);
    }

    @Override
    public boolean deleteById(Long id) {
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public boolean update(T model) {
        return mapper.updateByPrimaryKeySelective(model) > 0;
    }

    @Override
    public boolean updateByConditionSelective(T model, Condition condition) {
        return mapper.updateByConditionSelective(model, condition) > 0;
    }

    @Override
    public T findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(String fieldName, Object value) {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int selectCountByCondition(Condition condition) {
        return mapper.selectCountByCondition(condition);
    }

    @Override
    public MyPageInfo<T> pageList(Condition condition, PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrentPage(), pageParam.getPageSize());
        if (!StringUtils.isEmpty(pageParam.getSort()) && !StringUtils.isEmpty(pageParam.getOrder())) {
            PageHelper.orderBy(new StringBuffer().append(pageParam.getSort()).append(" ").append(pageParam.getOrder()).toString());
        }
        List<T> list = this.findByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        MyPageInfo<T> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public Condition buildValidCondition(Class clazz) {
        Condition condition = new Condition(clazz);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        return condition;
    }

    @Override
    public Example.Criteria buildValidCriteria(Condition condition) {
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        return criteria;
    }

    @Override
    public T findOneByCondition(Condition condition) {
        List<T> list = findByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("查询返回记录数大于1");
        }
        return list.get(0);
    }

    @Override
    public boolean softDeleteById(Long id) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andEqualTo("id", id);
        return softDelete(condition);
    }

    @Override
    public boolean softDeleteByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("ids not be null");
        }
        String[] idArray = ids.split(",");
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andIn("id", Arrays.asList(idArray));
        return softDelete(condition);
    }

    public boolean softDelete(Condition condition) {
        try {
            T model = modelClass.newInstance();
            // flag修改为0表示已删除
            Field flag = modelClass.getDeclaredField("flag");
            flag.setAccessible(true);
            flag.set(model, new Byte(Constants.Flag.UNVALID));
            // 更新时间
            Field modifyTime = modelClass.getDeclaredField("modifyTime");
            modifyTime.setAccessible(true);
            modifyTime.set(model, new Date());
            return this.updateByConditionSelective(model, condition);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdate(Iterable<String> ids, T model) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andIn("id", ids);
        this.updateByConditionSelective(model, condition);
    }

    @Override
    public void batchDelete(Iterable<String> iterable) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andIn("id", iterable);
        softDelete(condition);
    }
}
