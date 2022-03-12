package top.seiei.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.seiei.mapper.CategoryCustomMapper;
import top.seiei.mapper.CategoryMapper;
import top.seiei.pojo.Category;
import top.seiei.pojo.vo.CategoryVO;
import top.seiei.pojo.vo.NewItemsVO;
import top.seiei.service.CategorySerivce;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategorySerivceImpl implements CategorySerivce {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryCustomMapper categoryCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> getRootLevelCategory() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> result = categoryMapper.selectByExample(example);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCategoryList(Integer fatherId) {
        return categoryCustomMapper.getSubCategoryList(fatherId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        return categoryCustomMapper.getSixNewItemsLazy(rootCatId);
    }
}
