package top.seiei.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ESconfig {

    /**
     * 不配置，启动项目的时候会报错（解决netty引起的issue）
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
