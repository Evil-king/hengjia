package com.baibei.hengjia.api.modules.shop.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author hwq
 * @date 2019/06/04
 * <p>
 *     兑换积分入参
 * </p>
 */
@Data
public class ExchangePointDTO extends CustomerBaseDto {

    @NotBlank(message = "合计积分不能为空")
    private String sumPoint;

    List<OrderDetailsDTO> orderDetailsDTOList;

}
