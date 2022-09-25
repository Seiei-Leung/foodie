package top.seiei.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.seiei.enums.OrderStatusEnum;
import top.seiei.enums.YesOrNo;
import top.seiei.mapper.OrderItemsMapper;
import top.seiei.mapper.OrderStatusMapper;
import top.seiei.mapper.OrdersMapper;
import top.seiei.mapper.UserAddressMapper;
import top.seiei.pojo.OrderItems;
import top.seiei.pojo.OrderStatus;
import top.seiei.pojo.Orders;
import top.seiei.pojo.UserAddress;
import top.seiei.pojo.bo.ShopCartBO;
import top.seiei.pojo.bo.SumbitOrderBO;
import top.seiei.pojo.vo.ShopCartVO;
import top.seiei.service.ItemService;
import top.seiei.service.OrderService;
import top.seiei.utils.DateUtil;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.RedisOperator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private Sid sid;

    @Resource
    private UserAddressMapper userAddressMapper;

    @Resource
    private ItemService itemService;

    @Resource
    private RedisOperator redisOperator;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SumbitOrderBO sumbitOrderBO) {
        String orderId = sid.nextShort();
        String userId = sumbitOrderBO.getUserId();
        String addressId = sumbitOrderBO.getAddressId();
        String itemSpecIds = sumbitOrderBO.getItemSpecIds();
        Integer payMethod = sumbitOrderBO.getPayMethod();
        String leftMsg = sumbitOrderBO.getLeftMsg();
        Integer postAmount = 0; // 邮费默认为0
        // 获取地址信息
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);

        // 获取 Redis 购物车信息
        List<ShopCartBO> shopCartBOList = new ArrayList<>();
        String resultStrFromRedis = redisOperator.get("shopCart:" + userId);
        if (StringUtils.isNotBlank(resultStrFromRedis)) {
            shopCartBOList = JsonUtils.jsonToList(resultStrFromRedis, ShopCartBO.class);
        }

        // 整合数据，生成订单
        Integer totalAmount = 0; // 订单总价格
        Integer realPayAmount = 0; // 实际支付总价格
        List<ShopCartVO> shopCartVOList = itemService.getItemsBySpecIds(itemSpecIds);
        for (ShopCartVO shopCartVO : shopCartVOList) {
            // 结合 redius 购物车信息，获取购买商品数量
            Integer buyCounts = 0;
            ShopCartBO shopCartBOForDel = null;
            for (ShopCartBO item : shopCartBOList) {
                if (item.getSpecId().equals(shopCartVO.getSpecId())) {
                    buyCounts = item.getBuyCounts();
                    shopCartBOForDel = item;
                    break;
                }
            }
            if (buyCounts.equals(0)) {
                throw new RuntimeException(shopCartVO.getItemName() + " 的购买数量不能为空！！");
            }
            totalAmount += shopCartVO.getPriceNormal() * buyCounts;
            realPayAmount += shopCartVO.getPriceDiscount() * buyCounts;

            // 保存子订单
            String orderItemsId = sid.nextShort();
            OrderItems orderItems = new OrderItems();
            orderItems.setId(orderItemsId);
            orderItems.setOrderId(orderId);
            orderItems.setItemId(shopCartVO.getItemId());
            orderItems.setItemImg(shopCartVO.getItemImgUrl());
            orderItems.setItemName(shopCartVO.getItemName());
            orderItems.setItemSpecId(shopCartVO.getSpecId());
            orderItems.setItemSpecName(shopCartVO.getSpecName());
            orderItems.setPrice(shopCartVO.getPriceDiscount());
            orderItems.setBuyCounts(buyCounts);
            orderItemsMapper.insert(orderItems);

            // 扣除库存
            itemService.decreaseItemSpecStock(shopCartVO.getSpecId(), buyCounts);

            // 剔除 Redis 购物车对应的商品
            shopCartBOList.remove(shopCartBOForDel);
        }

        // 生成订单信息
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getDetail());
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);
        orders.setIsComment(YesOrNo.No.type);
        orders.setIsDelete(YesOrNo.No.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        ordersMapper.insertSelective(orders);

        // 生成订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        // 更新 Redis 购物车
        redisOperator.set("shopCart:" + userId, JsonUtils.objectToJson(shopCartBOList));

        return orderId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void notifyMerchantOrderPaid(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setPayTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus getOrderStatusByOrderId(String orderId) {
        return  orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOverTimeOrders() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatusList = orderStatusMapper.select(orderStatus);
        for (OrderStatus os : orderStatusList) {
            Date createdTime = os.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());
            // 超过一天，关闭订单
            if (days >= 1) {
                this.closeOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCloseTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

}
