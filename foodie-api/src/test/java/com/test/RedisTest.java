package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.seiei.Application;

// 不注释的话，每次 maven install 都会跑一遍 Test
// 声明使用 Spring
// @RunWith(SpringRunner.class)
// 引入 SpringBoot 启动类
// @SpringBootTest(classes = Application.class)
public class RedisTest {

    final static Logger logger = LoggerFactory.getLogger(RedisTest.class);

    /**
     * 操作 Redis 的接口
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // @Test
    public void get() {
        // opsForValue() 用于操作 Redis 的 String 类型
        if (stringRedisTemplate.opsForValue().get("name") != null) {
            logger.info(stringRedisTemplate.opsForValue().get("name").toString());
        }
    }

    // @Test
    public void set() {
        // opsForValue() 用于操作 Redis 的 String 类型
        stringRedisTemplate.opsForValue().set("name", "kkk");
    }

    // @Test
    public void del() {
        // opsForValue() 用于操作 Redis 的 String 类型
        stringRedisTemplate.delete("name");
    }
}
