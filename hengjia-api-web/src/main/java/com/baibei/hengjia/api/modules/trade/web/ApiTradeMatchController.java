package com.baibei.hengjia.api.modules.trade.web;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MatchVo;
import com.baibei.hengjia.api.modules.trade.service.IMatchLogService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname ApiTradeMatchController
 * @Description 配票
 * @Date 2019/6/10 11:00
 * @Created by Longer
 */
@Slf4j
@RestController
@RequestMapping("/api/match")
public class ApiTradeMatchController {
    @Autowired
    private IMatchLogService matchLogService;


    @PostMapping("/matchs")
    public ApiResult<MatchVo> match(@RequestBody @Validated List<MatchDto> matchDtoList ){
        log.info("开始执行休市交易配票逻辑...");
        ApiResult apiResult;
        try {
            MatchListDto matchListDto = new MatchListDto();
            matchListDto.setMatchDtoList(matchDtoList);
            MatchVo matchVo = matchLogService.match(matchListDto);
            apiResult = ApiResult.success(matchVo.getDisMatchList());
        } catch (ServiceException s){
            apiResult = ApiResult.error(s.getMessage());
            s.printStackTrace();
        }
        catch (Exception e) {
            apiResult = ApiResult.error(e.getMessage());
            e.printStackTrace();
        }
        log.info("结束执行休市交易配票逻辑...apiResult={}",JSON.toJSONString(apiResult));
        return apiResult;
    }
}
