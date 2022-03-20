package top.seiei.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.seiei.mapper.ItemsCommentsCustomMapper;
import top.seiei.pojo.vo.CommentVO;
import top.seiei.pojo.vo.PagedGridResult;
import top.seiei.service.BaseService;
import top.seiei.service.ItemsCommentsService;
import top.seiei.utils.DesensitizationUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemsCommentsServiceImpl extends BaseService implements ItemsCommentsService {

    @Resource
    private ItemsCommentsCustomMapper itemsCommentsCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult<List<CommentVO>> getCommentVOByItemIdAndCommentLevel(String itemId, Integer commentLevel, Integer page, Integer pageSize) {
        // 使用 pageHelper 插件
        PageHelper.startPage(page, pageSize);
        List<CommentVO> commentVOList = itemsCommentsCustomMapper.getCommentVOByItemIdAndCommentLevel(itemId, commentLevel);
        // 用户名脱敏
        for (CommentVO vo : commentVOList) {
            vo.setNickName(DesensitizationUtil.commonDisplay(vo.getNickName()));
        }
        return this.setPagedGridResult(commentVOList, page);
    }
}
