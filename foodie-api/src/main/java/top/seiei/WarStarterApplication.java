package top.seiei;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * war 包启动类
 * 开发阶段时，是直接手动调用 Application 启动项目的
 * 当创建 war 包时，就需要依托一个 war 启动类
 * 当启动 war 包时，会进入到 SpringBootServletInitializer，此时重写 configure 方法就需要指向 SpringBoot 启动类
 */
public class WarStarterApplication extends SpringBootServletInitializer {

    // 指向 Application 这个 SpringBoot 启动类
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
