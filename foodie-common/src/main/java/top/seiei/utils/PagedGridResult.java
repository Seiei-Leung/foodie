package top.seiei.utils;

public class PagedGridResult<T> {

    private T rows; // 数据列表
    private Integer page; // 当前页数
    private Integer total; // 总页数
    private Long records; // 总记录数

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getRecords() {
        return records;
    }

    public void setRecords(Long records) {
        this.records = records;
    }
}
