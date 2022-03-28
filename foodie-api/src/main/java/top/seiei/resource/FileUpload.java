package top.seiei.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*
    properties 资源文件类
 */
@Component
// 指定资源文件所在的地址，resources 资源文件在打包之后，其所有文件都会存放在 classpath 中
@PropertySource("classpath:file-upload-prod.properties")
// 定义属性前缀
@ConfigurationProperties(prefix = "file")
public class FileUpload {

    private String imageUserFaceLocation;

    private String imageUserFaceUrl;

    private String staticImgPath;

    public String getStaticImgPath() {
        return staticImgPath;
    }

    public void setStaticImgPath(String staticImgPath) {
        this.staticImgPath = staticImgPath;
    }

    public String getImageUserFaceUrl() {
        return imageUserFaceUrl;
    }

    public void setImageUserFaceUrl(String imageUserFaceUrl) {
        this.imageUserFaceUrl = imageUserFaceUrl;
    }

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }
}
