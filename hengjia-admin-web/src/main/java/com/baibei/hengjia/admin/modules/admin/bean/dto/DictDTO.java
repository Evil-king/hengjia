package com.baibei.hengjia.admin.modules.admin.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author jie
* @date 2019-04-10
*/
@Data
public class DictDTO implements Serializable {

    private Long id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;
}