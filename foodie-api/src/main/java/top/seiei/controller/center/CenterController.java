package top.seiei.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.center.CenterUserBO;
import top.seiei.service.center.CenterUserService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户中心首页模块", tags = {"用于用户中心首页模块的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Resource
    private CenterUserService centerUserService;

    /**
     * 根据用户id获取用户信息
     * @param userId 用户 ID
     * @return
     */
    @ApiOperation(value = "根据用户id获取用户信息", notes = "根据用户id获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public ServerResponse getUserInfoByUserId(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId
    ) {
        if (StringUtils.isBlank(userId)) {
            return ServerResponse.createdByError("参数不能为空");
        }
        return ServerResponse.createdBySuccess(centerUserService.getByUserid(userId));
    }
}
