package top.seiei.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.seiei.mapper.*;
import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;
import top.seiei.pojo.vo.CommentLevelCountsVO;
import top.seiei.pojo.vo.PagedGridResult;
import top.seiei.pojo.vo.SearchItemsVO;
import top.seiei.pojo.vo.ShopCartVO;
import top.seiei.service.BaseService;
import top.seiei.service.ItemService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItemServiceImpl extends BaseService implements ItemService {

    @Resource
    private ItemsMapper itemsMapper;

    @Resource
    private ItemsImgMapper itemsImgMapper;

    @Resource
    private ItemsSpecMapper itemsSpecMapper;

    @Resource
    private ItemsParamMapper itemsParamMapper;

    @Resource
    private ItemsCommentsCustomMapper itemsCommentsCustomMapper;

    @Resource
    private ItemsCustomMapper itemsCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items getItemById(String itemId) {
        Example example = new Example(Items.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", itemId);
        return itemsMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> getItemsImgByItemId(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> getItemsSpecByItemId(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam getItemsParamByItemId(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO getCommentLevelCountsVOByItemId(String itemId) {
        return itemsCommentsCustomMapper.getCommentLevelCountsVOByItemId(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult<List<SearchItemsVO>> searchItems(String keyWords, String sort, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOList = itemsCustomMapper.searchItems(keyWords, sort);
        return this.setPagedGridResult(searchItemsVOList, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult<List<SearchItemsVO>> searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOList = itemsCustomMapper.searchItemsByCatId(catId, sort);
        return this.setPagedGridResult(searchItemsVOList, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> getItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdList = new ArrayList<>();
        // 数组转化到集合
        Collections.addAll(specIdList, ids);
        List<ShopCartVO> result = itemsCustomMapper.getItemsBySpecIds(specIdList);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // 关于高并发可能导致库存数据问题，解决方法
        // synchronized 修饰词，不推荐使用，集群下无用，性能低下
        // 锁数据库：不推荐，导致数据库性能低下
        // 分布式锁 zookeepper reids

        // 这里使用乐观锁的形式，减少乐观锁的粒度，提高并发能力。
        // 直接写在update sql语句中 where 条件作为限制
        // 乐观锁与悲观锁的参考文章：https://www.jianshu.com/p/d2ac26ca6525

        int result = itemsCustomMapper.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            // 抛出 RuntimeException 用于回滚
            throw new RuntimeException("库存不足");
        }
    }
}
