package top.seiei.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.seiei.interceptor.UserTokenInterceptor;
import top.seiei.resource.CommonProperties;
import top.seiei.resource.FileUpload;

import javax.annotation.Resource;
import java.io.File;

/**
 * 1、使用 RestTemplate 配置
 * 2、添加静态资源服务
 *      - 覆写 WebMvcConfigurer 中的 addResourceHandlers 方法
 * 3、添加拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    String os = System.getProperty("os.name");

    @Resource
    private FileUpload fileUpload;

    @Resource
    private UserTokenInterceptor userTokenInterceptor;

    @Resource
    private CommonProperties commonProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /*
    也可以这样导入 UserTokenInterceptor
    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }*/

    /**
     * 添加静态资源服务
     * @param registry 静态资源服务注册器
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

    /**
     * 添加拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!commonProperties.getDebug()) {
            registry.addInterceptor(userTokenInterceptor)
                    .addPathPatterns("/shopcart/add")
                    .addPathPatterns("/shopcart/del")
                    .addPathPatterns("/address/list")
                    .addPathPatterns("/address/add")
                    .addPathPatterns("/address/update")
                    .addPathPatterns("/address/setDefalut")
                    .addPathPatterns("/address/delete")
                    .addPathPatterns("/orders/*")
                    .addPathPatterns("/center/*")
                    .addPathPatterns("/userInfo/*")
                    .addPathPatterns("/myorders/*")
                    .addPathPatterns("/mycomments/*")
                    .excludePathPatterns("/myorders/deliver") // 剔除路径
                    .excludePathPatterns("/orders/notifyMerchantOrderPaid");
            WebMvcConfigurer.super.addInterceptors(registry);
        }
    }
}
