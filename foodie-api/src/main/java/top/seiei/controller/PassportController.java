package top.seiei.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.UserBO;
import top.seiei.service.UsersService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;

// 使用 @ApiIgnore 注释，可以使 Swagger2 生产文档时忽略该 controller
//@ApiIgnore
@Api(value = "注册登录模块", tags = {"用于注册登录的相关接口"})
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
    @ApiOperation(value = "用户名是否存在", notes = "参数不能为空", httpMethod = "GET")
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
    @ApiOperation(value = "注册用户", notes = "参数不能为空", httpMethod = "POST")
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

    /**
     * 用户登录接口
     * @param userBO 前端传入的 BO 对象
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "参数不能为空", httpMethod = "POST")
    @PostMapping("/login")
    public ServerResponse login(@RequestBody UserBO userBO) {
        String userName = userBO.getUserName();
        String password = userBO.getPassword();

        // 检查参数是否为空
        if (StringUtils.isBlank(userName) ||
                StringUtils.isBlank(password)) {
            return ServerResponse.createdByError("用户名和密码不能为空");
        }

        // 执行存储逻辑
        try {
            Users result = usersService.queryUserForLogin(userName, password);
            if (result == null) {
                return ServerResponse.createdByError("用户名和密码不匹配！");
            }
            // 脱敏逻辑
            result.setPassword(null);
            return ServerResponse.createdBySuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createdByError("使用MD5检验时发生错误");
        }
    }

}
