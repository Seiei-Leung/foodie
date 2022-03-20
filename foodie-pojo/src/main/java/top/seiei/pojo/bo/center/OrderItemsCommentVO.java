package top.seiei.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "商品评价BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class OrderItemsCommentVO {

    @ApiModelProperty(value = "评价Id", name = "commentId", required = false)
    private String commentId;
    @ApiModelProperty(value = "商品ID", name = "itemId")
    private String itemId;
    @ApiModelProperty(value = "商品名称", name = "itemName")
    private String itemName;
    @ApiModelProperty(value = "规格Id", name = "itemSpecId")
    private String itemSpecId;
    @ApiModelProperty(value = "规格名称", name = "itemSpecName")
    private String itemSpecName;
    @ApiModelProperty(value = "评价等级", name = "commentLevel")
    private Integer commentLevel;
    @ApiModelProperty(value = "评价内容", name = "content")
    private String content;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSpecId() {
        return itemSpecId;
    }

    public void setItemSpecId(String itemSpecId) {
        this.itemSpecId = itemSpecId;
    }

    public String getItemSpecName() {
        return itemSpecName;
    }

    public void setItemSpecName(String itemSpecName) {
        this.itemSpecName = itemSpecName;
    }

    public Integer getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(Integer commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
