package top.seiei.service;

import org.springframework.web.multipart.MultipartFile;

public interface FdfsService {

    /**
     * 上传用户图片，并返回路径地址
     * @param pictureFile 文件
     * @param extName 文件后缀名
     * @return
     */
    public String uploadFace(MultipartFile pictureFile, String extName);

    public Boolean deleteFace(String filePath);
}
