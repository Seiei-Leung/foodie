package top.seiei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
// 表明需要被 spring 扫描到
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 添加 CORS 配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 设置允许跨域访问的前端 url
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("http://192.168.44.129:8080");

        // 设置允许发送 Cookie 信息，前端也是需要配置允许发送 Cookie 信息
        corsConfiguration.setAllowCredentials(true);

        // 设置允许请求的方式
        corsConfiguration.addAllowedMethod("*");

        // 设置允许的header
        corsConfiguration.addAllowedHeader("*");

        // 设置哪些后端url使用该跨域设置
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // 设置所有后端url都使用该跨域设置
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
