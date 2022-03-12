package top.seiei.mapper;

import top.seiei.pojo.vo.CategoryVO;
import top.seiei.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryCustomMapper {

    public List<CategoryVO> getSubCategoryList(Integer fatherId);

    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}