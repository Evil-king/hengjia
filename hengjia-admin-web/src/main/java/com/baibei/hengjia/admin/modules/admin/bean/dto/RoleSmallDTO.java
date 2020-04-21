package com.baibei.hengjia.admin.modules.admin.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jie
 * @date 2018-11-23
 */
@Data
public class RoleSmallDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
