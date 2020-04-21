package com.baibei.hengjia.admin.feign;

import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/10 8:17 PM
 * @description: API接口调用
 */
@FeignClient(name = "apiFeign", url = "${api.url}")
public interface ApiFeign {

    /**
     * 签退
     *
     * @return
     */
    @GetMapping("/admin/settlement/signBack")
    ApiResult signBack(@RequestParam("batchNo") String batchNo);


    /**
     * 发起出入金对账
     *
     * @return
     */
    @GetMapping("/admin/settlement/accountcheck")
    ApiResult accountcheck(@RequestParam("batchNo") String batchNo);


    /**
     * 生成业务办理数据
     *
     * @return
     */
    @GetMapping("/admin/settlement/amountReturn")
    ApiResult amountReturn(@RequestParam("batchNo") String batchNo);

    /**
     * 生成清算数据
     *
     * @return
     */
    @GetMapping("/admin/settlement/generateCleanData")
    ApiResult generateCleanData(@RequestParam("batchNo") String batchNo);

    /**
     * 发送清算文件至银行
     *
     * @return
     */
    @GetMapping("/admin/settlement/launchClean")
    ApiResult launchClean(@RequestParam("batchNo") String batchNo);

    /**
     * 查看清算进度
     *
     * @return
     */
    @GetMapping("/admin/settlement/cleanProcess")
    ApiResult cleanProcess(@RequestParam("batchNo") String batchNo);

    /**
     * 业务办理
     *
     * @return
     */
    @GetMapping("/admin/amountReturn/processById")
    ApiResult processById(@RequestParam("id") String id);
}
