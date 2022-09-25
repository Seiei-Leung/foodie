package com.test;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.seiei.Application;

import javax.annotation.Resource;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class FastDFStest {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    //@Test
    public void deleteFile() {
        fastFileStorageClient.deleteFile("seiei", "M00/00/00/wKjcgGJszLuAAHrgAAB8zpjB7-M174.jpg");
    }
}
