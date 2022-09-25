package top.seiei.service.center;

import top.seiei.pojo.OrderItems;
import top.seiei.pojo.bo.center.OrderItemsCommentVO;
import top.seiei.utils.PagedGridResult;

import java.util.List;

public interface MyCommentService {

    /**
     * 根据订单ID获取 获取用户准备评价时的商品信息
     * @param orderId 订单ID
     * @return
     */
    public List<OrderItems> getPendingOrderItems(String orderId);

    /**
     * 用户评价商品
     * @param orderId 订单 Id
     * @param userId 用户 Id
     * @param orderItemsCommentVOList 评价列表
     */
    public void saveComment(String orderId, String userId, List<OrderItemsCommentVO> orderItemsCommentVOList);

    /**
     * 根据用户ID 获取用户商品评价
     * @param userId 用户 ID
     * @param page 当前页数
     * @param pageSize 一页显示条数
     * @return
     */
    public PagedGridResult getCommentListByUserId(String userId, Integer page, Integer pageSize);
}
