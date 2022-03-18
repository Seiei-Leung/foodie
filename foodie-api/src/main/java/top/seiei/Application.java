package top.seiei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 这个类用于启动项目的，@SpringBootApplication 注解表示
 * 点击 @SpringBootApplication 注释查看
 * 其中 @ComponentScan 这个注释表示扫描类的，Application 类声明在 top.seiei 中，他就会默认扫描当前包（即 top.seiei 包内）及子包下所有的类
 * 其中 @SpringBootConfiguration 则表示它是一个 bean 容器
 * 其中 @EnableAutoConfiguration 则表示自动装配备用的、默认的配置（tomcat、springMVC 之类的配置）,帮我们自动装配（为什么使用SpringBoot就可以做到几乎零配置）
 */
@SpringBootApplication
/**
 *  扫描 DAO 文件
 */
@MapperScan(basePackages = "top.seiei.mapper")
/**
 * 扫描所有包，以及相关组件包
 * 不添加 @ComponentScan 注释，就只会默认扫描 top.seiei 包
 */
@ComponentScan(basePackages = {"top.seiei", "org.n3r.idworker"})
/**
 *
 */
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        // 使用 SpringApplication 中的 run 方法，它是一个静态的助手类方法，它用于执行一个 SpringApplication 实例
        SpringApplication.run(Application.class, args);
    }
}
