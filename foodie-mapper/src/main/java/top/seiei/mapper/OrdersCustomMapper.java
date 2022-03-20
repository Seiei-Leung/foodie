package top.seiei.mapper;

import org.apache.ibatis.annotations.Param;
import top.seiei.pojo.vo.center.MyOrderVO;
import top.seiei.pojo.vo.center.OrderStatusCountsVO;
import top.seiei.pojo.vo.center.OrderTrendVO;

import java.util.List;

public interface OrdersCustomMapper {

    public List<MyOrderVO> selectMyOrderVO(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus);

    public OrderStatusCountsVO getStatusCounts(String userId);

    public List<OrderTrendVO> getOrderTrend(String userId);
}
