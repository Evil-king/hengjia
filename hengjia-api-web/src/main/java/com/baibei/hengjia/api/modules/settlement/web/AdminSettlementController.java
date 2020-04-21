package com.baibei.hengjia.api.modules.settlement.web;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.cash.bean.dto.FilePlannedSpeedDto;
import com.baibei.hengjia.api.modules.cash.bean.dto.ReconciliationMatchingDto;
import com.baibei.hengjia.api.modules.cash.bean.dto.SignInBackDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.FilePlannedSpeedVo;
import com.baibei.hengjia.api.modules.cash.bean.vo.ReconciliationMatchingVo;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.settlement.biz.AmountReturnBiz;
import com.baibei.hengjia.api.modules.settlement.biz.CleanBiz;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/20 3:03 PM
 * @description: 清算操作
 */
@RestController
@RequestMapping("/admin/settlement")
@Slf4j
public class AdminSettlementController {
    @Autowired
    private ICashService cashService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private AmountReturnBiz amountReturnBiz;
    @Autowired
    private CleanBiz cleanBiz;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;


    /**
     * 签退
     *
     * @return
     */
    @GetMapping("/signBack")
    public ApiResult signBack(@RequestParam("batchNo") String batchNo) {
        log.info("开始签退........................");
        SignInBackDto signInBackDto = new SignInBackDto();
        signInBackDto.setFuncFlag(Constants.SigningInBack.SIGNING_BACK);
        ApiResult result = cashService.signInBack(signInBackDto);
        log.info("当前签退的状态为{},消息{}", result.getCode(), result.getMsg());
        if (result.hasSuccess()) {
            //签退的时候将提现申请中的订单置为审核失败
            orderWithdrawService.operatorReview();
            // 更新清算进度状态
            cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.SIGN_BACK, Constants.CleanFlowPathStatus.COMPLETED);
            return ApiResult.success();
        }
        return ApiResult.error(result.getMsg());
    }


    /**
     * 发起出入金对账
     *
     * @return
     */
    @GetMapping("/accountcheck")
    public ApiResult accountcheck(@RequestParam("batchNo") String batchNo) {
        log.info("开始发起出入金对账");
        ReconciliationMatchingDto reconciliationMatchingDto = new ReconciliationMatchingDto();
        reconciliationMatchingDto.setFuncFlag(Constants.PAB1006.WITHDRAW_DEPOSIT);
        reconciliationMatchingDto.setBeginDateTime(DateUtil.getBeginDay());
        reconciliationMatchingDto.setEndDateTime(DateUtil.getEndDay());
        ApiResult<ReconciliationMatchingVo> apiResult = cashService.reconciliationMatching(reconciliationMatchingDto);
        log.info("发起出入金对账执行结果={}", JSON.toJSONString(apiResult));
        if (apiResult.hasSuccess()) {
            // 更新清算进度状态
            cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.ACCOUNTCHECK, Constants.CleanFlowPathStatus.COMPLETED);
            return ApiResult.success();
        }
        return ApiResult.error(apiResult.getMsg());
    }


    /**
     * 业务返还数据生成
     *
     * @return
     */
    @GetMapping("/amountReturn")
    public ApiResult amountReturn() {
        log.info("开始生成业务办理数据");
        amountReturnBiz.generate();
        log.info("业务办理数据生成完毕");
        return ApiResult.success();
    }


    /**
     * 生成清算数据
     *
     * @return
     */
    @GetMapping("/generateCleanData")
    public ApiResult generateCleanData(@RequestParam("batchNo") String batchNo) {
        cleanBiz.generateCleanData(batchNo);
        return ApiResult.success();
    }

    /**
     * 生成清算文件，发送清算请求至银行
     *
     * @return
     */
    @GetMapping("/launchClean")
    public ApiResult launchClean(@RequestParam("batchNo") String batchNo) {
        cleanBiz.launchClean(batchNo);
        return ApiResult.success();
    }


    /**
     * 查看清算进度
     *
     * @return
     */
    @GetMapping("/cleanProcess")
    public ApiResult cleanProcess(@RequestParam("batchNo") String batchNo) {
        log.info("开始查看清算进度");
        FilePlannedSpeedDto filePlannedSpeedDto = new FilePlannedSpeedDto();
        Date now = new Date();
        filePlannedSpeedDto.setBeginDate(now);
        filePlannedSpeedDto.setEndDate(now);
        filePlannedSpeedDto.setFuncFlag("1");
        ApiResult<FilePlannedSpeedVo> apiResult = cashService.filePlannedSpeed(filePlannedSpeedDto);
        log.info("查看清算进度结果={}", JSON.toJSONString(apiResult));
        if (apiResult.hasSuccess()) {
            return ApiResult.success();
        }
        return ApiResult.error(apiResult.getMsg());
    }
}
