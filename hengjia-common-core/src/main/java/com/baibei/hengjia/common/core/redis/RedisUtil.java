package com.baibei.hengjia.common.core.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/23 3:08 PM
 * @description: Redis基本操作封装
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * String的set操作,不设置过期时间
     *
     * @param key
     * @param value
     */
    public void set(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * String的set操作,不设置过期时间
     * @param key
     * @param value
     */
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * String的set操作,指定过期时间,单位:秒
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void set(final String key, final String value, int seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }


    /**
     * key在指定时间过期
     *
     * @param key
     * @param date
     */
    public void expireAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * String的get操作
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        return obj == null ? null : obj.toString();
    }


    /**
     * hash的putAll
     *
     * @param key
     * @param map
     */
    public void hsetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * hash 删除
     *
     * @param key
     * @param fieldList
     */
    public void hdelete(String key, Set<String> fieldList) {
        redisTemplate.opsForHash().delete(key, fieldList.toArray());
    }

    /**
     * 普通删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * hash的get
     *
     * @param key
     * @return map集合
     */
    public Map<String, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash的HINCRBY key field increment
     *
     * @param key
     * @param field
     * @param value
     */
    public void hincrBy(String key, String field, long value) {
        redisTemplate.opsForHash().increment(key, field, value);
    }

    /**
     * hmget
     *
     * @param key
     * @param field
     * @return
     */
    public Object hmget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * sorted set的zadd
     *
     * @param key
     * @param member
     * @param score
     */
    public void zdd(String key, String member, double score) {
        redisTemplate.opsForZSet().add(key, member, score);
    }

    /**
     * sorted set的zrem
     *
     * @param key
     * @param member
     */
    public void zremove(String key, String member) {
        redisTemplate.opsForZSet().remove(key, member);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * 过期key,单位:秒
     *
     * @param key
     * @param timeout
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取Redis系列化
     *
     * @return
     */
    public RedisSerializer<?> getValueSerializer() {
        return redisTemplate.getValueSerializer();
    }

    /**
     * Redis发布消息
     *
     * @param channel
     * @param msg
     */
    public void pub(String channel, String msg) {
        redisTemplate.convertAndSend(channel, msg);
    }

    /**
     * 将元素推进队列
     *
     * @param key
     * @param value
     */
    public void leftPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从队列拿元素
     *
     * @param key
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 以增量的方式将double值存储在变量中。
     * @param key
     * @param value 增加的值
     */
    public void incr(String key,int value) {
        redisTemplate.opsForValue().increment(key,value);
    }
}
