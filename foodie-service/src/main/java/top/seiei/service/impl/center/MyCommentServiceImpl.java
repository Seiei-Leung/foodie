package top.seiei.service.impl.center;

import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.seiei.enums.YesOrNo;
import top.seiei.mapper.ItemsCommentsCustomMapper;
import top.seiei.mapper.OrderItemsMapper;
import top.seiei.mapper.OrderStatusMapper;
import top.seiei.mapper.OrdersMapper;
import top.seiei.pojo.OrderItems;
import top.seiei.pojo.OrderStatus;
import top.seiei.pojo.Orders;
import top.seiei.pojo.bo.center.OrderItemsCommentVO;
import top.seiei.pojo.vo.PagedGridResult;
import top.seiei.pojo.vo.center.MyCommentVO;
import top.seiei.service.BaseService;
import top.seiei.service.center.MyCommentService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MyCommentServiceImpl extends BaseService implements MyCommentService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private Sid sid;

    @Resource
    private ItemsCommentsCustomMapper itemsCommentsCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> getPendingOrderItems(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        List<OrderItems> result = orderItemsMapper.select(orderItems);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(String orderId, String userId, List<OrderItemsCommentVO> orderItemsCommentVOList) {
        // 1、订单表修改 “是否已评价”字段
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.Yes.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 2、订单状态表修改评价时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);

        // 3、评价新增到评价表
        for (OrderItemsCommentVO orderItemsCommentVO : orderItemsCommentVOList) {
            orderItemsCommentVO.setCommentId(sid.nextShort());
        }
        itemsCommentsCustomMapper.saveComments(userId, orderItemsCommentVOList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getCommentListByUserId(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> result = itemsCommentsCustomMapper.getCommentListByUserId(userId);
        return setPagedGridResult(result, page);
    }


}
