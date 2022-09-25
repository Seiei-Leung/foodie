package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.bo.ShopCartBO;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;
import top.seiei.utils.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "购物车模块", tags = {"用于购物车模块的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    static final Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    @Autowired
    private RedisOperator redisOperator;

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

        // 购物车数据同步到 redis 保存
        // 问题：
        // 1、在没有登录的时候，购物车的数据是添加到 cookie 中，那么登录之后是否就马上更新购物车的数据到 redis 中？
        // 2、登录之后的购物车信息是否需要组合成 cookie 发送到前端去？
        // 3、在已经登录后，执行添加购物车操作，后端还需要操作 cookie 信息吗？
        String resultStrFromRedis = redisOperator.get("shopCart:" + userId);
        List<ShopCartBO> shopCartBOList = new ArrayList<>();
        // 该用户的购物车是否已经有数据
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            shopCartBOList = JsonUtils.jsonToList(resultStrFromRedis, ShopCartBO.class);
            Boolean isExist = false; // redis 中是否已经存储该商品信息
            for (ShopCartBO item : shopCartBOList) {
                if (shopCartBO.getSpecId().equals(item.getSpecId())) {
                    isExist = true;
                    // 增加数量
                    item.setBuyCounts(item.getBuyCounts() + shopCartBO.getBuyCounts());
                }
            }
            // 如果 redis 中没有存在相同的商品
            if (!isExist) {
                shopCartBOList.add(shopCartBO);
            }
        } else {
            // 该用户的购物车为空
            shopCartBOList.add(shopCartBO);
        }
        // 更新 Redis 数据
        redisOperator.set("shopCart:" + userId, JsonUtils.objectToJson(shopCartBOList));
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

        // 购物车数据同步到 redis 保存
        String keyOfRedis = "shopCart:" + userId;
        String resultStrFromRedis = redisOperator.get(keyOfRedis);
        // 该用户购物车是否存在数据
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(resultStrFromRedis, ShopCartBO.class);
            // 循环购物车剔除对应的商品
            for (ShopCartBO item : shopCartBOList) {
                if (item.getSpecId().equals(itemSpecId)) {
                    shopCartBOList.remove(item);
                    // 修改循环数组长度的时候，需要添加 break 调出循环，否则会报错
                    break;
                }
            }
            // 存储到 Redis 中
            redisOperator.set(keyOfRedis, JsonUtils.objectToJson(shopCartBOList));
        }
        return ServerResponse.createdBySuccess();
    }
}
