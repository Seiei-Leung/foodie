package top.seiei.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * ES 文档映射
 * createIndex 表示在没有索引库的时候，是否自动生成索引库
 * type 可以在 es head 插件上查看索引信息，在 `mappings` 属性下的属性名则为 type
 * indexName 为索引库名
 */
@Document(indexName = "foodie-items", type = "doc", createIndex = false)
public class Items {

    // index 为 false ,无需倒排索引
    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String itemId;

    @Field(store = true, type = FieldType.Text, index = true)
    private String itemName;

    @Field(store = true, type = FieldType.Text, index = false)
    private String imgUrl;

    @Field(store = true, type = FieldType.Integer, index = false)
    private Integer sellCounts;

    @Field(store = true, type = FieldType.Integer, index = false)
    private Integer price;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
