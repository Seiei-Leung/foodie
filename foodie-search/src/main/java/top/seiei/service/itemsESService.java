package top.seiei.service;

import top.seiei.es.pojo.Items;
import top.seiei.utils.PagedGridResult;

import java.util.List;

public interface itemsESService {

    /**
     * 根据关键词获取商品列表
     * @param keyWords 关键词
     * @param page 当前页数
     * @param pageSize 每页显示的数量
     * @return
     */
    public PagedGridResult<List<Items>> searchItems(String keyWords, Integer page, Integer pageSize);
}
