package com.baibei.hengjia.admin.modules.settlement.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/8 3:51 PM
 * @description:
 */
@Data
public class CleanDataPageDto extends PageParam {
    private String batchNo;
    private String customerNo;
}
