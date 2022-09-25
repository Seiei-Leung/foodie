package top.seiei.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.Users;
import top.seiei.pojo.vo.UserVO;
import top.seiei.service.UsersService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 单点登录 SSO（Single Sign On）
 * 相同顶级域名之间的单点登录很简单，因为它们可以共用 Cookie
 * 而不同顶级域名之间的单点登录则需要一个第三方作为协调，即需要一个第三方的独立登录系统作为管理
 * 这个独立登录系统就称为 CAS（Central Authentication Service）中央认证服务
 */
@Controller
public class SSOController {

    public static final String REDIS_USER_TOKEN = "redis_user_token"; // 用户的全局会话，用于保存用户信息
    public static final String REDIS_USER_TICKET = "redis_user_ticket"; // 用户的全局门票
    public static final String REDIS_TMP_TICKET = "redis_tmp_ticket"; // 用户的临时票据
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket"; // Cookie 存储全局门票的键名

    @Autowired
    private RedisOperator redisOperator;

    @Resource
    private UsersService usersService;

    /**
     * 获取登陆界面
     * 具体逻辑：
     * 1、检查当前是否存在登录过而保存下的门票 Cookie
     * 2、如果不存在 Cookie，则等待用户输入登录信息进行登录，登录之后，
     * 创建全局门票并保存到当前登录界面的 Cookie 中和 Redis 中，
     * 构建临时票据到 Redis 中，同时作为参数添加到回调地址，
     * 进入到回调地址，回调界面的前端会根据获取到的票据向后端发送一次票据检查
     * 如果票据与 Redis 的票据一致，则登录成功
     *
     * 3、如果存在 Cookie，则获取 Cookie 中的门票信息并向后端发送一次检查，
     * 与 Redis 存储的信息检查一致后，则认为用户无需再进行登录操作
     * 此时构建一个临时票据存储到 Redis 中，同时作为参数添加到回调地址中
     * 进入到回调地址，回调界面的前端的具体操作逻辑就像第二步的一致
     *
     * 4、登出的时候就需要清空全局门票和用户会话信息
     *
     * 需要注意就是制作全局门票和临时票据时，构建的键名和键值，UUID 生成的字符串构建为键名，用户Id 作为键值
     *
     * @param request HttpServletRequest 用于获取 Cookie 信息，检查是否已经登陆
     * @param response HttpServletResponse
     * @param model model 实例，用于填充登陆界面模板里回调地址
     * @param returnUrl 回调地址
     * @return
     */
    @GetMapping("login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        Model model,
                        String returnUrl) {

        // 检查是否包含对应的 Cookie
        String globalTicket = CookieUtils.getCookieValue(request, COOKIE_USER_TICKET, true);
        if (!StringUtils.isBlank(globalTicket)) {
            String userId = redisOperator.get(REDIS_USER_TICKET + ":" + globalTicket);
            // 从 Cookie 中获取到全局门票，并且通过 Redis 的检验，获取到 userId
            if (!StringUtils.isBlank(userId)) {
                // 构建临时票据，添加到 Redis 和回调地址中
                String tmpTicket = UUID.randomUUID().toString();
                redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, userId);
                return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
            }
        }

        // 前端模板填充回调地址
        model.addAttribute("returnUrl", returnUrl);
        return "login";
    }

    /**
     * 具体登录逻辑
     * @param username 用户名
     * @param password 用户密码
     * @param returnUrl 回调地址
     * @param model model 对象，用于设置模板返回错误信息
     * @param request HttpServletRequest 对象用于设置 Cookie
     * @param reponse HttpServletResponse 对象
     * @return
     * @throws Exception
     */
    @PostMapping
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse reponse) throws Exception {
        // 填充回调地址，因为后续可能会因为错误，而重新进入 登录模板 界面
        model.addAttribute("returnUrl", returnUrl);

        // 检查登录信息
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "登录信息不能为空！！");
            return "login";
        }

        // 检查登录信息是否正确
        Users result = usersService.queryUserForLogin(username, password);
        if (result == null) {
            model.addAttribute("errmsg", "用户名和密码不一致");
            return "login";
        }

        String userId = result.getId();

        // 1、构建全局门票，保存到 Redis 和 Cookie 中
        String globalTicket = UUID.randomUUID().toString();
        redisOperator.set(REDIS_USER_TICKET + ":" + globalTicket, userId);
        CookieUtils.setCookie(request, reponse, COOKIE_USER_TICKET, globalTicket, true);

        // 2、构建临时票据，保存到 Redis 中，并设置过期时间，并添加到回调地址的参数中
        String tmpTicket = UUID.randomUUID().toString();
        redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, userId, 600);

        // 3、保存用户会话（保存用户信息）
        // 需要保存全局门票信息，在登出的逻辑中需要获取到它进行清空处理
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(result, userVO);
        userVO.setUserUniqueToken(globalTicket);
        redisOperator.set(REDIS_USER_TOKEN + ":" + userId, JsonUtils.objectToJson(userVO));

        // 重定向到回调地址中（这里其实可以由前端操作，后端只需将包含票据的回调地址返回到前端即可）
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    /**
     * 回调地址界面根据url参数的临时票据请求后端检验该票据是否有效
     * 同时还要检验全局门票或用户会话是否存在，因为退出登录只会清空全局门票或用户会话，不会清空临时票据
     * 如果有效则返回用户信息
     * @param tmpTicket 临时票据
     * @return
     */
    @PostMapping("verifyTmpTicket")
    @ResponseBody
    public ServerResponse verifyTmpTicket(@RequestParam  String tmpTicket) {
        if (StringUtils.isBlank(tmpTicket)) {
            return ServerResponse.createdByError("用户票据异常！");
        }

        // 根据用户临时票据获取用户 Id
        String userId = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isBlank(userId)) {
            return ServerResponse.createdByError("用户票据异常！");
        } else {
            // 清除临时票据
            redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);
        }
        // 根据用户 Id 获取用户会话中的用户信息
        // 登出逻辑里只会清空全局门票和用户会话
        String userMsg = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userMsg)) {
            return ServerResponse.createdByError("用户票据异常！");
        }
        UserVO userVO = JsonUtils.jsonToPojo(userMsg, UserVO.class);
        return ServerResponse.createdBySuccess(userVO);
    }

    /**
     * 登出逻辑，清空 Redis 中的全局门票和用户会话
     * 登录界面的 Cookie 可以由前端使用过期门票发送请求获取到错误信息后，有前端进行清空操作
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public ServerResponse logout(@RequestParam String userId) {
        // 清空 Redis 中会话，门票
        // 获取用户会话，从而获取门票
        String userMsg = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (!StringUtils.isBlank(userMsg)) {
            UserVO userVO = JsonUtils.jsonToPojo(userMsg, UserVO.class);
            // 获取全局门票
            String globalTicket = userVO.getUserUniqueToken();
            // 删除全局门票
            redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
            redisOperator.del(REDIS_USER_TICKET + ":" + globalTicket);
        }
        return ServerResponse.createdBySuccess();
    }
}
