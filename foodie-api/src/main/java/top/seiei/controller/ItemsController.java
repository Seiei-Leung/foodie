package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;
import top.seiei.pojo.vo.*;
import top.seiei.service.ItemService;
import top.seiei.service.ItemsCommentsService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "商品模块", tags = {"用于获取商品信息的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{

    @Resource
    private ItemService itemService;

    @Resource
    private ItemsCommentsService itemsCommentsService;

    /**
     * 根据商品主键获取商品信息
     * @param itemId 商品主键
     * @return
     */
    @ApiOperation(value = "根据商品主键获取商品信息", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ServerResponse getItems(
            @ApiParam(name = "itemId", value = "商品主键", required = true, example = "cake-1001")
            @PathVariable String itemId) {
        if (itemId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        Items items = itemService.getItemById(itemId);
        ItemsParam itemsParam = itemService.getItemsParamByItemId(itemId);
        List<ItemsImg> itemsImgList = itemService.getItemsImgByItemId(itemId);
        List<ItemsSpec> itemsSpecList = itemService.getItemsSpecByItemId(itemId);

        ItemsVO itemsVO = new ItemsVO();
        itemsVO.setItems(items);
        itemsVO.setItemsParam(itemsParam);
        itemsVO.setItemsImgList(itemsImgList);
        itemsVO.setItemsSpecList(itemsSpecList);

        return ServerResponse.createdBySuccess(itemsVO);
    }

    /**
     * 根据商品主键获取商品评价的总数（分级别计算）
     * @param itemId 商品主键
     * @return
     */
    @ApiOperation(value = "根据商品主键获取商品评价的总数（分级别计算）", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ServerResponse getCommentLevelCountsVOByItemId(
            @ApiParam(name = "itemId", value = "商品主键", required = true, example = "cake-1001")
            @RequestParam String itemId) {
        if (itemId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.getCommentLevelCountsVOByItemId(itemId);
        return ServerResponse.createdBySuccess(commentLevelCountsVO);
    }

    /**
     *
     * 根据商品主键获取商品评价的总数（分级别计算）
     * @param itemId 商品主键
     * @param level 评价级别
     * @param page 当前页数
     * @param pageSize 每页个数
     * @return
     */
    @ApiOperation(value = "根据商品主键获取商品评价详情列表（可传入评价级别筛选）", httpMethod = "GET")
    @GetMapping("/comments")
    public ServerResponse getCommentVOByItemIdAndCommentLevel(
            @ApiParam(name = "itemId", value = "商品主键", required = true, example = "cake-1001")
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价级别", required = false, example = "1")
            @RequestParam(required = false) Integer level,
            @ApiParam(name = "page", value = "当前页数", required = false, example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "pageSize", value = "每页个数", required = false, example = "10")
            @RequestParam(required = false) Integer pageSize
    ) {
        if (itemId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;
        PagedGridResult<List<CommentVO>> commentVOList = itemsCommentsService.getCommentVOByItemIdAndCommentLevel(itemId, level, page, pageSize);
        return ServerResponse.createdBySuccess(commentVOList);
    }

    /**
     * 根据关键词获取商品列表
     * @param keywords 商品主键
     * @param sort 评价级别
     * @param page 当前页数
     * @param pageSize 每页个数
     * @return
     */
    @ApiOperation(value = "根据关键词获取商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ServerResponse searchItems(
            @ApiParam(name = "keywords", value = "商品关键词", required = true, example = "cake-1001")
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序类型", required = false, example = "1")
            @RequestParam(required = false) String sort,
            @ApiParam(name = "page", value = "当前页数", required = false, example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "pageSize", value = "每页个数", required = false, example = "10")
            @RequestParam(required = false) Integer pageSize
    ) {
        if (keywords == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;
        PagedGridResult<List<SearchItemsVO>> searchItemsVOList = itemService.searchItems(keywords, sort, page, pageSize);
        return ServerResponse.createdBySuccess(searchItemsVOList);
    }

    /**
     * 根据分类主键获取商品列表
     * @param catId 分类主键
     * @param sort 评价级别
     * @param page 当前页数
     * @param pageSize 每页个数
     * @return
     */
    @ApiOperation(value = "根据分类主键获取商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ServerResponse searchItemsByCatId(
            @ApiParam(name = "catId", value = "分类主键", required = true, example = "cake-1001")
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序类型", required = false, example = "1")
            @RequestParam(required = false) String sort,
            @ApiParam(name = "page", value = "当前页数", required = false, example = "1")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "pageSize", value = "每页个数", required = false, example = "10")
            @RequestParam(required = false) Integer pageSize
    ) {
        if (catId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;
        PagedGridResult<List<SearchItemsVO>> searchItemsVOList = itemService.searchItems(catId, sort, page, pageSize);
        return ServerResponse.createdBySuccess(searchItemsVOList);
    }

    /**
     * 根据商品规格id集合字符串获取最新商品信息
     * @param itemSpecIds 商品规格id集合字符串
     * @return
     */
    @ApiOperation(value = "根据商品规格id集合字符串获取最新商品信息", httpMethod = "GET")
    @GetMapping("/refresh")
    public ServerResponse getItemsBySpecIds(
            @ApiParam(name = "itemSpecIds", value = "商品规格id集合字符串", required = true, example = "1,2,3")
            @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return ServerResponse.createdByError("参数不能为空");
        }
        List<ShopCartVO> result = itemService.getItemsBySpecIds(itemSpecIds);
        return ServerResponse.createdBySuccess(result);
    }
}
