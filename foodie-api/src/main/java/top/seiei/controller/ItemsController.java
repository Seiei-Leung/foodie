package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;
import top.seiei.pojo.vo.ItemsVO;
import top.seiei.service.ItemService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "商品模块", tags = {"用于获取商品信息的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Resource
    private ItemService itemService;

    /**
     * 根据商品主键获取商品信息
     * @param itemId 商品主键
     * @return
     */
    @ApiOperation(value = "根据商品主键获取商品信息", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ServerResponse getItems(
            @ApiParam(name = "itemId", value = "商品主键", required = true)
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

}
