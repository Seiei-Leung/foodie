package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.seiei.utils.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Api(value = "测试模块", tags = {"用于测试模块的相关接口"})
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * 设置SpringSession
     * @param request HttpServletRequest
     * @param reponse HttpServletResponse
     * @param title 标题
     * @param content 内容
     * @return
     */
    @ApiOperation(value = "设置SpringSession", notes = "设置SpringSession", httpMethod = "GET")
    @GetMapping("/setSession")
    public ServerResponse setSession(HttpServletRequest request,
                                     HttpServletResponse reponse,
                                     @ApiParam(name = "title", value = "标题", required = true)
                                     @RequestParam String title,
                                     @ApiParam(name = "content", value = "内容", required = true)
                                     @RequestParam String content) {
        HttpSession session = request.getSession();
        session.setAttribute(title,content);
        return ServerResponse.createdBySuccess(session.getId());
    }

    /**
     * 获取SpringSession
     * @param request HttpServletRequest
     * @param reponse HttpServletResponse
     * @param title 标题
     * @return
     */
    @ApiOperation(value = "获取SpringSession", notes = "获取SpringSession", httpMethod = "GET")
    @GetMapping("/getSession")
    public ServerResponse getSession(HttpServletRequest request,
                                     HttpServletResponse reponse,
                                     @ApiParam(name = "title", value = "标题", required = true)
                                     @RequestParam String title) {
        HttpSession session = request.getSession();
        return ServerResponse.createdBySuccess(session.getAttribute(title));
    }
}
