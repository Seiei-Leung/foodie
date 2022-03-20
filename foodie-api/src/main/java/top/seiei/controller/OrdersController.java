package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.seiei.enums.PayMethod;
import top.seiei.pojo.OrderStatus;
import top.seiei.pojo.bo.SumbitOrderBO;
import top.seiei.service.OrderService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;

@Api(value = "订单模块", tags = {"用于管理订单模块的相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Resource
    private OrderService orderService;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 创建订单
     * @param sumbitOrderBO 订单信息BO对象
     * @return
     */
    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public ServerResponse createdOrder(@RequestBody SumbitOrderBO sumbitOrderBO) {
        if (!sumbitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type) && !sumbitOrderBO.getPayMethod().equals(PayMethod.ZHIFUBAO.type)) {
            return ServerResponse.createdByError("支付方式不正确");
        }
        String orderId;
        // 创建订单
        try {
            orderId = orderService.createOrder(sumbitOrderBO);
        } catch (RuntimeException error) {
            return ServerResponse.createdByError(error.getMessage());
        }

        // todo 清除购物车相对应的商品数据



        // 发送信息到支付中心
        // 执行 post 逻辑
        Integer result = restTemplate.postForObject("http://localhost:8088/orders/notifyMerchantOrderPaid?orderId=" + orderId, null, Integer.class);

        return ServerResponse.createdBySuccess(orderId);
    }

    /**
     * 微信支付回调接口
     * @param orderId 订单 ID
     * @return
     */
    @PostMapping("notifyMerchantOrderPaid")
    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    public Integer notifyMerchantOrderPaid(
            @ApiParam(name = "orderId", value = "订单 ID", required = true)
            @RequestParam String orderId) {
        orderService.notifyMerchantOrderPaid(orderId);
        return HttpStatus.OK.value();
    }

    /**
     * 查询订单是否支付成功
     * @param orderId 订单 ID
     * @return
     */
    @PostMapping("getPaidOrderInfo")
    @ApiOperation(value = "查询订单是否支付成功", notes = "查询订单是否支付成功", httpMethod = "POST")
    public ServerResponse getPaidOrderInfo(
            @ApiParam(name = "orderId", value = "订单 ID", required = true)
            @RequestParam String orderId) {
        OrderStatus orderStatus = orderService.getOrderStatusByOrderId(orderId);
        return ServerResponse.createdBySuccess(orderStatus);
    }
}
