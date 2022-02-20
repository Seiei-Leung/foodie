package top.seiei.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.bo.UserBO;
import top.seiei.service.UsersService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Resource
    private UsersService usersService;

    /**
     * 检查用户名是否重复
     * @param userName  用户名
     * @return
     */
    @GetMapping("/userNameIsExist")
    public ServerResponse userNameIsExist(@RequestParam String userName) {
        if (StringUtils.isBlank(userName)) {
            return ServerResponse.createdByError("用户名不能为空！");
        }
        Boolean result = usersService.queryUserNameIsExist(userName);
        if (result) {
            return ServerResponse.createdByError("用户名重复！！");
        }
        return ServerResponse.createdBySuccess();
    }

    /**
     * 注册用户
     * @param userBO 前端传入的 BO 对象
     * @return
     */
    @PostMapping("/regist")
    public ServerResponse regist(@RequestBody UserBO userBO) {
        String userName = userBO.getUserName();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        // 检查参数是否为空
        if (StringUtils.isBlank(userName) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return ServerResponse.createdByError("用户名和密码不能为空");
        }

        // 用户名是否已存在
        if (usersService.queryUserNameIsExist(userName)) {
            return ServerResponse.createdByError("用户名已存在");
        }

        // 密码不能少于4位
        if (password.length() < 4) {
            return ServerResponse.createdByError("密码不能少于4位");
        }

        // 密码需要一致
        if (!password.equals(confirmPassword)) {
            return ServerResponse.createdByError("两次密码不一致");
        }

        // 执行存储逻辑
        try {
            usersService.createUser(userBO);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createdByError("保存信息时发生错误");
        }

        return ServerResponse.createdBySuccess();
    }



}
