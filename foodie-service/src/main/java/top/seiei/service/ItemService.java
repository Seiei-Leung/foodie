package top.seiei.service;

import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;
import top.seiei.pojo.vo.CommentLevelCountsVO;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品主键获取商品主信息
     * @param itemId 商品主键
     * @return
     */
    public Items getItemById(String itemId);

    /**
     * 根据商品主键获取商品图片列表
     * @param itemId 商品主键
     * @return
     */
    public List<ItemsImg> getItemsImgByItemId(String itemId);

    /**
     * 根据商品主键获取商品规格
     * @param itemId 商品主键
     * @return
     */
    public List<ItemsSpec> getItemsSpecByItemId(String itemId);

    /**
     * 根据商品主键获取商品参数
     * @param itemId 商品主键
     * @return
     */
    public ItemsParam getItemsParamByItemId(String itemId);

    /**
     * 根据商品主键获取评论数
     * @param itemId 商品主键
     * @return
     */
    public CommentLevelCountsVO getCommentLevelCountsVOByItemId(String itemId);
}
