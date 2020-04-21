package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.dto.ControlSwitchDto;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Classname ApiMatchSwitchController
 * @Description 开关
 * @Date 2019/08/8
 * @Created by Longer
 */
@Slf4j
@RestController
@RequestMapping("/api/matchSwitch")
public class ApiMatchSwitchController {
    @Autowired
    private IMatchConfigService matchConfigService;


    /**
     * 操作开关
     * @return
     */
    @PostMapping("/controlSwitch")
    public ApiResult controlSwitch(@RequestBody @Validated ControlSwitchDto controlSwitchDto) {
        ApiResult apiResult;
        try {
            matchConfigService.matchSwitchByTradeDay(controlSwitchDto.getSwtch(),controlSwitchDto.getSwitchType());
            apiResult = ApiResult.success();
        } catch (ServiceException s){
            matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, controlSwitchDto.getSwitchType());
            apiResult = ApiResult.error(s.getMessage());
        }
        catch (Exception e) {
            matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, controlSwitchDto.getSwitchType());
            e.printStackTrace();
            apiResult = ApiResult.error(e.getMessage());
        }
        return apiResult;
    }


}
