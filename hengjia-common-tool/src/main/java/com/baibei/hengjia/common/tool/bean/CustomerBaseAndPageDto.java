package com.baibei.hengjia.common.tool.bean;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 5:34 PM
 * @description: 该类整合了网关转发过来的客户ID编号参数以及分页参数
 */
@Data
public class CustomerBaseAndPageDto extends CustomerBaseDto {
    // 当前页
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage;

    // 每页记录数
    @NotNull(message = "每页记录数不能为空")
    @Max(value = 20, message = "每页记录数不能超过20")
    private Integer pageSize;

    // 排序字段
    private String sort;

    // 排序规则,asc/desc
    private String order;
}
