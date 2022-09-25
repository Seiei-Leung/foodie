package top.seiei.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*
    common properties 类
 */
@Component
// 指定资源文件所在的地址，resources 资源文件在打包之后，其所有文件都会存放在 classpath 中
@PropertySource("classpath:common.properties")
public class CommonProperties {

    @Value("${isDebug}")
    private Boolean isDebug;

    public Boolean getDebug() {
        return isDebug;
    }

    public void setDebug(Boolean debug) {
        isDebug = debug;
    }

}
