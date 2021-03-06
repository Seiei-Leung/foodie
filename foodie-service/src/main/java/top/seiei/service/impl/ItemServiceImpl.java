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
        // ?????????????????????
        Collections.addAll(specIdList, ids);
        List<ShopCartVO> result = itemsCustomMapper.getItemsBySpecIds(specIdList);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // ????????????????????????????????????????????????????????????
        // synchronized ????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????
        // ???????????? zookeepper reids

        // ?????????????????????????????????????????????????????????????????????????????????
        // ????????????update sql????????? where ??????????????????
        // ???????????????????????????????????????https://www.jianshu.com/p/d2ac26ca6525

        int result = itemsCustomMapper.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            // ?????? RuntimeException ????????????
            throw new RuntimeException("????????????");
        }
    }
}
