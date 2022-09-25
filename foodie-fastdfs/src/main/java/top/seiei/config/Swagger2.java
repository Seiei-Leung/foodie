package top.seiei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 访问文档的默认地址：http://localhost:8088/swagger-ui.html
 * 第三方ui的访问地址：http://localhost:8088/doc.html
 */

// 表明需要被 spring 扫描到
@Configuration
// 表明 开启 Swagger2 配置
@EnableSwagger2
public class Swagger2 {

    // 配置 Swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)      // 指定文档类型为 Swagger2
                    .apiInfo(apiInfo())     // 定义文档汇总信息
                    .select().apis(RequestHandlerSelectors.basePackage("top.seiei.controller"))     // 指定扫描controller包作为文档 api来源
                    .paths(PathSelectors.any())     // 取所有的 controller
                    .build();
    }

    /**
     * 定义文档汇总信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 aip 文档")   // 文档页标题
                .contact(new Contact("Seiei", "seiei.top", "seiei@qq.com"))     // 联系信息
                .description("专为天天吃货提供 api 文档")     // 详细描述
                .version("1.0.1")       // 文档版本号
                .termsOfServiceUrl("seiei.top")     // 服务条款地址
                .build();
    }
}
