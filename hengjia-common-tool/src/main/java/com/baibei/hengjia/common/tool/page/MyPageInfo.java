package com.baibei.hengjia.common.tool.page;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/25 11:23 PM
 * @description: 分页列表, 简化了PageHelper库中PageInfo类的字段
 */
@Data
public class MyPageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //当前页
    private int currentPage;
    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;
    //排序
    private String orderBy;
    // 排序字段
    private String sort;
    // 排序规则,asc/desc
    private String order;
    //当前页面第一个元素在数据库中的行号
    private int startRow;
    //当前页面最后一个元素在数据库中的行号
    private int endRow;
    //总记录数
    private long total;
    //总页数
    private int pages;
    //结果集
    private List<T> list;


    public MyPageInfo() {
    }

    /**
     * 包装Page对象
     *
     * @param list
     */
    public MyPageInfo(List<T> list) {
        this(list, 8);
    }

    /**
     * 包装Page对象
     *
     * @param list          page结果
     * @param navigatePages 页码数量
     */
    public MyPageInfo(List<T> list, int navigatePages) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.currentPage = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.orderBy = page.getOrderBy();
            // 设置排序字段和排序规则
            if (!StringUtils.isEmpty(page.getOrderBy())) {
                String[] array = page.getOrderBy().split(" ");
                this.sort = array[0];
                this.order = array[1];
            }
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
            this.total = page.getTotal();
            //由于结果是>startRow的，所以实际的需要+1
            if (this.size == 0) {
                this.startRow = 0;
                this.endRow = 0;
            } else {
                this.startRow = page.getStartRow() + 1;
                //计算实际的endRow（最后一页的时候特殊）
                this.endRow = this.startRow - 1 + this.size;
            }
        } else if (list instanceof Collection) {
            this.currentPage = 1;
            this.pageSize = list.size();
            this.pages = 1;
            this.list = list;
            this.size = list.size();
            this.total = list.size();
            this.startRow = 0;
            this.endRow = list.size() > 0 ? list.size() - 1 : 0;
        }
    }

    @Override
    public String toString() {
        return "MyPageInfo{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", orderBy='" + orderBy + '\'' +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}
