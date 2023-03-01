package com.salon.ht.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static RedisUtils redisUtils;

    /**
     * initialization
     */
    @PostConstruct
    public void init() {
        redisUtils = this;
        redisUtils.redisTemplate = this.redisTemplate;
    }

    /**
     * Query key, support fuzzy query
     */
    public static Set<String> keys(String key) {
        return redisUtils.redisTemplate.keys(key);
    }

    /**
     * Get value
     */
    public static Object get(String key) {
        return redisUtils.redisTemplate.opsForValue().get(key);
    }

    /**
     * Setting value
     */
    public static void set(String key, Object value) {
        redisUtils.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Set the value and set the expiration time
     *
     * @param expire Expiration time in seconds
     */
    public static void set(String key, String value, Long expire) {
        redisUtils.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * Delete key
     */
    public static void delete(String key) {
        redisUtils.redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * Setting objects
     *
     * @param key     key
     * @param hashKey hashKey
     * @param object  object
     */
    public static void hset(String key, String hashKey, Object object) {
        redisUtils.redisTemplate.opsForHash().put(key, hashKey, object);
    }

    /**
     * Setting objects
     *
     * @param key     key
     * @param hashKey hashKey
     * @param object  object
     * @param expire  Expiration time in seconds
     */
    public static void hset(String key, String hashKey, Object object, Integer expire) {
        redisUtils.redisTemplate.opsForHash().put(key, hashKey, object);
        redisUtils.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * Set up HashMap
     *
     * @param key key
     * @param map map value
     */
    public static void hset(String key, HashMap<String, Object> map) {
        redisUtils.redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * key Set value when it does not exist
     *
     * @param key     key
     * @param hashKey hashKey
     * @param object  value
     */
    public static void hsetAbsent(String key, String hashKey, Object object) {
        redisUtils.redisTemplate.opsForHash().putIfAbsent(key, hashKey, object);
    }


    /**
     * Get Hash value
     *
     * @param key     key
     * @param hashKey hashkey
     * @return Object
     */
    public static Object hget(String key, String hashKey) {
        return redisUtils.redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * Get all the values of the key
     *
     * @param key key for get
     * @return Object
     */
    public static Object hget(String key) {
        return redisUtils.redisTemplate.opsForHash().entries(key);
    }

    /**
     * Delete all values of key
     *
     * @param key key
     */
    public static void deleteKey(String key) {
        redisUtils.redisTemplate.opsForHash().getOperations().delete(key);
    }

    /**
     * Judge whether there is a value under the key
     *
     * @param key key
     */
    public static Boolean hasKey(String key) {
        return redisUtils.redisTemplate.opsForHash().getOperations().hasKey(key);
    }

    /**
     * Judge whether there is a value under key and hasKey
     *
     * @param key key
     * @param hasKey key
     */
    public static Boolean hasKey(String key, String hasKey) {
        return redisUtils.redisTemplate.opsForHash().hasKey(key, hasKey);
    }
}
