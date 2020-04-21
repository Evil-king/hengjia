package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.SignInBackDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.SignInBackVo;
import com.baibei.hengjia.api.modules.cash.service.ICashService;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.settlement.async.PABAsyncTask;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/cash")
public class ApiSigningInBackController {

    @Autowired
    private ICashService cashService;

    @Autowired
    private PABAsyncTask pabAsyncTask;

    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 签到
     *
     * @param signInBackDto
     * @return
     */
    @GetMapping("/signIn")
    public ApiResult<SignInBackVo> signIn(SignInBackDto signInBackDto) {
        logger.info("开始签到.......................");
        signInBackDto.setFuncFlag(Constants.SigningInBack.SIGNING_IN);
        ApiResult<SignInBackVo> result = cashService.signInBack(signInBackDto);
        if (result.hasSuccess()) {
            // 更新清算进度状态
            cleanFlowPathService.findAndUpdate(DateUtil.yyyyMMddNoLine.get().format(new Date()), Constants.CleanFlowPathCode.SIGN_IN, Constants.CleanFlowPathStatus.COMPLETED);
        }
        return result;
    }

    /**
     * 签退
     *
     * @param signInBackDto
     * @return
     */
    @GetMapping("/signBack")
    public ApiResult<SignInBackVo> signBack(SignInBackDto signInBackDto) {
        logger.info("开始签退........................");
        signInBackDto.setFuncFlag(Constants.SigningInBack.SIGNING_BACK);
        ApiResult result = cashService.signInBack(signInBackDto);
        logger.info("当前签退的状态为{},消息{}", result.getCode(), result.getMsg());
        if (ResultEnum.SUCCESS.getCode() == result.getCode()) {
            pabAsyncTask.withDrawDeposit(cashService);
            //签退的时候将提现申请中的订单置为审核失败
            orderWithdrawService.operatorReview();
        }
        return result;
    }
}
