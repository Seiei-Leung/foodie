package top.seiei.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import top.seiei.es.pojo.Items;
import top.seiei.utils.PagedGridResult;
import top.seiei.service.itemsESService;

import java.util.ArrayList;
import java.util.List;

@Service
public class itemsESServiceImpl implements itemsESService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PagedGridResult<List<Items>> searchItems(String keyWords, Integer page, Integer pageSize) {

        String matchFieldName = "itemName"; // 检索字段名
        String preTag = "<span class='actived'>"; // 高亮前缀
        String postTag = "</span>"; // 高亮后缀

        // 设置检索信息
        // QueryBuilders 内置很多方法，如 matchall，matchquery 等等
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("itemName", keyWords);

        // 分页信息
        Pageable pageable = PageRequest.of(page ,pageSize);

        // 高亮配置
        HighlightBuilder.Field highlightField = new HighlightBuilder.Field(matchFieldName).preTags(preTag).postTags(postTag);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder) // 搜索条件，可以多条 withQuery
                .withHighlightFields(highlightField) // 高亮配置
                .withPageable(pageable) // 分页
                .build();

        // 这里还需要封装 返回结果映射，否则它是直接返回 _source 即没有高亮信息
        AggregatedPage<Items> result = elasticsearchTemplate.queryForPage(searchQuery, Items.class, new SearchResultMapper() {
            // response 是返回结果集（即使用 PostMan 请求时，返回的全部数据）
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Items> result = new ArrayList<>();
                // 获取命中的数据集合
                SearchHits hits = response.getHits();
                // 循环命中数据数据
                for (SearchHit hit : hits) {
                    // 获取高亮数据
                    String itemName = hit.getHighlightFields().get(matchFieldName).getFragments()[0].toString();
                    String itemId = hit.getSourceAsMap().get("itemId").toString();
                    String imgUrl = hit.getSourceAsMap().get("imgUrl").toString();
                    Integer sellCounts = (Integer) hit.getSourceAsMap().get("sellCounts");
                    Integer price = (Integer) hit.getSourceAsMap().get("price");

                    Items items = new Items();
                    items.setItemId(itemId);
                    items.setItemName(itemName);
                    items.setImgUrl(imgUrl);
                    items.setSellCounts(sellCounts);
                    items.setPrice(price);
                    result.add(items);
                }
                // 设置返回数据，并返回
                return new AggregatedPageImpl<>((List<T>)result, pageable, response.getHits().totalHits);
            }
        });
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(result.getContent());
        pagedGridResult.setPage(page + 1);
        pagedGridResult.setTotal(result.getTotalPages()); // 总页数
        pagedGridResult.setRecords(result.getTotalElements()); // 总命中数
        return pagedGridResult;
    }
}
