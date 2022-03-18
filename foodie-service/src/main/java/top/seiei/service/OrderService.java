package top.seiei.service;

import top.seiei.pojo.OrderStatus;
import top.seiei.pojo.bo.SumbitOrderBO;

public interface OrderService {

    /**
     * 创建订单
     * @param sumbitOrderBO 提交订单 BO
     */
    public String createOrder(SumbitOrderBO sumbitOrderBO);

    /**
     * 微信支付回调接口时，将订单状态修改为已支付，且设置支付时间
     * @param orderId
     */
    public void notifyMerchantOrderPaid(String orderId);

    /**
     * 获取订单状态
     * @param orderId 订单ID
     * @return
     */
    public OrderStatus getOrderStatusByOrderId(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOverTimeOrders();

    /**
     * 关闭订单
     * @param orderId 订单主键
     */
    public void closeOrder(String orderId);

}
