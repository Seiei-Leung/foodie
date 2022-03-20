package top.seiei.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import top.seiei.controller.BaseController;
import top.seiei.pojo.vo.PagedGridResult;
import top.seiei.service.center.MyOrdereService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;

@Api(value = "我的订单模块", tags = {"用于用户的我的订单模块的相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrderController extends BaseController {

    @Resource
    private MyOrdereService myOrdereService;

    /**
     * 根据用户id获取我的订单信息
     * @param userId 用户主键
     * @param orderStatus 订单状态
     * @param page 当前页数
     * @param pageSize 每页个数
     * @return
     */
    @ApiOperation(value = "根据用户id获取我的订单信息", notes = "根据用户id获取我的订单信息", httpMethod = "POST")
    @PostMapping("/query")
    public ServerResponse getMyOrders(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = true)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "当前页数", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页个数", required = true)
            @RequestParam Integer pageSize
    ) {
        if (userId == null) {
            return ServerResponse.createdByError("用户主键不能为空");
        }
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;
        PagedGridResult result = myOrdereService.getMyOrders(userId, orderStatus, page, pageSize);
        return ServerResponse.createdBySuccess(result);
    }

    /**
     * 根据订单Id发货，修改订单状态为发货
     * @param orderId
     * @return
     */
    @ApiOperation(value = "根据订单Id发货，修改订单状态为发货", notes = "根据订单Id发货，修改订单状态为发货", httpMethod = "GET")
    @GetMapping("/delivery")
    public ServerResponse delivery(
            @ApiParam(name = "orderId", value = "用户主键", required = true)
            @RequestParam String orderId
    ) {
        if (orderId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        myOrdereService.updateDeliveryOrderStatus(orderId);
        return ServerResponse.createdBySuccess();
    }

    /**
     * 确认收货
     * @param userId 用户主键
     * @param orderId 订单ID
     * @return
     */
    @ApiOperation(value = "确认收货", notes = "确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public ServerResponse confirmReceive(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId
    ) {
        if (userId == null || orderId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        ServerResponse serverResponse = myOrdereService.getByUserIdAndOrderId(userId, orderId);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        if (!myOrdereService.updateSuccessOrderStatus(orderId)) {
            return ServerResponse.createdByError("确认收货失败！");
        }
        return ServerResponse.createdBySuccess();
    }

    /**
     * 删除订单
     * @param userId 用户主键
     * @param orderId 订单ID
     * @return
     */
    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public ServerResponse delete(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId
    ) {
        if (userId == null || orderId == null) {
            return ServerResponse.createdByError("参数不能为空");
        }
        ServerResponse serverResponse = myOrdereService.getByUserIdAndOrderId(userId, orderId);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        if (!myOrdereService.deleteOrders(orderId)) {
            return ServerResponse.createdByError("删除失败");
        }
        return ServerResponse.createdBySuccess();
    }

    @ApiOperation(value = "根据用户Id获取用户订单不同状态下的数量", notes = "根据用户Id获取用户订单不同状态下的数量", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public ServerResponse delete(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
    ) {

}
