package com.test;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import top.seiei.es.pojo.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class EStest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    // ------------------------------------ 操作索引库 --------------------------------------------

    /**
     * 使用 ElasticsearchTemplate 插入文档，从而起到 新增索引库/新增索引库属性
     *
     * 不建议使用 ElasticsearchTemplate 对索引库进行管理（创建，更新，删除）
     * 索引库就像数据库或者数据库中的表，平时是不会通过 Java 代码频繁创建修改删除数据库的表
     * 只会针对数据做 CRUD 的操作
     * 而 ElasticsearchTemplate 里头操作索引库 Bug 多
     */
    //@Test
    public void createdOrUpdateIndex() {
        // 构建文档源信息
        Student student = new Student();
        student.setStuId(1L);
        student.setName("seiei");
        student.setAge(18);
        student.setSex(true);
        student.setDesc("走在风中，今天阳光突然好温柔！");

        // 使用 index 操作类型，它用于创建或替换一个现有的文档，即还可以提供新增字段属性的功能
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(student).build();
        elasticsearchTemplate.index(indexQuery);
    }

    /**
     * 删除索引
     */
    //@Test
    public void deleteIndex() {
        elasticsearchTemplate.deleteIndex(Student.class);
    }

    // ------------------------------------ 操作文档 --------------------------------------------

    /**
     * 更新文档
     */
    //@Test
    public void updateStudentDoc() {
        // 更新信息准备
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", "Taka");
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(updateMap);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                                    .withId("1") // 文档 Id
                                    .withClass(Student.class) // 映射索引表对象
                                    .withIndexRequest(indexRequest) // 具体更改数据内容
                                    .build();
        elasticsearchTemplate.update(updateQuery);
    }

    /**
     * 查询文档
     */
    //@Test
    public void getStudentDoc() {
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1");
        Student student = elasticsearchTemplate.queryForObject(getQuery, Student.class);
        System.out.println(student);
    }

    /**
     * 删除文档
     */
    //@Test
    public void deleteStudentDoc() {
        elasticsearchTemplate.delete(Student.class, "1");
    }

    // ------------------------------------ 全文检索 --------------------------------------------

    /**
     * 检索
     */
    //@Test
    public void search() {
        // 设置检索信息
        // QueryBuilders 内置很多方法，如 matchall，matchquery 等等
        QueryBuilder queryBuilder = QueryBuilders.termQuery("desc", "温柔");

        // 分页信息
        Pageable pageable = PageRequest.of(0 ,10);

        // 排序
        SortBuilder sortBuilder = new FieldSortBuilder("age").order(SortOrder.ASC);

        // 检索
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                                    .withQuery(queryBuilder) // 搜索条件，可以多条 withQuery
                                    .withPageable(pageable) // 分页
                                    .withSort(sortBuilder) // 排序
                                    .build();
        AggregatedPage<Student> result = elasticsearchTemplate.queryForPage(searchQuery, Student.class);

        System.out.println("总的分页数：" + result.getTotalPages());
        List<Student> listOfStudent = result.getContent();
        for (Student item : listOfStudent) {
            System.out.println(item);
        }
    }

    /**
     * 配置高亮
     */
    //@Test
    public void searchAndSetHighlight() {
        QueryBuilder queryBuilder = QueryBuilders.termQuery("desc", "温柔");
        Pageable pageable = PageRequest.of(0 ,10);
        // 高亮配置
        HighlightBuilder.Field highlightField = new HighlightBuilder.Field("desc").preTags("<span>").postTags("</span>");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder) // 搜索条件，可以多条 withQuery
                .withHighlightFields(highlightField) // 高亮配置
                .withPageable(pageable) // 分页
                .build();

        // 这里还需要设置 返回结果映射，否则它是直接返回 _source 即没有高亮信息
        AggregatedPage<Student> result = elasticsearchTemplate.queryForPage(searchQuery, Student.class, new SearchResultMapper() {
            // response 是返回结果集（使用 PostMan 返回的全部数据）
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Student> result = new ArrayList<>();
                // 获取命中的数据集合
                SearchHits hits = response.getHits();
                // 循环命中数据数据
                for (SearchHit hit : hits) {
                    // 获取高亮数据
                    String desc = hit.getHighlightFields().get("desc").getFragments()[0].toString();
                    Object id = (Object) hit.getSourceAsMap().get("stuId");
                    Boolean sex = (Boolean) hit.getSourceAsMap().get("sex");
                    String name = hit.getSourceAsMap().get("name").toString();
                    Integer age = (Integer) hit.getSourceAsMap().get("age");

                    Student student = new Student();
                    student.setDesc(desc);
                    student.setSex(sex);
                    student.setStuId(Long.valueOf(id.toString()));
                    student.setName(name);
                    student.setAge(age);
                    result.add(student);
                }
                // 设置返回数据，并返回
                if (result.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>)result);
                }
                return null;
            }
        });
        System.out.println("总的分页数：" + result.getTotalPages());
        List<Student> listOfStudent = result.getContent();
        for (Student item : listOfStudent) {
            System.out.println(item);
        }
    }


}
