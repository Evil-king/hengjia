package com.baibei.hengjia.api.modules.cash.base;


import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -8077385476032641582L;
    //流水号
    private String thirdLogNo;

    //保留域
    private String Reserve;
}
