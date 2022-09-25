package top.seiei.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;
import top.seiei.utils.ResponseCode;
import top.seiei.utils.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * userToken 拦截器
 */
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 拦截请求后，执行后续逻辑之前
     * 检验 Redis 中是否含有该用户的 Token
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler
     * @return 返回 Ture 表示拦截后放行，返回 false 表示拦截后，驳回
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("headerUserId");
        String headerUserToken = request.getHeader("headerUserToken");
        String redisToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);

        // 判断传入的 Token 和 Redis 存储的 Token 是否一致
        if (StringUtils.isBlank(redisToken) || StringUtils.isBlank(headerUserToken) || !redisToken.equals(headerUserToken)) {
            // 设置 json 响应对象返回
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(JsonUtils.objectToJson(ServerResponse.createdByError(ResponseCode.NEED_LOGIN.getCode(), "用户尚未登录或登录已过期，请重新登录")));
                out.flush();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        return true;
    }

    /**
     * 请求 Controller 之后，渲染数据，视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求 Controller 之后，渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
