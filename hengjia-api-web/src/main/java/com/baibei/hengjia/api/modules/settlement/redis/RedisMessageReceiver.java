package com.baibei.hengjia.api.modules.settlement.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.match.bean.vo.BaseMatchUsersVo;
import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.match.web.BuyMatchComponent;
import com.baibei.hengjia.api.modules.settlement.bean.dto.BankFileDto;
import com.baibei.hengjia.api.modules.settlement.biz.CleanBiz;
import com.baibei.hengjia.api.modules.settlement.biz.CleanResultBiz;
import com.baibei.hengjia.api.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.api.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 6:19 PM
 * @description: Redis订阅消息接收器
 */
@Component
@Slf4j
public class RedisMessageReceiver {
    @Autowired
    private CleanBiz cleanBiz;
    @Autowired
    private IBankOrderService bankOrderService;
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CleanResultBiz cleanResultBiz;
    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Autowired
    private BuyMatchComponent buyMatchComponent;

    /**
     * 出入金对账
     *
     * @param msg
     */
    public void withdrawDeposit(String msg) {
        log.info("正在执行出入金对账 msg={}", msg);
        Object realMsg = redisUtil.rightPop(RedisConstant.SET_WITHDRAW_DEPOSIT_LIST);
        if (realMsg == null) {
            log.info("节点获取不到队列元素,直接返回");
            return;
        }
        log.info("节点获取到队列元素,realMsg={}", realMsg);
        JSONObject jsonObject = JSONObject.parseObject(realMsg.toString());
        String batchNo = jsonObject.getString("batchNo");
        try {
            ApiResult apiResult = withDrawDepositDiffService.withDrawDepositDiff(batchNo);
            log.info("结束执行出入金对账，执行结果===>{}", apiResult.toString());
        } catch (Exception e) {
            log.error("出入金对账异常",e);
        }
    }

    /**
     * 接收执行买入配货通知
     * @param msg
     */
    public void buyMatch(String msg) {
        log.info("接收到执行买入配货通知 msg={}", msg);
        Object realMsg = redisUtil.rightPop(RedisConstant.MATCH_BUYMATCH_LIST);
        if (realMsg == null) {
            log.info("节点获取不到队列元素,直接返回");
            return;
        }
        log.info("节点获取到队列元素,realMsg={}", realMsg);
        String batchNo = realMsg.toString();
        try {
            ApiResult apiResult = buyMatchComponent.buyMatch(batchNo);
            log.info("结束执行买入配货，执行结果===>{}", apiResult.toString());
        } catch (Exception e) {
            log.error("买入配货异常",e);
        }
    }

   /* *//**
     * 清算数据统计
     *
     * @param msg
     *//*
    public void cleanPre(String msg) {
        log.info("正在执行清算数据统计,msg={}", msg);
        Object realMsg = redisUtil.rightPop(RedisConstant.SET_CLEAN_PRE_LIST);
        if (realMsg == null) {
            log.info("节点获取不到队列元素,直接返回");
            return;
        }
        log.info("节点获取到队列元素,realMsg={}", realMsg);
        try {
            cleanBiz.cleanPre(realMsg.toString());
        } catch (Exception e) {
            log.error("执行清算数据统计异常", e);
        }
    }

    *//**
     * 发送清算请求至银行
     *
     * @param msg
     *//*
    public void sendToBank(String msg) {
        log.info("正在执行发送清算请求至银行,msg={}", msg);
        Object realMsg = redisUtil.rightPop(RedisConstant.SET_CLEAN_LIST);
        if (realMsg == null) {
            log.info("节点获取不到队列元素,直接返回");
            return;
        }
        log.info("节点获取到队列元素,realMsg={}", realMsg);
        try {
            cleanBiz.sendToBank(realMsg.toString());
        } catch (Exception e) {
            log.error("发送清算请求至银行异常", e);
        }
    }
*/
    /**
     * 接收银行通知交易网查看文件通知,realMsg={"fileName":"2019043423.txt","funcFlag":"1"}
     *
     * @param msg
     */
    public void getBankFile(String msg) {
        log.info("接收银行通知文件,msg={}", msg);
        Object realMsg = redisUtil.rightPop(RedisConstant.SET_BANK_FILE_LIST);
        if (realMsg == null) {
            log.info("节点获取不到队列元素,直接返回");
            return;
        }
        log.info("节点获取到队列元素,realMsg={}", realMsg);
        JSONObject jsonObject = JSON.parseObject(realMsg.toString());
        if (jsonObject == null) {
            log.error("接收银行通知交易网查看文件通知消息为空,请检查");
            return;
        }
        String fileName = jsonObject.getString("fileName");
        Integer funcFlag = jsonObject.getInteger("funcFlag");
        try {
            switch (funcFlag) {
                // 清算失败文件
                case 1:
                    cleanResultBiz.batFailResult(fileName);
                    break;
                // 对账记录不平文件
                case 5:
                    cleanResultBiz.batCustDzFail(fileName);
                    break;
                // 出入金流水文件
                case 3:
                    String batchNo = bankOrderService.bankOrder(fileName);
                    Map map = new HashMap();
                    map.put("batchNo", batchNo);
                    String mapStr = JSONObject.toJSONString(map);
                    log.info("发送出入金对账通知,主题为：{}，参数为：{}", RedisConstant.SET_WITHDRAW_DEPOSIT_TOPIC, JSON.parseObject(mapStr));
                    redisUtil.leftPush(RedisConstant.SET_WITHDRAW_DEPOSIT_LIST,mapStr);
                    redisUtil.pub(RedisConstant.SET_WITHDRAW_DEPOSIT_TOPIC, RedisConstant.SET_WITHDRAW_DEPOSIT_TOPIC);
                    break;
                default:
                    log.info("funcFlag error");
            }
        } catch (Exception e) {
            log.error("接收银行通知处理文件异常", e);
        }
    }
}
