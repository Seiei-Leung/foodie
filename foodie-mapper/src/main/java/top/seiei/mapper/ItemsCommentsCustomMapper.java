package top.seiei.mapper;

import org.apache.ibatis.annotations.Param;
import top.seiei.pojo.bo.center.OrderItemsCommentVO;
import top.seiei.pojo.vo.CommentLevelCountsVO;
import top.seiei.pojo.vo.CommentVO;
import top.seiei.pojo.vo.center.MyCommentVO;

import java.util.List;

public interface ItemsCommentsCustomMapper {

    public CommentLevelCountsVO getCommentLevelCountsVOByItemId(String itemId);

    public List<CommentVO> getCommentVOByItemIdAndCommentLevel(@Param("itemId") String itemId, @Param("commentLevel") Integer commentLevel);

    public void saveComments(@Param("userId") String userId, @Param("commentList") List<OrderItemsCommentVO> commentList);

    public List<MyCommentVO> getCommentListByUserId(String userId);
}