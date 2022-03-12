package top.seiei.controller;


import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.UserBO;
import top.seiei.service.UsersService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 使用 @ApiIgnore 注释，可以使 Swagger2 生产文档时忽略该 controller
//@ApiIgnore
@Api(value = "注册登录模块", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Resource
    private UsersService usersService;

    /**
     * 检查用户名是否重复
     * @param userName  用户名
     * @return
     */
    @ApiOperation(value = "用户名是否存在", notes = "参数不能为空", httpMethod = "GET")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "用户名", name = "userName", example = "Seiei", required = true)
    )
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
    public ServerResponse regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
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
            Users user = usersService.createUser(userBO);
            // 脱敏逻辑
            user = setNullProperty(user);
            // 配置Cookie
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
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
    public ServerResponse login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
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
            result = setNullProperty(result);

            // 配置Cookie
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);

            return ServerResponse.createdBySuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createdByError("使用MD5检验时发生错误");
        }
    }

    /**
     * 用户登出
     * @param request request 对象
     * @param response response 对象
     * @param userId 用户 ID
     * @return
     */
    @ApiOperation(value = "用户退出登录", notes = "参数不能为空", httpMethod = "POST")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "用户ID", name = "userId", required = true)
    )
    @PostMapping("/logout")
    // @RequestParam 是在请求url中取值，使用 Post 方法也可以在请求url中设置对应的数值
    public ServerResponse logout(HttpServletRequest request, HttpServletResponse response,@RequestParam String userId) {
        // 清空Cookies信息
        CookieUtils.deleteCookie(request, response, "user");

        return ServerResponse.createdBySuccess();
    }

    /**
     * 脱敏逻辑
     * @param users 用户
     * @return
     */
    private Users setNullProperty(Users users)
    {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setBirthday(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        return users;
    }

}
