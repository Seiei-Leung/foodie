package top.seiei.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import top.seiei.controller.BaseController;
import top.seiei.enums.YesOrNo;
import top.seiei.pojo.Orders;
import top.seiei.pojo.bo.center.OrderItemsCommentVO;
import top.seiei.pojo.vo.PagedGridResult;
import top.seiei.service.center.MyCommentService;
import top.seiei.service.center.MyOrdereService;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "我的评价模块", tags = {"用于用户的我的评价模块的相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentController extends BaseController {

    @Resource
    private MyOrdereService myOrdereService;

    @Resource
    private MyCommentService myCommentService;

    /**
     * 根据用户id和订单ID获取用户准备评价时的商品信息
     * @param userId 用户主键
     * @param orderId 订单ID
     * @return
     */
    @ApiOperation(value = "根据用户id和订单ID获取用户准备评价时的商品信息", notes = "根据用户id和订单ID获取用户准备评价时的商品信息", httpMethod = "POST")
    @PostMapping("/pending")
    public ServerResponse getPendingOrderItems(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId
    ) {
        if (userId == null && orderId == null) {
            return ServerResponse.createdByError("用户主键不能为空");
        }
        ServerResponse<Orders> serverResponse = myOrdereService.getByUserIdAndOrderId(userId, orderId);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        // 判断订单是否已评价
        Orders myOrder = serverResponse.getData();
        if (myOrder.getIsComment() == YesOrNo.Yes.type) {
            return ServerResponse.createdByError("该订单已评价！");
        }
        return ServerResponse.createdBySuccess(myCommentService.getPendingOrderItems(orderId));
    }

    /**
     * 用户保存评价
     * @param userId 用户Id
     * @param orderId 订单 id
     * @param orderItemList 评价列表
     * @return
     */
    @ApiOperation(value = "根据用户id和订单ID获取用户准备评价时的商品信息", notes = "根据用户id和订单ID获取用户准备评价时的商品信息", httpMethod = "POST")
    @PostMapping("/saveList")
    public ServerResponse saveComment(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "orderItemList", value = "订单ID", required = true)
            @RequestBody List<OrderItemsCommentVO> orderItemList
    ) {
        if (userId == null && orderId == null) {
            return ServerResponse.createdByError("用户主键不能为空");
        }
        ServerResponse<Orders> serverResponse = myOrdereService.getByUserIdAndOrderId(userId, orderId);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        // 判断订单是否已评价
        Orders myOrder = serverResponse.getData();
        if (myOrder.getIsComment() == YesOrNo.Yes.type) {
            return ServerResponse.createdByError("该订单已评价！");
        }
        if (orderItemList == null || orderItemList.size() == 0) {
            return ServerResponse.createdByError("评论内容不能为空！");
        }
        myCommentService.saveComment(orderId, userId, orderItemList);

        return ServerResponse.createdBySuccess();
    }

    /**
     * 根据用户ID 获取用户商品评价
     * @param userId 用户主键
     * @param page 当前页数
     * @param pageSize 一页显示条数
     * @return
     */
    @ApiOperation(value = "根据用户ID 获取用户商品评价", notes = "根据用户ID 获取用户商品评价", httpMethod = "POST")
    @PostMapping("/query")
    public ServerResponse getCommentListByUserId(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页数", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "一页显示条数", required = true)
            @RequestParam Integer pageSize
    ) {
        if (userId == null) {
            return ServerResponse.createdByError("用户主键不能为空");
        }
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;
        PagedGridResult result = myCommentService.getCommentListByUserId(userId, page, pageSize);
        return ServerResponse.createdBySuccess(result);
    }
}
