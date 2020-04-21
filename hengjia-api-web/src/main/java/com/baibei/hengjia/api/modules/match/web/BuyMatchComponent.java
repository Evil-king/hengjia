package com.baibei.hengjia.api.modules.match.web;

import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.match.bean.vo.BaseMatchUsersVo;
import com.baibei.hengjia.api.modules.match.dao.BuymatchUserMapper;
import com.baibei.hengjia.api.modules.match.model.BuymatchUser;
import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.match.service.IBuymatchUserService;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.api.modules.match.service.IMatchFailLogService;
import com.baibei.hengjia.api.modules.product.service.IProductStockService;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BuyMatchComponent
 * @Description 解决配货大事物问题
 * @Date 2019/9/6 10:24
 * @Created by Longer
 */
@Component
@Slf4j
public class BuyMatchComponent {

    @Autowired
    private IBuymatchUserService buymatchUserService;
    @Autowired
    private IMatchFailLogService matchFailLogService;
    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Value("${buymatch.period.num}")
    private Integer periodNum;//每次只给买入配货多少个用户
    @Value("${buymatch.offset.flag}")
    private String offsetFlag;//是否执行补货逻辑。offset=执行；unoffset=不执行


    public ApiResult buyMatch(String batchNo){
        log.info("开始批量执行批货逻辑");
        long start = System.currentTimeMillis();
        List<BaseMatchUsersVo> baseMatchUsersVoList = getAllUsers(batchNo);
        for (BaseMatchUsersVo baseMatchUsersVo : baseMatchUsersVoList) {
            try{
                ApiResult apiResult = buymatchLogService.buyMatch(batchNo, baseMatchUsersVo);
                if(apiResult.getCode()!=200){
                    log.info(apiResult.getMsg());
                }
            }catch (Exception e){
                log.error("配货执行报错：",e);
                e.printStackTrace();
            }
        }
        log.info("batch buyMatch time comsuming " + (System.currentTimeMillis() - start) + " ms");
        return ApiResult.success();
    }

    /**
     * 获取所有配货用户集合
     * @param batchNo
     * @return
     */
    private List<BaseMatchUsersVo> getAllUsers(String batchNo) {
        List<BaseMatchUsersVo> baseMatchUsersVoList = new ArrayList<>();
        //获取以前配货失败用户集合
        if(Constants.OffsetFlag.OFFSET.equals(offsetFlag)){
            List<MatchFailLog> matchFailLogVoList = matchFailLogService.getCurrentDayFailLogs(Constants.MatchFailLogStatus.WAIT);
            for (MatchFailLog matchFailLog  : matchFailLogVoList) {
                if(baseMatchUsersVoList.size()<periodNum){
                    BaseMatchUsersVo baseMatchUsersVo = new BaseMatchUsersVo();
                    baseMatchUsersVo.setDealNo(matchFailLog.getDealNo());
                    baseMatchUsersVo.setCustomerNo(matchFailLog.getCustomerNo());
                    baseMatchUsersVo.setProductTradeNo(matchFailLog.getProductTradeNo());
                    baseMatchUsersVo.setMatchNum(matchFailLog.getMatchNum());
                    baseMatchUsersVo.setFailCount(matchFailLog.getFailCount());
                    baseMatchUsersVo.setOperateType(Constants.BuyMatchOperateType.PLAN);
                    baseMatchUsersVo.setCreateTime(matchFailLog.getCreateTime());
                    baseMatchUsersVoList.add(baseMatchUsersVo);
                }
            }
        }
        //获取本批次下的用户集合
        List<BuymatchUser> buymatchUserList = buymatchUserService.getByBatchNo(batchNo,Constants.BuyMatchUserStatus.UNRUN);
        //合并集合
        for (BuymatchUser buymatchUser : buymatchUserList) {
            if(baseMatchUsersVoList.size()<periodNum) {
                BaseMatchUsersVo baseMatchUsersVo = new BaseMatchUsersVo();
                baseMatchUsersVo.setDealNo(buymatchUser.getDealNo());
                baseMatchUsersVo.setCustomerNo(buymatchUser.getCustomerNo());
                baseMatchUsersVo.setProductTradeNo(buymatchUser.getProductTradeNo());
                baseMatchUsersVo.setMatchNum(buymatchUser.getMatchNum());
                baseMatchUsersVo.setFailCount(0);
                baseMatchUsersVo.setOperateType(buymatchUser.getOperateType());
                baseMatchUsersVo.setCreateTime(buymatchUser.getCreateTime());
                baseMatchUsersVoList.add(baseMatchUsersVo);
            }
        }
        return baseMatchUsersVoList;
    }

}
