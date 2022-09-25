package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.seiei.es.pojo.Items;
import top.seiei.utils.PagedGridResult;
import top.seiei.service.itemsESService;
import top.seiei.utils.ServerResponse;
import javax.annotation.Resource;
import java.util.List;

@Api(value = "ES 商品模块", tags = {"用于 ES 商品模块的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Resource
    private itemsESService itemsESService;

    /**
     * 根据关键词获取商品列表
     * @param keywords 商品主键
     * @param sort 排序类型
     * @param page 当前页数
     * @param pageSize 每页个数
     * @return
     */
    @ApiOperation(value = "根据关键词获取商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ServerResponse searchItems(
            @ApiParam(name = "keywords", value = "商品关键词", required = true, example = "好吃")
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序类型", required = false, example = "k")
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
        // 由于 ES 的分页，页码是从 0 开始，所以 page 要减一
        page -= 1;
        PagedGridResult<List<Items>> searchItemsVOList = itemsESService.searchItems(keywords, page, pageSize);
        return ServerResponse.createdBySuccess(searchItemsVOList);
    }
}
