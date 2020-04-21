package com.baibei.hengjia.api.modules.trade.bean.vo;

import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MatchVo
 * @Description 配票VO
 * @Date 2019/6/10 14:36
 * @Created by Longer
 */
@Data
public class MatchVo {
    /**
     * 未全部配送成功的信息集合
     */
    private List<MatchDto> disMatchList = new ArrayList<>();
}
