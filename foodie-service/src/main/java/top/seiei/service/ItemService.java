package top.seiei.service;

import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;
import top.seiei.pojo.vo.CommentLevelCountsVO;
import top.seiei.utils.PagedGridResult;
import top.seiei.pojo.vo.SearchItemsVO;
import top.seiei.pojo.vo.ShopCartVO;

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

    /**
     * 根据关键词获取商品列表
     * @param keyWords 关键词
     * @param sort 排序方式
     * @param page 当前页数
     * @param pageSize 每页显示的数量
     * @return
     */
    public PagedGridResult<List<SearchItemsVO>> searchItems(String keyWords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类Id 获取商品列表
     * @param catId 分类Id
     * @param sort 排序方式
     * @param page 当前页数
     * @param pageSize 每页显示的数量
     * @return
     */
    public PagedGridResult<List<SearchItemsVO>> searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格id集合字符串获取最新商品信息
     * @param specIds 规格id集合字符串
     * @return
     */
    public List<ShopCartVO> getItemsBySpecIds(String specIds);

    /**
     * 减少库存
     * @param specId 规格 id
     * @param buyCounts 购买数量
     */
    public void decreaseItemSpecStock(String specId, int buyCounts);
}
