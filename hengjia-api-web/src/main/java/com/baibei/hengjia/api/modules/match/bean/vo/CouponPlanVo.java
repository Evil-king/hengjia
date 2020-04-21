package com.baibei.hengjia.api.modules.match.bean.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/8 10:55
 * @description:
 */
@Data
public class CouponPlanVo {
        private Integer totalAmout;
        private Integer successAmount;
        private Integer failAmount;
        private List<String> planNos;
}
