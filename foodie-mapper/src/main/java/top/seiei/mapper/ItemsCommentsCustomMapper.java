package top.seiei.mapper;

import top.seiei.pojo.vo.CommentLevelCountsVO;

public interface ItemsCommentsCustomMapper {

    public CommentLevelCountsVO getCommentLevelCountsVOByItemId(String itemId);
}