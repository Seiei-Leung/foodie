package top.seiei.service;

import top.seiei.pojo.vo.CommentVO;
import top.seiei.utils.PagedGridResult;

import java.util.List;

public interface ItemsCommentsService {

    /**
     * 根据商品主键、评价级别获取商品评价详情列表，评价级别为 null 时，默认获取所有评价列表
     * @param itemId 商品主键
     * @param commentLevel 评价级别
     * @return
     */
    public PagedGridResult<List<CommentVO>> getCommentVOByItemIdAndCommentLevel(String itemId, Integer commentLevel, Integer page, Integer pageSize);
}
