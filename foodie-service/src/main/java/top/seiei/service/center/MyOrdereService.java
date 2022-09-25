package top.seiei.service.center;

import top.seiei.pojo.Orders;
import top.seiei.utils.PagedGridResult;
import top.seiei.pojo.vo.center.OrderStatusCountsVO;
import top.seiei.utils.ServerResponse;

public interface MyOrdereService {

    /**
     * 获取用户的所有订单
     * @param userId 用户 Id
     * @param orderStatus 订单状态
     * @param page 当前页
     * @param pageSize 一页显示的条数
     * @return
     */
    public PagedGridResult getMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 发货，把订单状态改成发货状态
     * @param orderId 订单ID
     */
    public boolean updateDeliveryOrderStatus(String orderId);

    /**
     * 根据用户ID和订单ID获取订单信息
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return
     */
    public ServerResponse<Orders> getByUserIdAndOrderId(String userId, String orderId);

    /**
     * 更新订单状态为成功
     * @param orderId 订单ID
     */
    public boolean updateSuccessOrderStatus(String orderId);

    /**
     * 更新订单状态为关闭
     * @param orderId 订单ID
     */
    public boolean deleteOrders(String orderId);

    /**
     * 根据用户ID 获取该用户不同订单状态的订单个数
     * @param userId 用户Id
     * @return
     */
    public OrderStatusCountsVO getStatusCounts(String userId);

    /**
     * 根据用户ID 获取分页的订单动向
     * @param userId 用户ID
     * @param page 当前页数
     * @param pageSize 一页显示条数
     * @return
     */
    public PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize);
}
