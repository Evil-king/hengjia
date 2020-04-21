package com.baibei.hengjia.api.modules.match.web;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.api.modules.match.model.OffsetDeliveryticket;
import com.baibei.hengjia.api.modules.match.service.IBuymatchUserService;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.api.modules.match.service.IMatchFailLogService;
import com.baibei.hengjia.api.modules.match.service.IOffsetDeliveryticketService;
import com.baibei.hengjia.api.modules.settlement.service.ICustDzFailService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryTransferDto;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.api.modules.trade.service.IMatchConfigService;
import com.baibei.hengjia.api.modules.trade.service.IMatchLogService;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname ApiBuyMatchController
 * @Description 买入配货
 * @Date 2019/8/6 14:43
 * @Created by Longer
 */
@Slf4j
@RestController
@RequestMapping("/api/buyMatch")
public class ApiBuyMatchController {
    @Autowired
    private IBuymatchUserService buymatchUserService;
    @Autowired
    private IMatchConfigService matchConfigService;
    @Autowired
    private IMatchAuthorityService matchAuthorityService;
    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private ICouponAccountService couponAccountService;
    @Autowired
    private IOffsetDeliveryticketService offsetDeliveryticketService;
    @Autowired
    private IMatchFailLogService matchFailLogService;


    /**
     * 买入配货
     * @return
     */
    @PostMapping("/buyMatchs")
    public ApiResult buyMatchs(){
        log.info("开始执行买入配货用户入库逻辑...");
        ApiResult apiResult;
        try {
            buymatchUserService.buyMatchUsers();
            apiResult = ApiResult.success();
        } catch (ServiceException s){
            log.info("买入配货业务异常，关闭开关...");
            /*matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.BUYMATCH);*/
            apiResult = ApiResult.error(s.getMessage());
        }
        catch (Exception e) {
            log.info("买入配货系统异常，关闭开关...");
            matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.BUYMATCH);
            e.printStackTrace();
            apiResult = ApiResult.error(e.getMessage());
        }
        log.info("结束执行买入配货用户入库逻辑...apiResult={}",JSON.toJSONString(apiResult));
        return apiResult;
    }

    /**
     * 系统自动提货。
     * 规则：配货权为0的用户，系统给予自动提货，并且刷新配货权
     * @return
     */
    @PostMapping("/sysDelivery")
    public ApiResult sysDelivery(){
        long start = System.currentTimeMillis();
        //查询出配货权为0的用户集合
        List<String> customerListWithAuthorityZero = matchAuthorityService.getCustomerListWithAuthorityZero();
        log.info("开始遍历执行系统提货逻辑...");
        for (String customerNo : customerListWithAuthorityZero) {
            CustomerAddress customerAddress = customerAddressService.findDefaultByNo(customerNo);
            if (!StringUtils.isEmpty(customerAddress)) {
                DeliveryApplyDto deliveryApplyDto = new DeliveryApplyDto();
                deliveryApplyDto.setCustomerNo(customerNo);
                deliveryApplyDto.setValidateTradeDayFlag("1");
                deliveryApplyDto.setDeliveryCount(1);
                deliveryApplyDto.setHoldType(Constants.HoldType.MAIN);
                deliveryApplyDto.setProductTradeNo("0002");
                deliveryApplyDto.setAddressId(customerAddress.getId());
                deliveryApplyDto.setSource(Constants.DeliverySource.SYS);
                ApiResult apiResult = deliveryService.deliveryApply(deliveryApplyDto);
                if(apiResult.getCode()!=200){
                    log.info("系统提货失败，用户：{}，原因：{}",customerNo,apiResult.getMsg());
                }
            }else{
                log.info("系统提货失败，用户："+customerNo+"，失败原因：未找到用户提货地址");
            }
        }
        log.info("sysDelivery time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success();
    }


    /**
     * 系统自动提货。
     * 规则：用户提货，扣挂牌商货物，生成自己的提货单
     * @return
     */
    @PostMapping("/deliveryTransfer")
    public ApiResult deliveryTransfer(){
        long start = System.currentTimeMillis();
        //获取符合条件的用户集合
        ApiResult<List<CouponAccount>> couponAccountListResult = couponAccountService.findByBalance(new BigDecimal(1160),
                null, "0002", Constants.CouponType.DELIVERYTICKET);
        List<CouponAccount> couponAccountList = couponAccountListResult.getData();
        for (CouponAccount couponAccount : couponAccountList) {
            try{
                DeliveryTransferDto deliveryTransferDto = new DeliveryTransferDto();
                deliveryTransferDto.setCustomerNo(couponAccount.getCustomerNo());
                deliveryTransferDto.setProductTradeNo(couponAccount.getProductTradeNo());
                ApiResult apiResult = deliveryService.deliveryTransfer(deliveryTransferDto);
                if (apiResult.getCode()!=200) {
                    log.info("自动提货失败，"+apiResult.getMsg());
                }
            }catch (Exception e){
                log.error("自动提货异常",e);
                e.printStackTrace();
            }
        }
        log.info("deliveryTransfer time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success();
    }


    /**
     * 提货券补扣。
     * @return
     */
    @PostMapping("/offsetDeliveryTicket")
    public ApiResult offsetDeliveryTicket(){
        long start = System.currentTimeMillis();
        List<OffsetDeliveryticket> offsetDeliveryticketList = offsetDeliveryticketService.getInfosWithAuthority();
        for (OffsetDeliveryticket offsetDeliveryticket : offsetDeliveryticketList) {
            try{
                ApiResult result = offsetDeliveryticketService.offset(offsetDeliveryticket.getCustomerNo(), offsetDeliveryticket.getProductTradeNo(),Constants.ValidTradeFlag.VALID);
                if(result.getCode()!=200){
                    log.info("提货券补扣失败，原因："+result.getMsg());
                }
            }catch (Exception e){
                log.error("提货券补扣异常，原因：",e);
                e.printStackTrace();
            }
        }
        log.info("offsetDeliveryTicket time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success();
    }

    /**
     * 提货券补扣。
     * @return
     */
    @PostMapping("/firstOffsetDeliveryTicket")
    public ApiResult firstOffsetDeliveryTicket(){
        long start = System.currentTimeMillis();
        List<OffsetDeliveryticket> offsetDeliveryticketList = offsetDeliveryticketService.getInfosWithAuthority();
        for (OffsetDeliveryticket offsetDeliveryticket : offsetDeliveryticketList) {
            try{
                ApiResult result = offsetDeliveryticketService.offset(offsetDeliveryticket.getCustomerNo(), offsetDeliveryticket.getProductTradeNo(),Constants.ValidTradeFlag.UNVALID);
                if(result.getCode()!=200){
                    log.info("提货券补扣失败，原因："+result.getMsg());
                }
            }catch (Exception e){
                log.error("提货券补扣异常，原因：",e);
                e.printStackTrace();
            }
        }
        log.info("offsetDeliveryTicket time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success();
    }

    /**
     * 初始化提货券补扣名单
     * @return
     */
    @PostMapping("/initOffsetData")
    public ApiResult initOffsetData(){
        offsetDeliveryticketService.initOffsetData();
        return ApiResult.success();
    }

    /**
     * 将wait状态的失败单，置为失效状态
     * @return
     */
    @PostMapping("/destoryFailLog")
    public ApiResult destoryFailLog(){
        matchFailLogService.destroyFailLog(Constants.MatchFailLogStatus.WAIT);
        return ApiResult.success();
    }

}
