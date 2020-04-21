package com.baibei.hengjia.common.tool.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/25 11:31 PM
 * @description: 分页工具类
 */
@Slf4j
public class PageUtil {

    /**
     * 分页对象转换
     *
     * @param myPageInfo
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> MyPageInfo<T> transform(MyPageInfo<?> myPageInfo, Class<T> classType) {
        if (myPageInfo == null) {
            return new MyPageInfo<>();
        }
        MyPageInfo<T> pageInfoResult = new MyPageInfo<>();
        try {
            pageInfoResult.setList(copyList(myPageInfo.getList(), classType));
        } catch (Exception e) {
            log.error("transform error", e);
        }
        pageInfoResult.setCurrentPage(myPageInfo.getCurrentPage());
        pageInfoResult.setPageSize(myPageInfo.getPageSize());
        pageInfoResult.setSize(myPageInfo.getSize());
        pageInfoResult.setOrderBy(myPageInfo.getOrderBy());
        pageInfoResult.setStartRow(myPageInfo.getStartRow());
        pageInfoResult.setEndRow(myPageInfo.getEndRow());
        pageInfoResult.setTotal(myPageInfo.getTotal());
        pageInfoResult.setPages(myPageInfo.getPages());
        pageInfoResult.setSort(myPageInfo.getSort());
        pageInfoResult.setOrder(myPageInfo.getOrder());
        return pageInfoResult;
    }


    /**
     * 转换对象并复制至list
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> source, Class<T> clazz) {
        if (source == null || source.size() == 0) {
            return Collections.emptyList();
        }
        List<T> res = new ArrayList<>(source.size());
        for (Object o : source) {
            try {
                T t = clazz.newInstance();
                res.add(t);
                BeanUtils.copyProperties(o, t);
            } catch (Exception e) {
                log.error("copyList error", e);
            }
        }
        return res;
    }
}
