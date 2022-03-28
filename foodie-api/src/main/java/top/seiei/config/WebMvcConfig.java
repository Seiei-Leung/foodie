package top.seiei.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.seiei.resource.FileUpload;

import javax.annotation.Resource;
import java.io.File;

/**
 * 1、使用 RestTemplate 配置
 * 2、添加静态资源服务
 *      - 覆写 WebMvcConfigurer 中的 addResourceHandlers 方法
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    String os = System.getProperty("os.name");

    @Resource
    private FileUpload fileUpload;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /*
        添加静态资源服务
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // windows 系统
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/") // 如果重写静态资源服务，则需要重新配置 Swagger2 映射
                                                             .addResourceLocations("file:" + File.separator + fileUpload.getStaticImgPath() + File.separator);
        }
        // linux 系统
        if (os.toLowerCase().startsWith("lin")) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/") // 如果重写静态资源服务，则需要重新配置 Swagger2 映射
                    .addResourceLocations("file:" + fileUpload.getStaticImgPath() + File.separator);
        }
    }
}
