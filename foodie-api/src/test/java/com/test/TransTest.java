package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.seiei.Application;
import top.seiei.service.impl.TestTransServiceImpl;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
// 引入 SpringBoot 启动类
@SpringBootTest(classes = Application.class)
public class TransTest {

    @Resource
    private TestTransServiceImpl testTransService;

    @Test
    public void testForTransactional() {
        testTransService.testForTransactional();
    }

}
