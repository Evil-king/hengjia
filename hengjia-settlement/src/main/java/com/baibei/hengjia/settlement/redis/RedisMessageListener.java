package com.baibei.hengjia.settlement.redis;

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
     * @param cleanListenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter withdrawDepositListenerAdapter,
                                            MessageListenerAdapter cleanListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听开始执行出入金对账通知
        container.addMessageListener(withdrawDepositListenerAdapter, new PatternTopic(RedisConstant.SET_WITHDRAW_DEPOSIT_TOPIC));
        //监听开始执行清算通知
        container.addMessageListener(cleanListenerAdapter, new PatternTopic(RedisConstant.SET_CLEAN_TOPIC));
        return container;
    }

    @Bean
    public MessageListenerAdapter withdrawDepositListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "withdrawDeposit");
    }

    @Bean
    public MessageListenerAdapter cleanListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "clean");
    }
}
