package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.bo.ShopCartBO;
import top.seiei.utils.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车模块", tags = {"用于购物车模块的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    static final Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    /**
     * 添加购物车
     * @param userId 用户 Id
     * @param shopCartBO 添加购物车商品 BO
     * @param request request 对象
     * @param response response 对象
     * @return
     */
    @ApiOperation(value = "添加购物车", notes = "添加购物车", httpMethod = "POST")
    @PostMapping("/add")
    public ServerResponse add(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @RequestBody ShopCartBO shopCartBO,
            HttpServletRequest request,
            HttpServletResponse response
            ) {

        // todo 判断用户权限
        if (StringUtils.isBlank(userId)) {
            return ServerResponse.createdByError("");
        }

        // todo 购物车数据同步到 redis 保存
        // 问题：
        // 1、在没有登录的时候，购物车的数据是添加到 cookie 中，那么登录之后是否就马上更新购物车的数据到 redis 中？
        // 2、登录之后的购物车信息是否需要组合成 cookie 发送到前端去？
        // 3、在已经登录后，执行添加购物车操作，后端还需要操作 cookie 信息吗？
        logger.info(shopCartBO.toString());

        return ServerResponse.createdBySuccess();
    }

    /**
     * 删除购物车
     * @param userId 用户主键
     * @param itemSpecId 规格主键
     * @param request request 对象
     * @param response response 对象
     * @return
     */
    @ApiOperation(value = "删除购物车", notes = "删除购物车", httpMethod = "POST")
    @PostMapping("/del")
    public ServerResponse del(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "规格主键", required = true)
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        // todo 判断用户权限
        if (StringUtils.isBlank(userId)) {
            return ServerResponse.createdByError("");
        }

        // todo 购物车数据同步到 redis 保存

        return ServerResponse.createdBySuccess();
    }
}
