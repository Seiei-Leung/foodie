package top.seiei.service.impl.center;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.seiei.enums.OrderStatusEnum;
import top.seiei.enums.YesOrNo;
import top.seiei.mapper.OrderStatusMapper;
import top.seiei.mapper.OrdersCustomMapper;
import top.seiei.mapper.OrdersMapper;
import top.seiei.pojo.OrderStatus;
import top.seiei.pojo.Orders;
import top.seiei.pojo.vo.center.MyOrderVO;
import top.seiei.utils.PagedGridResult;
import top.seiei.pojo.vo.center.OrderStatusCountsVO;
import top.seiei.pojo.vo.center.OrderTrendVO;
import top.seiei.service.BaseService;
import top.seiei.service.center.MyOrdereService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MyOrdereServiceImpl extends BaseService implements MyOrdereService {

    @Resource
    private OrdersCustomMapper ordersCustomMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        // 使用 pageHelper 插件
        PageHelper.startPage(page, pageSize);
        List<MyOrderVO> result = ordersCustomMapper.selectMyOrderVO(userId, orderStatus);
        return this.setPagedGridResult(result, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateDeliveryOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setDeliverTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);

        // 除了主键限制更新外，还需要订单状态限制更新
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return result == 1;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ServerResponse<Orders> getByUserIdAndOrderId(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsDelete(YesOrNo.No.type);
        orders = ordersMapper.selectOne(orders);
        if (orders == null) {
            return ServerResponse.createdByError("订单不存在！");
        }
        return ServerResponse.createdBySuccess(orders);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateSuccessOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setSuccessTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);

        // 除了主键限制更新外，还需要订单状态限制更新
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return result == 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrders(String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.Yes.type);

        // 除了主键限制更新外，还需要订单状态限制更新
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);

        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getStatusCounts(String userId) {
        return ordersCustomMapper.getStatusCounts(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OrderTrendVO> result = ordersCustomMapper.getOrderTrend(userId);
        return setPagedGridResult(result, page);
    }
}
