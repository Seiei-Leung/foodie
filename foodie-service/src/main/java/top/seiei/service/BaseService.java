package top.seiei.service;

import com.github.pagehelper.PageInfo;
import top.seiei.pojo.vo.PagedGridResult;

import java.util.List;

public class BaseService {

    /**
     * 转化为 PagedGridResult 返回
     * @param data 查询出来的数据
     * @param page 当前页数
     * @return
     */
    public PagedGridResult setPagedGridResult(List<?> data, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(data);
        PagedGridResult<List<?>> pagedGridResult = new PagedGridResult<List<?>>();
        pagedGridResult.setRows(data);
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());
        return pagedGridResult;
    }
}
