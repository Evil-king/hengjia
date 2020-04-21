package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.FilePlannedSpeedDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.FilePlannedSpeedVo;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth/api/filePlannedSpeed")
public class AuthApiFilePlannedSpeedController {

    @Autowired
    private ICashService cashService;


    /**
     * 查询文件进度
     *
     * @param filePlannedSpeedDto
     * @return
     */
    @PostMapping("/findFilePlannedSpeed")
    public ApiResult<FilePlannedSpeedVo> findFilePlannedSpeed(@RequestBody FilePlannedSpeedDto filePlannedSpeedDto) {
        if (filePlannedSpeedDto.getBeginDate() == null || filePlannedSpeedDto.getEndDate() == null) {
            filePlannedSpeedDto.setBeginDate(new Date());
            filePlannedSpeedDto.setEndDate(new Date());
        }
        return cashService.filePlannedSpeed(filePlannedSpeedDto);
    }
}
