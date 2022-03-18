package top.seiei.mapper;

import org.apache.ibatis.annotations.Param;
import top.seiei.pojo.vo.SearchItemsVO;
import top.seiei.pojo.vo.ShopCartVO;

import java.util.List;

public interface ItemsCustomMapper {

    public List<SearchItemsVO> searchItems(@Param("keyWords") String keyWords, @Param("sort") String sort);

    public List<SearchItemsVO> searchItemsByCatId(@Param("catId") Integer catId, @Param("sort") String sort);

    public List<ShopCartVO> getItemsBySpecIds(@Param("specIds") List<String> specIds);

    public int decreaseItemSpecStock(@Param("specId") String specId, @Param("pendingCounts") Integer pendingCounts);
}