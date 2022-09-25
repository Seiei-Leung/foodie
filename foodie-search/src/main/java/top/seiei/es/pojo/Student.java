package top.seiei.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Document 注释声明映射索引库对象
 * indexName 表示索引名称
 * type 值必须填写为 _doc
 */
@Document(indexName = "student", type = "_doc", replicas = 0, shards = 3)
public class Student {

    /**
     * @Id 注释表示 ES 创建文档的时候，会直接使用该属性作为文档的Id，而不是自动生成
     */
    @Id
    private Long stuId;

    /**
     * @Field 注释，表示该索引库的字段属性，store 表示是否存储，默认是 false
     * 可以在此设置 index 等属性
     */
    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private Boolean sex;

    @Field(store = true)
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", desc='" + desc + '\'' +
                '}';
    }
}
