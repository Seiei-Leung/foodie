package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.seiei.pojo.UserAddress;
import top.seiei.pojo.bo.AddressBO;
import top.seiei.service.AddressService;
import top.seiei.utils.MobileEmailUtils;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;

@Api(value = "用户地址模块", tags = {"用于管理用户地址模块的相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 根据用户id获取用户地址
     * @param userId 用户 id
     * @return
     */
    @ApiOperation(value = "根据用户id获取用户地址", notes = "根据用户id获取用户地址", httpMethod = "POST")
    @PostMapping("/list")
    public ServerResponse getAddressByUserId(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId
    ) {
        if (StringUtils.isBlank(userId)) {
            return ServerResponse.createdByError("参数不能为空");
        }

        return ServerResponse.createdBySuccess(addressService.getAddressByUserId(userId));
    }

    /**
     * 用户新增地址
     * @param addressBO 地址BO
     * @return
     */
    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ServerResponse addAddress(
            @RequestBody AddressBO addressBO
    ) {
        ServerResponse checkResult = this.checkAddressBO(addressBO);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        UserAddress userAddress = addressService.addUserAddress(addressBO);
        return ServerResponse.createdBySuccess(userAddress);
    }

    /**
     * 用户修改地址
     * @param addressBO 地址BO
     * @return
     */
    @ApiOperation(value = "修改地址", notes = "修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public ServerResponse updateAddress(
            @RequestBody AddressBO addressBO
    ) {
        ServerResponse checkResult = this.checkAddressBO(addressBO);
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ServerResponse.createdByError("修改地址主键不能为空");
        }
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        UserAddress userAddress = addressService.updateUserAddress(addressBO);
        return ServerResponse.createdBySuccess(userAddress);
    }

    /**
     * 删除地址
     * @param userId 用户id
     * @param addressId 地址id
     * @return
     */
    @ApiOperation(value = "删除地址", notes = "删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public ServerResponse deleteAddress(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址id", required = true)
            @RequestParam String addressId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ServerResponse.createdByError("参数不能为空");
        }
        addressService.deleteAddress(userId, addressId);
        return ServerResponse.createdBySuccess();
    }

    /**
     * 修改默认地址
     * @param userId 用户Id
     * @param addressId 地址id
     * @return
     */
    @ApiOperation(value = "修改默认地址", notes = "修改默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public ServerResponse setDefaultAddress(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址id", required = true)
            @RequestParam String addressId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ServerResponse.createdByError("参数不能为空");
        }
        addressService.setDefalutAddress(userId, addressId);
        return ServerResponse.createdBySuccess();
    }

    /**
     * 检查传入的地址 BO 数据是否符合规格
     * @param addressBO 地址 BO 对象
     * @return
     */
    private ServerResponse checkAddressBO(AddressBO addressBO) {
        if (StringUtils.isBlank(addressBO.getReceiver())) {
            return ServerResponse.createdByError("收货人姓名不能为空");
        }
        if (addressBO.getReceiver().length() > 12) {
            return ServerResponse.createdByError("收货人姓名不能太长");
        }
        if (StringUtils.isBlank(addressBO.getMobile())) {
            return ServerResponse.createdByError("手机号码不能为空");
        }
        if (!MobileEmailUtils.checkMobileIsOk(addressBO.getMobile())) {
            return ServerResponse.createdByError("手机号码格式错误");
        }
        if (StringUtils.isBlank(addressBO.getCity()) ||
            StringUtils.isBlank(addressBO.getProvince()) ||
            StringUtils.isBlank(addressBO.getCity()) ||
            StringUtils.isBlank(addressBO.getDistrict())
        ) {
            return ServerResponse.createdByError("收货地址不能为空");
        }
        return ServerResponse.createdBySuccess();
    }
}
