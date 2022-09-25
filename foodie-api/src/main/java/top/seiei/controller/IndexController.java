package top.seiei.controller;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.seiei.enums.YesOrNo;
import top.seiei.pojo.Carousel;
import top.seiei.pojo.Category;
import top.seiei.pojo.vo.CategoryVO;
import top.seiei.pojo.vo.NewItemsVO;
import top.seiei.service.CarouselService;
import top.seiei.service.CategoryService;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Api(value = "首页模块", tags = {"用于首页获取信息的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Resource
    private CarouselService carouselService;

    @Resource
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 首页获取轮播图信息列表
     * @return
     */
    @ApiOperation(value = "首页获取轮播图信息列表", notes = "首页获取轮播图信息列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ServerResponse getCarousel() {
        List<Carousel> result = new ArrayList<>();
        // 从 Redis 获取轮播图信息
        String resultStrFromRedis = redisOperator.get("carousel");
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            // Json 转化为对象列表
            result = JsonUtils.jsonToList(resultStrFromRedis, Carousel.class);
        } else {
            // 如果 redis 没有对应的数据，即从数据库中获取
            result = carouselService.getAll(YesOrNo.Yes.type);
            // 获取到的数据存入 redis 中
            redisOperator.set("carousel", JsonUtils.objectToJson(result));
        }
        return ServerResponse.createdBySuccess(result);
    }

    /**
     * 首页获取顶级级别的大分类
     * @return
     */
    @ApiOperation(value = "获取顶级级别的大分类", notes = "首页获取顶级级别的大分类", httpMethod = "GET")
    @GetMapping("/cats")
    public ServerResponse getRootLevelCategory() {
        List<Category> result = new ArrayList<>();
        // 从 Redis 获取大分类信息
        String resultStrFromRedis = redisOperator.get("cats");
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            result = JsonUtils.jsonToList(resultStrFromRedis, Category.class);
        } else {
            result = categoryService.getRootLevelCategory();
            // 这里注意缓存穿透的问题
            redisOperator.set("cats", JsonUtils.objectToJson(result));
        }
        return ServerResponse.createdBySuccess(result);
    }

    /**
     * 根据某个父分类的主键，获取其下一级的分类信息列表
     * @param rootCatId 父类主键
     * @return
     */
    @ApiOperation(value = "根据某个父类主键获取其下一级的分类信息列表", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ServerResponse getSubCategoryList(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        // 从 Redis 获取分类信息
        List<CategoryVO> result = new ArrayList<>();
        String resultStrFromRedis = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            result = JsonUtils.jsonToList(resultStrFromRedis, CategoryVO.class);
        } else {
            result = categoryService.getSubCategoryList(rootCatId);
            // 这里注意缓存穿透的问题
            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(result));
        }
        return ServerResponse.createdBySuccess(result);
    }

    /**
     * 根据分类Id 获取六个商品推荐
     * @param rootCatId 父类主键
     * @return
     */
    @ApiOperation(value = "根据以及分类Id 获取六个商品推荐", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ServerResponse getSixNewItemsLazy(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        List<NewItemsVO> result = categoryService.getSixNewItemsLazy(rootCatId);
        return ServerResponse.createdBySuccess(result);
    }


}
