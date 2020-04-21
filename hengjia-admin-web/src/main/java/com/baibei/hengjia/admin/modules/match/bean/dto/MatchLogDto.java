package com.baibei.hengjia.admin.modules.match.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class MatchLogDto extends PageParam {
    /**
     * 配票流水号
     */
    private String matchNo;

    /**
     * 用户编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:赠送；special:其他)
     */
    private String matchType;

    /**
     * 配票状态(SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功)
     */
    private String matchStatus;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
