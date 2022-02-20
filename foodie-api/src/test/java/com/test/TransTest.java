package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.seiei.Application;
import top.seiei.service.impl.TestTransServiceImpl;

import javax.annotation.Resource;

// 不注释的话，每次 maven install 都会跑一遍 Test
// 声明使用 Spring
//@RunWith(SpringRunner.class)
// 引入 SpringBoot 启动类
//@SpringBootTest(classes = Application.class)
public class TransTest {

    @Resource
    private TestTransServiceImpl testTransService;

    // 运行 Test 入口
    //@Test
    public void testForTransactional() {
        testTransService.testForTransactional();
    }

}
