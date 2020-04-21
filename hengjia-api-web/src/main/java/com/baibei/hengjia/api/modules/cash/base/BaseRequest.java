package com.baibei.hengjia.api.modules.cash.base;


import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 基础的请求类,要走网关
 */
@Data
public class BaseRequest extends CustomerBaseDto implements Serializable {


    private static final long serialVersionUID = -5795267981172161441L;

    @Size(max = 32, message = "会员子账号长度错误")
    private String supAcctId; // 资金汇总账号


}