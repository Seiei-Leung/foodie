package top.seiei.service;

import top.seiei.pojo.Category;
import top.seiei.pojo.vo.CategoryVO;
import top.seiei.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategorySerivce {

    /**
     * 获取顶级级别的大分类
     * @return
     */
    public List<Category> getRootLevelCategory();

    /**
     * 根据父级Id 获取其下一级的所有子分类信息
     * @param fatherId 父类id
     * @return
     */
    public List<CategoryVO> getSubCategoryList(Integer fatherId);

    /**
     * 根据以及分类Id 获取六个商品推荐
     * @param rootCatId 父类id
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
