package top.seiei.controller;


import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.ShopCartBO;
import top.seiei.pojo.bo.UserBO;
import top.seiei.pojo.vo.UserVO;
import top.seiei.service.UsersService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 使用 @ApiIgnore 注释，可以使 Swagger2 生产文档时忽略该 controller
//@ApiIgnore
@Api(value = "注册登录模块", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController{

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Resource
    private UsersService usersService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 检查用户名是否重复
     * @param userName  用户名
     * @return
     */
    @ApiOperation(value = "用户名是否存在", notes = "参数不能为空", httpMethod = "GET")
    @GetMapping("/userNameIsExist")
    public ServerResponse userNameIsExist(
            @ApiParam(name = "userName", value = "用户名", example = "Seiei", required = true)
            @RequestParam String userName) {
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
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);

            // 同步购物车
            sycnShopCart(request, response, user.getId());

            // 生产用户token，存入 redis 会话
            String token = UUID.randomUUID().toString();
            redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), token);
            userVO.setUserUniqueToken(token);

            // 配置Cookie
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

            return ServerResponse.createdBySuccess(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createdByError("保存信息时发生错误");
        }
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
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(result, userVO);

            // 生成用户token，存入 redis 会话
            String token = UUID.randomUUID().toString();
            redisOperator.set(REDIS_USER_TOKEN + ":" + result.getId(), token);
            userVO.setUserUniqueToken(token);

            // 配置Cookie
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

            // 同步购物车
            sycnShopCart(request, response, result.getId());
            return ServerResponse.createdBySuccess(userVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createdByError("M5加密出错!!!");
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
        CookieUtils.deleteCookie(request, response, "shopcart");

        // 清空 Redis token
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        return ServerResponse.createdBySuccess();
    }

    /**
     * 同步 Cookie 和 Redis 的购物车信息
     * @param request request 对象
     * @param response response 对象
     * @param userId 用户 Id
     */
    private void sycnShopCart(HttpServletRequest request, HttpServletResponse response,  String userId) {
        List<ShopCartBO> shopCartBOListOfCookie = new ArrayList<>();
        List<ShopCartBO> shopCartBOListOfRedis = new ArrayList<>();
        String strOfCookie  = CookieUtils.getCookieValue(request, "shopcart", true);
        String strOfRedis = redisOperator.get("shopCart:" + userId);

        if (StringUtils.isNotBlank(strOfRedis)) {
            shopCartBOListOfRedis = JsonUtils.jsonToList(strOfRedis, ShopCartBO.class);
        }
        if (StringUtils.isNotBlank(strOfCookie)) {
            shopCartBOListOfCookie = JsonUtils.jsonToList(strOfCookie, ShopCartBO.class);
        }

        if (shopCartBOListOfRedis.size() != 0) {
            // 如果 redis 有数据，cookie没有数据，则将 redis 的数据同步到 cookie 中
            if (shopCartBOListOfCookie.size() == 0) {
                CookieUtils.setCookie(request, response, "shopcart", strOfRedis, true);
            }
            // 如果 redis 有数据，cookie有数据，则将 cookie 的数据添加到 redis 中
            // 商品数量取 cookie 的值
            else {

                for (ShopCartBO itemOfCookie : shopCartBOListOfCookie) {
                    String specIdOfCookie = itemOfCookie.getSpecId();
                    Boolean isExist = false; // 在 redis 购物车是否存在该商品信息
                    for (ShopCartBO itemOfRedis : shopCartBOListOfRedis) {
                        String specIdOfRedis = itemOfRedis.getSpecId();
                        if (specIdOfCookie.equals(specIdOfRedis)) {
                            itemOfRedis.setBuyCounts(itemOfCookie.getBuyCounts());
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        shopCartBOListOfRedis.add(itemOfCookie);
                    }
                }
                redisOperator.set("shopCart:" + userId, JsonUtils.objectToJson(shopCartBOListOfRedis));
            }
        } else {
            // 如果 redis 没有数据，cookie 有数据，则将 cookie 的数据同步到 redis 中
            if (shopCartBOListOfCookie.size() != 0) {
                redisOperator.set("shopCart:" + userId, strOfCookie);
            }
        }
    }
}
