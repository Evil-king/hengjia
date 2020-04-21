package com.baibei.hengjia.api.modules.settlement.web;

import com.baibei.hengjia.api.modules.settlement.biz.CleanBiz;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/3 3:30 PM
 * @description:
 */
@RestController
@RequestMapping("/api/settlement")
@Slf4j
public class ApiCleanController {
    @Autowired
    private CleanBiz cleanBiz;

    /**
     * 生成清算数据
     *
     * @return
     */
    @GetMapping("/generateCleanData")
    public ApiResult<String> generateCleanData(@RequestParam("batchNo") String batchNo) {
        cleanBiz.generateCleanData(batchNo);
        return ApiResult.success();
    }

    /**
     * 生成清算文件，发送清算请求至银行
     *
     * @return
     */
    @GetMapping("/launchClean")
    public ApiResult<String> launchClean(@RequestParam("batchNo") String batchNo) {
        cleanBiz.launchClean(batchNo);
        return ApiResult.success();
    }

    /**
     * 生成清算状态
     *
     * @param batchNo
     * @return
     */
    @GetMapping("/cleanStatus")
    public ApiResult<String> cleanStatus(@RequestParam("batchNo") String batchNo) {
        if (StringUtils.isEmpty(batchNo)) {
            return ApiResult.badParam("批次号不能为空");
        }
        try {
            cleanBiz.updateCleanStatus(batchNo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error("生成清算状态异常");
        }
        return ApiResult.success("生成清算状态成功");
    }

}
