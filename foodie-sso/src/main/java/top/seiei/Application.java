package top.seiei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "top.seiei.mapper")
@ComponentScan(basePackages = {"top.seiei", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        // 使用 SpringApplication 中的 run 方法，它是一个静态的助手类方法，它用于执行一个 SpringApplication 实例
        SpringApplication.run(Application.class, args);
    }
}
