package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.SigningRecordVo;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanDataService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 出金服务
 */
@RestController
@RequestMapping("/auth/api/OrderWithdraw")
public class AuthApiOrderWithdrawController {

    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private ICleanDataService cleanDataService;
//    @Autowired
//    private WeiPayProcess weiPayProcess;
//    @Autowired
//    private AliPayProcess aliPayProcess;

    /**
     * 出金申请
     *
     * @return
     */
    @PostMapping("/withdrawApplication")
    public ApiResult<Boolean> withdrawApplication(@Valid @RequestBody OrderWithdrawDto orderWithdrawDto) {
        return orderWithdrawService.withdrawApplicationApplication(orderWithdrawDto);
    }

    /**
     * 平安用户信息
     *
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/withdrawInfo")
    public ApiResult getWithdrawInfo(@RequestBody CustomerBaseDto customerBaseDto) {
        ApiResult apiResult = new ApiResult();
        SigningRecordVo recordVo = new SigningRecordVo();
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerBaseDto.getCustomerNo());
        if (signingRecord == null) {
            apiResult.setData(recordVo);
            return apiResult;
        }
        recordVo.setBankName(signingRecord.getBankName());
        recordVo.setAcctName(signingRecord.getAcctName());
        recordVo.setRealtedAcctId(signingRecord.getRelatedAcctId());
        apiResult.setData(recordVo);
        return apiResult;
    }

    /**
     * 用户是否第一次签约
     *
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/isSingingFirst")
    public ApiResult isSingingFirst(@RequestBody CustomerBaseDto customerBaseDto) {
        ApiResult apiResult = new ApiResult();
        if (signingRecordService.isTodaySigning(customerBaseDto.getCustomerNo()) == true &&
                cleanDataService.isCleanSuccess(customerBaseDto.getCustomerNo()) == true) {
            apiResult.setData(true);
            return apiResult;
        }
        return ApiResult.success(false);
    }


//    /**
//     * 微信支付
//     * @param weiPayDto
//     * @return
//     */
//    @PostMapping("/weiH5Pay")
//    public ApiResult weiH5Pay(@Validated @RequestBody WeiPayDto weiPayDto){
//        weiPayDto.setTempleStr(weiPayDto.getIp());
//        String result = new Gson().toJson(weiPayDto);
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        jsonObject.put("channel", "001");
//        return weiPayProcess.doPosscess(jsonObject);
//    }
//
//    /**
//     * 支付宝
//     * @param aliPayDto
//     * @return
//     */
//    @PostMapping("/aliPay")
//    public ApiResult aliPay(@Validated @RequestBody AliPayDto aliPayDto){
//        String result = new Gson().toJson(aliPayDto);
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        jsonObject.put("channel", "002");
//        return aliPayProcess.doPosscess(jsonObject);
//    }
}
