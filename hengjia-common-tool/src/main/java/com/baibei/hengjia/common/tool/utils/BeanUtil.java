package com.baibei.hengjia.common.tool.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/14 4:51 PM
 * @description: 对象复制工具类, 依赖org.springframework.beans.BeanUtils的简单封装
 */
public class BeanUtil {
    /**
     * 复制对象
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 复制集合中的对象
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> copyProperties(List<?> source, Class<T> clazz) {
        if (source == null || source.size() == 0) {
            return Collections.emptyList();
        }
        List<T> res = new ArrayList<>(source.size());
        for (Object o : source) {
            T t = null;
            try {
                t = clazz.newInstance();
                BeanUtils.copyProperties(o, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.add(t);
        }
        return res;
    }
}
