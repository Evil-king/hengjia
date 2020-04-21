package com.baibei.hengjia.api.modules.settlement.redis;

import com.baibei.hengjia.common.tool.constants.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 6:16 PM
 * @description: Redis订阅消息监听
 */
@Configuration
public class RedisMessageListener {
    @Autowired
    private RedisMessageReceiver redisMessageReceiver;

    /**
     * Redis订阅
     *
     * @param connectionFactory
     * @param withdrawDepositListenerAdapter
     * @param
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter withdrawDepositListenerAdapter,
                                             MessageListenerAdapter bankFileListenerAdapter,MessageListenerAdapter buyMatchListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听开始执行出入金对账通知
        container.addMessageListener(withdrawDepositListenerAdapter, new PatternTopic(RedisConstant.SET_WITHDRAW_DEPOSIT_TOPIC));
        /*//监听开始执行清算数据统计通知
        container.addMessageListener(cleanPreListenerAdapter, new PatternTopic(RedisConstant.SET_CLEAN_PRE_TOPIC));
        //监听开始执行清算发送银行通知
        container.addMessageListener(cleanListenerAdapter, new PatternTopic(RedisConstant.SET_CLEAN_TOPIC));*/
        // 监听接收银行文件通知
        container.addMessageListener(bankFileListenerAdapter, new PatternTopic(RedisConstant.SET_BANK_FILE));

        //监听开始执行买入配货通知
        container.addMessageListener(buyMatchListenerAdapter, new PatternTopic(RedisConstant.MATCH_BUYMATCH_TOPIC));
        return container;
    }

    @Bean
    public MessageListenerAdapter withdrawDepositListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "withdrawDeposit");
    }

  /*  @Bean
    public MessageListenerAdapter cleanPreListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "cleanPre");
    }

    @Bean
    public MessageListenerAdapter cleanListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "sendToBank");
    }
*/

    @Bean
    public MessageListenerAdapter bankFileListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "getBankFile");
    }
    @Bean
    public MessageListenerAdapter buyMatchListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "buyMatch");
    }
}
