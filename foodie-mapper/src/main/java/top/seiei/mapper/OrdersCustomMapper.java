package top.seiei.mapper;

import org.apache.ibatis.annotations.Param;
import top.seiei.pojo.vo.center.MyOrderVO;

import java.util.List;

public interface OrdersCustomMapper {

    public List<MyOrderVO> selectMyOrderVO(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus);
}
