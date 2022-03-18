package top.seiei.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新增或修改用户地址传入的 BO
 */
@ApiModel(value = "新增或修改用户地址传入的 BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class AddressBO {

    @ApiModelProperty(value = "地址ID", name = "addressId", required = false)
    private String addressId;
    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private String userId;
    @ApiModelProperty(value = "收件人姓名", name = "receiver", required = false)
    private String receiver;
    @ApiModelProperty(value = "收件人手机号", name = "mobile", required = false)
    private String mobile;
    @ApiModelProperty(value = "省份", name = "province", required = false)
    private String province;
    @ApiModelProperty(value = "城市", name = "city", required = false)
    private String city;
    @ApiModelProperty(value = "区县", name = "district", required = false)
    private String district;
    @ApiModelProperty(value = "详细地址", name = "detail", required = false)
    private String detail;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
