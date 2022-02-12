package top.seiei.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.seiei.pojo.Users;
import top.seiei.service.impl.UsersServiceImpl;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private UsersServiceImpl usersServiceImpl;

    @GetMapping("/hello")
    @ResponseBody
    public Object hello() {
        return "Hello,world";
    }

    @GetMapping("/getUser")
    @ResponseBody
    public Users getUser(String id) {
        return usersServiceImpl.getUser(id);
    }
}
