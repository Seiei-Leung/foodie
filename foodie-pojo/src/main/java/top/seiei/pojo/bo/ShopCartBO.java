package top.seiei.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 添加购物车传入的 BO
 */
@ApiModel(value = "添加购物车传入的 BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class ShopCartBO {

    @ApiModelProperty(value = "商品ID", name = "itemId", required = true)
    private String itemId;
    @ApiModelProperty(value = "商品图片", name = "itemImgUrl", required = true)
    private String itemImgUrl;
    @ApiModelProperty(value = "商品名称", name = "itemName", required = true)
    private String itemName;
    @ApiModelProperty(value = "商品规格ID", name = "specId", required = true)
    private String specId;
    @ApiModelProperty(value = "商品规格", name = "specName", required = true)
    private String specName;
    @ApiModelProperty(value = "购买个数", name = "buyCounts", required = true)
    private Integer buyCounts;
    @ApiModelProperty(value = "商品折扣价", name = "priceDiscount", required = true)
    private Integer priceDiscount;
    @ApiModelProperty(value = "商品原价", name = "priceNormal", required = true)
    private Integer priceNormal;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getBuyCounts() {
        return buyCounts;
    }

    public void setBuyCounts(Integer buyCounts) {
        this.buyCounts = buyCounts;
    }

    public Integer getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(Integer priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public Integer getPriceNormal() {
        return priceNormal;
    }

    public void setPriceNormal(Integer priceNormal) {
        this.priceNormal = priceNormal;
    }

    @Override
    public String toString() {
        return "ShopCartBO{" +
                "itemId='" + itemId + '\'' +
                ", itemImgUrl='" + itemImgUrl + '\'' +
                ", itemName='" + itemName + '\'' +
                ", specId='" + specId + '\'' +
                ", specName='" + specName + '\'' +
                ", buyCounts=" + buyCounts +
                ", priceDiscount=" + priceDiscount +
                ", priceNormal=" + priceNormal +
                '}';
    }
}
