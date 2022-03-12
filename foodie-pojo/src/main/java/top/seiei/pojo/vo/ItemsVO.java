package top.seiei.pojo.vo;

import top.seiei.pojo.Items;
import top.seiei.pojo.ItemsImg;
import top.seiei.pojo.ItemsParam;
import top.seiei.pojo.ItemsSpec;

import java.util.List;

public class ItemsVO {

    private Items items;
    private ItemsParam itemsParam;
    private List<ItemsImg> itemsImgList;
    private List<ItemsSpec> itemsSpecList;

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public ItemsParam getItemsParam() {
        return itemsParam;
    }

    public void setItemsParam(ItemsParam itemsParam) {
        this.itemsParam = itemsParam;
    }

    public List<ItemsImg> getItemsImgList() {
        return itemsImgList;
    }

    public void setItemsImgList(List<ItemsImg> itemsImgList) {
        this.itemsImgList = itemsImgList;
    }

    public List<ItemsSpec> getItemsSpecList() {
        return itemsSpecList;
    }

    public void setItemsSpecList(List<ItemsSpec> itemsSpecList) {
        this.itemsSpecList = itemsSpecList;
    }
}
