package com.baibei.hengjia.admin.modules.product.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/2 6:51 PM
 * @description:
 */
@Data
public class TradeProductPageListDto extends PageParam {

    private String spuNo;

    private String customerNo;
}
